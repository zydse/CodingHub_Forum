package top.zydse.elasticsearch.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.zydse.elasticsearch.entity.Publish;

/**
 * CreateBy: zydse
 * ClassName: PublishRepository
 * Description:
 *
 * @Date: 2020/4/2
 */
public interface PublishRepository extends ElasticsearchRepository<Publish, Long> {
}
