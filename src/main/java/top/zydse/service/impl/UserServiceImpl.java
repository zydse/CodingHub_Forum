package top.zydse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.AliResponseDTO;
import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.VerificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.UserMapper;
import top.zydse.mapper.UserRoleMapper;
import top.zydse.model.User;
import top.zydse.model.UserExample;
import top.zydse.model.UserRole;
import top.zydse.model.UserRoleExample;
import top.zydse.provider.AliMessageProvider;
import top.zydse.service.CommentService;
import top.zydse.service.NotificationService;
import top.zydse.service.QuestionService;
import top.zydse.service.UserService;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * CreateBy: zydse
 * ClassName: UserService
 * Description:
 *
 * @Date: 2020/3/10
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonExtensionMapper extensionMapper;
    @Autowired
    private AliMessageProvider aliMessageProvider;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;

    //保存一个github用户的资料
    @Transactional
    public void saveGithubUser(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().
                andAccountIdEqualTo(user.getAccountId());
        List<User> githubUsers = userMapper.selectByExample(userExample);
        userExample.clear();
        userExample.createCriteria()
                .andNameEqualTo(user.getName())
                .andAccountIdIsNull();
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 0) {
            throw new CustomizeException(CustomizeErrorCode.DUPLICATE_USERNAME);
        } else if (githubUsers.size() == 0) {
            //新增用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setSalt(user.getGmtCreate());
            Md5Hash password = new Md5Hash(user.getToken());
            Md5Hash hash = new Md5Hash(password.toString(), Long.toString(user.getSalt()), 1);
            user.setPassword(hash.toString());
            extensionMapper.saveUser(user);
            UserRole record = new UserRole();
            record.setUserId(user.getId());
            record.setGmtCreate(System.currentTimeMillis());
            //注册后默认是初级用户
            record.setRoleId(3);
            userRoleMapper.insert(record);
        } else {
            //更新用户资料
            user.setSalt(githubUsers.get(0).getSalt());
            user.setId(githubUsers.get(0).getId());
            user.setCredit(githubUsers.get(0).getCredit());
            user.setGmtModified(System.currentTimeMillis());
            Md5Hash password = new Md5Hash(user.getToken());
            Md5Hash hash = new Md5Hash(password.toString(), Long.toString(user.getSalt()), 1);
            user.setPassword(hash.toString());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    //通过用户名查询用户是否存在
    public User selectByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList == null || userList.size() != 1)
            return null;
        return userList.get(0);
    }

    //新增一个用户
    @Transactional
    public ResultDTO register(RegisterDTO registerDTO, VerificationDTO verificationCode) {
        if (verificationCode == null || !registerDTO.getPhoneNumber().equals(verificationCode.getPhoneNumber())) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_ERROR);
        }
        if(selectByPhone(registerDTO.getPhoneNumber()) != null){
            return ResultDTO.errorOf(CustomizeErrorCode.DUPLICATE_PHONE_NUMBER);
        }
        long gap = (registerDTO.getGmtCreate() - verificationCode.getGmtCreate()) / 1000 / 60;
        if (verificationCode.getCode().equals(registerDTO.getVerifyCode()) && gap < 15L) {
            User user = new User();
            user.setName(registerDTO.getUsername());
            user.setSalt(registerDTO.getGmtCreate());
            Md5Hash hash = new Md5Hash(registerDTO.getPassword(), user.getSalt().toString(), 1);
            user.setPassword(hash.toString());
            user.setPhoneNumber(registerDTO.getPhoneNumber());
            user.setGmtCreate(registerDTO.getGmtCreate());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl("/images/avatar" + registerDTO.getGmtCreate() % 10 + ".png");
            user.setToken(UUID.randomUUID().toString());
            user.setCredit(0);
            extensionMapper.saveUser(user);
            UserRole record = new UserRole();
            record.setUserId(user.getId());
            record.setGmtCreate(System.currentTimeMillis());
            //注册后默认是初级用户
            record.setRoleId(3);
            userRoleMapper.insert(record);
            return ResultDTO.successOf(user);
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_INACTIVE);
        }
    }

    @Override
    public User findByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList.size() != 1)
            return null;
        User user = userList.get(0);
        user.setToken(UUID.randomUUID().toString());
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    public ResultDTO<VerificationDTO> sendSms(String phoneNumber, long timestamp) {
        StringBuilder code = new StringBuilder();
        UUID uuid = UUID.randomUUID();
        long bits = uuid.getLeastSignificantBits();
        Random rand = new Random(bits);
        for (int i = 0; i < 6; i++)
            code.append(rand.nextInt(10));
        AliResponseDTO aliResponseDTO = aliMessageProvider.sendSms(phoneNumber, code.toString());
        log.info(aliResponseDTO.toString());
        if ("OK".equals(aliResponseDTO.getCode())) {
            VerificationDTO record = new VerificationDTO();
            log.info("code put into session : {}", code.toString());
            record.setCode(code.toString());
            record.setPhoneNumber(phoneNumber);
            record.setGmtCreate(timestamp);
            return ResultDTO.successOf(record);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.ALI_SERVER_ERROR);
    }

    @Override
    public User selectByPhone(String phoneNumber) {
        UserExample example = new UserExample();
        example.createCriteria().andPhoneNumberEqualTo(phoneNumber);
        List<User> userList = userMapper.selectByExample(example);
        return userList.size() == 0 ? null : userList.get(0);
    }

    @Override
    public List<String> getPermCode(Long userId) {
        return extensionMapper.getPermCodeByUser(userId);
    }

    @Override
    @Transactional
    public void resetPassword(String password, String phone) {
        UserExample example = new UserExample();
        example.createCriteria().andPhoneNumberEqualTo(phone);
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size() != 1)
            throw new CustomizeException(CustomizeErrorCode.ACCOUNT_ERROR);
        User user = userList.get(0);
        Md5Hash hash = new Md5Hash(password, user.getSalt().toString(), 1);
        user.setPassword(hash.toString());
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional
    @Override
    public int deleteById(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null)
            throw new CustomizeException(CustomizeErrorCode.SYSTEM_ERROR);
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        //删除用户角色
        userRoleMapper.deleteByExample(userRoleExample);
        //删除所有一级、二级回复
        commentService.deleteCommentByUserId(userId);
        //删除点赞历史
        commentService.deleteThumbHistoryByUserId(userId);
        //删除收藏历史
        questionService.deleteCollectionByUserId(userId);
        //删除通知人和接收人为当前用户的所有通知
        notificationService.deleteNotificationByUserId(userId);
        //删除用户所有的发帖历史
        questionService.deleteQuestionByUserId(userId);
        //删除用户的浏览历史
        questionService.deleteViewHistoryByUserId(userId);
        //删除用户
        userMapper.deleteByPrimaryKey(userId);
        return 1;
    }
}
