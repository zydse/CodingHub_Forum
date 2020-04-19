package top.zydse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.dto.TagTypeDTO;
import top.zydse.elasticsearch.dao.PublishRepository;
import top.zydse.elasticsearch.entity.Publish;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.*;
import top.zydse.model.*;
import top.zydse.model.Collection;
import top.zydse.service.QuestionService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: QuestionService
 * Description:
 *
 * @Date: 2020/3/9
 */
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ViewHistoryMapper viewHistoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CommonExtensionMapper extensionMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private PublishRepository publishRepository;
    @Autowired
    private CollectionMapper collectionMapper;

    //有查询条件的方法
    public PaginationDTO<QuestionDTO> findAll(String search, Integer page, Integer size) {
        HashMap<String, Float> fields = new HashMap<>();
        fields.put("title", 0.5f);
        fields.put("description", 0.5f);
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(search).fields(fields);
        NativeSearchQuery countQuery = new NativeSearchQueryBuilder()
                .withIndices("index_publish")
                .withQuery(queryBuilder)
                .build();
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        long totalCount = elasticsearchTemplate.count(countQuery);
        log.info("count for search condition:{}", totalCount);
        paginationDTO.setPagination((int) totalCount, page, size);
        NativeSearchQuery resultQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(paginationDTO.getCurrentPage() - 1, size))
                .withSort(SortBuilders.fieldSort("gmtLastComment").order(SortOrder.DESC))
                .build();
        List<Publish> publishes = elasticsearchTemplate.queryForList(resultQuery, Publish.class);
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Publish publish : publishes) {
            User user = new User();
            user.setAvatarUrl(publish.getAvatarUrl());
            user.setName(publish.getName());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(publish, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        paginationDTO.setPageData(dtoList);
        return paginationDTO;
    }

    //没有查询条件的查询方法
    public PaginationDTO<QuestionDTO> findAll(Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        int totalCount = (int) questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page, size);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("is_top desc, is_quality desc, gmt_last_comment desc");
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    public PaginationDTO<QuestionDTO> findAll(Long id, int page, int size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(id);
        example.setOrderByClause("gmt_modified desc");
        int totalCount = (int) questionMapper.countByExample(example);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> dtoList = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(questionList.get(0).getCreator());
        for (Question plainQuestion : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(plainQuestion, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        paginationDTO.setPageData(dtoList);
        return paginationDTO;
    }

    @NotNull
    private PaginationDTO<QuestionDTO> getQuestionDTOPaginationDTO(PaginationDTO<QuestionDTO> paginationDTO, List<Question> questionList) {
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Question plainQuestion : questionList) {
            User user = userMapper.selectByPrimaryKey(plainQuestion.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(plainQuestion, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        paginationDTO.setPageData(dtoList);
        return paginationDTO;
    }

    @Transactional
    public QuestionDTO viewQuestion(Long questionId, User viewer) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        boolean viewed = false;
        User creator = userMapper.selectByPrimaryKey(question.getCreator());
        if (viewer != null && !(viewed = viewer.getId().equals(creator.getId()))) {
            //登录用户，浏览别人发帖，记录浏览历史
            ViewHistoryExample viewHistoryExample = new ViewHistoryExample();
            viewHistoryExample.createCriteria()
                    .andViewerEqualTo(viewer.getId())
                    .andQuestionIdEqualTo(questionId);
            List<ViewHistory> viewHistories = viewHistoryMapper.selectByExample(viewHistoryExample);
            if (viewHistories.size() == 0) {
                //第一次浏览
                ViewHistory history = new ViewHistory();
                history.setViewer(viewer.getId());
                history.setQuestionId(questionId);
                history.setGmtCreate(System.currentTimeMillis());
                history.setGmtModified(history.getGmtCreate());
                viewHistoryMapper.insertSelective(history);
            } else {
                //重复浏览
                viewed = true;
                ViewHistory history = viewHistories.get(0);
                history.setGmtModified(System.currentTimeMillis());
                viewHistoryMapper.updateByPrimaryKey(history);
            }
        }
        if (!viewed)
            increaseViewCount(questionId);
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        List<Tag> tagList = extensionMapper.listTagsByQuestion(questionId);
        if (viewer != null) {
            CollectionExample collectionExample = new CollectionExample();
            collectionExample.createCriteria()
                    .andQuestionIdEqualTo(questionId)
                    .andUserIdEqualTo(viewer.getId());
            List<Collection> collectionList = collectionMapper.selectByExample(collectionExample);
            dto.setIsCollect(collectionList.size() == 0 ? 0 : 1);
        }
        dto.setTags(tagList);
        dto.setUser(creator);
        return dto;
    }

    @Transactional
    public void saveOrUpdate(Question question, String tag, String avatarUrl, String name) {
        String[] tags = tag.split(",");
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andTagNameIn(Arrays.asList(tags));
        List<Tag> tagList = tagMapper.selectByExample(tagExample);
        if (tags.length != tagList.size()) {
            throw new CustomizeException(CustomizeErrorCode.TAG_NOT_FOUND);
        }
        Map<String, Tag> tagMap = tagList.stream().collect(Collectors.toMap(Tag::getTagName, obj -> obj));
        if (question.getId() == null) {
            //创建一个问题
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setGmtLastComment(question.getGmtCreate());
            extensionMapper.savePublish(question);
            //保存到es
            Publish publish = new Publish();
            BeanUtils.copyProperties(question, publish);
            publish.setName(name);
            publish.setAvatarUrl(avatarUrl);
            publish.setCommentCount(0);
            publish.setViewCount(0);
            publish.setIsQuality(0);
            publish.setIsTop(0);
            publishRepository.save(publish);
            for (String t : tags) {
                Tag tagObj = tagMap.get(t);
                QuestionTag record = new QuestionTag();
                record.setQuestionId(question.getId());
                record.setTagId(tagObj.getId());
                record.setGmtCreate(System.currentTimeMillis());
                extensionMapper.insertQuestionTag(record);
                tagObj.setCount(tagObj.getCount() + 1);
                tagMapper.updateByPrimaryKey(tagObj);
            }
        } else {
            //更新问题信息
            question.setGmtModified(System.currentTimeMillis());
            int count = questionMapper.updateByPrimaryKeySelective(question);
            if (count != 1)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_ALREADY_DELETED);
            Optional<Publish> optional = publishRepository.findById(question.getId());
            if (!optional.isPresent())
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            Publish publish = optional.get();
            publish.setTitle(question.getTitle());
            publish.setDescription(question.getDescription());
            publishRepository.save(publish);
        }
    }

    private void increaseViewCount(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount(question.getViewCount() + 1);
        questionMapper.updateByPrimaryKey(question);
        Optional<Publish> optional = publishRepository.findById(question.getId());
        if (!optional.isPresent())
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        Publish publish = optional.get();
        publish.setViewCount(question.getViewCount());
        publishRepository.save(publish);
    }

    public QuestionDTO findById(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        List<Tag> tags = extensionMapper.listTagsByQuestion(questionId);
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        user.setPassword(null);
        user.setToken(null);
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        dto.setTags(tags);
        dto.setUser(user);
        return dto;
    }

    @Override
    public List<Question> relatedQuestion(QuestionDTO questionDTO) {
        List<Question> questionList = extensionMapper.relatedQuestion(questionDTO.getId());
        if (questionList == null || questionList.size() == 0)
            return null;
        return questionList;
    }

    @Override
    public List<TagTypeDTO> getAllTags() {
        List<Tag> tags = tagMapper.selectByExample(new TagExample());
        //拿到所有的标签类型language、database、、、
        List<String> types = tags.stream()
                .map(Tag::getTagType)
                .distinct()
                .collect(Collectors.toList());
        Map<String, List<Tag>> maps = tags.stream()
                .collect(Collectors.groupingBy(Tag::getTagType));
        List<TagTypeDTO> list = new ArrayList<>();
        for (String type : types) {
            TagTypeDTO dto = new TagTypeDTO();
            dto.setType(type);
            dto.setTags(maps.get(type));
            list.add(dto);
        }
        return list;
    }

    @Override
    @Transactional
    public int deleteById(Long questionId) {
        List<Tag> tagList = extensionMapper.listTagsByQuestion(questionId);
        if (tagList == null || tagList.size() == 0)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        for (Tag tag : tagList) {
            tag.setCount(tag.getCount() - 1);
            tagMapper.updateByPrimaryKey(tag);
        }
        publishRepository.deleteById(questionId);
        return questionMapper.deleteByPrimaryKey(questionId);
    }

    @Override
    @Transactional
    public int top(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        Integer isTop = question.getIsTop();
        question.setIsTop(1 == isTop ? 0 : 1);
        return questionMapper.updateByPrimaryKey(question);
    }

    @Override
    @Transactional
    public int quality(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        Integer isQuality = question.getIsQuality();
        question.setIsQuality(1 == isQuality ? 0 : 1);
        return questionMapper.updateByPrimaryKey(question);
    }

    @Override
    public PaginationDTO<QuestionDTO> findByTagId(Integer tagId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        int totalCount = extensionMapper.countQuestionByTagId(tagId);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Question> questionList = extensionMapper.findQuestionByTagId(tagId, offset, size);
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    @Override
    public Tag findTag(Integer tagId) {
        return tagMapper.selectByPrimaryKey(tagId);
    }

    @Override
    public PaginationDTO<QuestionDTO> listEmptyComment(Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCommentCountEqualTo(0);
        int totalCount = (int) questionMapper.countByExample(questionExample);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        questionExample.setOrderByClause("gmt_modified desc");
        List<Question> questionList = questionMapper.
                selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    @Override
    public PaginationDTO<QuestionDTO> listRecentlyTrend(Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample questionExample = new QuestionExample();
        long start = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7;
        questionExample.createCriteria().andGmtCreateGreaterThan(start);
        int totalCount = (int) questionMapper.countByExample(questionExample);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        questionExample.setOrderByClause("comment_count desc, gmt_last_comment desc");
        List<Question> questionList = questionMapper.
                selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    @Override
    @Transactional
    public int collect(User user, Long questionId) {
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andUserIdEqualTo(user.getId())
                .andQuestionIdEqualTo(questionId);
        List<Collection> list = collectionMapper.selectByExample(example);
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (list.size() != 0) {
            collectionMapper.deleteByPrimaryKey(list.get(0).getId());
            question.setCollectionCount(question.getCollectionCount() - 1);
            questionMapper.updateByPrimaryKeySelective(question);
            return question.getCollectionCount();
        }
        Collection collection = new Collection();
        collection.setUserId(user.getId());
        collection.setQuestionId(questionId);
        collection.setGmtCreate(System.currentTimeMillis());
        collectionMapper.insertSelective(collection);
        question.setCollectionCount(question.getCollectionCount() + 1);
        questionMapper.updateByPrimaryKeySelective(question);
        return question.getCollectionCount();
    }
}
