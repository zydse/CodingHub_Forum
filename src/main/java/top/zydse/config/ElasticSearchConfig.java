package top.zydse.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;

/**
 * CreateBy: zydse
 * ClassName: ElasticSearchConfig
 * Description:
 *
 * @Date: 2020/4/2
 */
@Configuration
@Slf4j
public class ElasticSearchConfig {
//    @Bean(name = "RestHighLevelClient")
//    public RestHighLevelClient restHighLevelClient(){
//        HttpHost httpHostArray = new HttpHost("zydse.top", 9200, "http");
//        //创建RestHighLevelClient客户端
//        return new RestHighLevelClient(RestClient.builder(httpHostArray));
//    }
//
//    //项目主要使用RestHighLevelClient，对于低级的客户端暂时不用
//    @Bean
//    public RestClient restClient(){
//        HttpHost httpHostArray = new HttpHost("zydse.top", 9200, "http");
//        return RestClient.builder(httpHostArray).build();
//    }

    @Bean(name = "transportClient")
    public TransportClient transportClient() {
        TransportClient transportClient = null;
        try {
            // 配置信息
            Settings esSetting = Settings.builder()
                    .put("cluster.name", "elasticsearch") //集群名字
                    .build();
            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("zydse.top"), 9300);
            transportClient.addTransportAddresses(transportAddress);
        } catch (Exception e) {
            log.error("elasticsearch TransportClient create error!!", e);
        }
        return transportClient;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client){
        return new ElasticsearchTemplate(client);
    }
}