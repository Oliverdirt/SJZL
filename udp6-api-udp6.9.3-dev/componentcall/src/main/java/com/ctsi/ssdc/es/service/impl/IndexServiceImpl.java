package com.ctsi.ssdc.es.service.impl;

import com.ctsi.ssdc.es.service.IndexService;
import com.ctsi.ssdc.es.util.PingUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * ES索引操作实现类
 */
@Service
public class IndexServiceImpl implements IndexService {

    private static final Logger log = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /** 集群地址，如果有多个用“,”隔开 */
    @Value("${elasticsearch.address}")
    private String address;




    /**
     * 索引是否存在
     * @param indexName
     * @return
     */
    @Override
    public boolean existsIndex(String indexName) {
        try{
            GetIndexRequest request = new GetIndexRequest(indexName);
            boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
            return exists;
        }catch (Exception e){
            log.error("未知错误:{}", e);
        }
        return false;
    }

    /**
     * 创建索引,当前为固定索引信息,存储组件调用日志；
     */
    @Override
    public void createIndex() {
        try {
            boolean serverReachable = pingConnect();
            if (!serverReachable){
                log.info("ES server is unreachable now,please try again ");
                return;
            }
            boolean b = existsIndex("hx-component-call");
            if (b){
                log.info("索引：hx-component-call---已存在");
                return;
            }
            // 创建 Mapping
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                        .field("dynamic", true)
                        .startObject("properties")
                            .startObject("id")
                                .field("type", "text")
                                .startObject("fields")
                                    .startObject("keyword")
                                        .field("type", "keyword")
                                    .endObject()
                                .endObject()
                            .endObject()
                            .startObject("company")
                                .field("type", "text")
                                .startObject("fields")
                                    .startObject("keyword")
                                        .field("type", "keyword")
                                    .endObject()
                                .endObject()
                             .endObject()
                             .startObject("project_name")
                                .field("type", "text")
                                .startObject("fields")
                                    .startObject("keyword")
                                        .field("type", "keyword")
                                    .endObject()
                                .endObject()
                            .endObject()
                            .startObject("component_name")
                                .field("type", "text")
                                .startObject("fields")
                                    .startObject("keyword")
                                        .field("type", "keyword")
                                    .endObject()
                                .endObject()
                            .endObject()
                            .startObject("component_method_name")
                                .field("type", "text")
                                .startObject("fields")
                                    .startObject("keyword")
                                        .field("type", "keyword")
                                    .endObject()
                                .endObject()
                            .endObject()
                            .startObject("call_time")
                                .field("type", "date")
                                .field("format", "yyyy-MM-dd")
                            .endObject()
                        .endObject()
                    .endObject();
            // 创建索引配置信息，配置
            Settings settings = Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 0)
                    .build();
            // 新建创建索引请求对象，然后设置索引类型（ES 7.0 将不存在索引类型）和 mapping 与 index 配置
            CreateIndexRequest request = new CreateIndexRequest("hx-component-call", settings);
            request.mapping("doc", mapping);
            // RestHighLevelClient 执行创建索引
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            // 判断是否创建成功
            boolean isCreated = createIndexResponse.isAcknowledged();
            log.info("create es index result：{}", isCreated);
        } catch (IOException e) {
            log.error("failed to create es index caused by :", e);
        }
    }


    /**
     * 删除索引
     * @param indexName
     */
    @Override
    public void deleteIndex(String indexName) {
        try {
            boolean serverReachable = pingConnect();
            if (!serverReachable){
                log.info("ES server is unreachable now,please try again ");
                return;
            }
            // 新建删除索引请求对象
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            // 执行删除索引
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            // 判断是否删除成功
            boolean siDeleted = acknowledgedResponse.isAcknowledged();
            log.info("delete es index result：{}", siDeleted);
        } catch (IOException e) {
            log.error("failed to delete es index caused by : ", e);
        }
    }

    /**
     * ES服务是否可用
     * @return
     */
    public boolean pingConnect(){
        boolean flag = true;
        String[] hostList = address.split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            Integer port = Integer.valueOf(addr.split(":")[1]);
            boolean connect = PingUtil.connect(host, port, 10 * 1000);
            if (!connect) {
                flag = false;
            }
        }
        return  flag;
    }
}
