package com.ctsi.ssdc.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.ctsi.ssdc.criteria.IntegerCriteria;
import com.ctsi.ssdc.es.service.DocumentService;
import com.ctsi.ssdc.es.util.PingUtil;
import com.ctsi.ssdc.log.domain.CscpComponentCallLog;
import com.ctsi.ssdc.log.domain.CscpComponentCallLogExample;
import com.ctsi.ssdc.log.repository.CscpComponentCallLogRepository;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ES文档操作实现类
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private CscpComponentCallLogRepository cscpComponentCallLogRepository;


    /** 集群地址，如果有多个用“,”隔开 */
    @Value("${elasticsearch.address}")
    private String address;

    /**
     * 文档是否存在
     *
     * @param id
     * @return
     */
    @Override
    public boolean existsDocument(String id) {
        boolean result = false;
        try {
            // 获取请求对象
            GetRequest getRequest = new GetRequest("hx-component-call", "doc",id);
            // 是否获取源码内容
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            // 执行请求，验证文档是否存在
            boolean isExist = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
            log.info("文档是否存在：{}", isExist);
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将结果返回
            result = isExist;
        } catch (IOException e) {
            log.error("", e);
        }
        return result;
    }

    /**
     * 单个添加文档，通过循环实现效果等同于batchAddDoc
     * 但不推荐使用（跟数据库及ES链接过于频繁）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDocument() {
        try {
            boolean serverReachable = pingConnect();
            if (!serverReachable){
                return;
            }
            // 创建索引请求对象
            IndexRequest indexRequest = new IndexRequest("hx-component-call");
            // 创建员工信息
            List<CscpComponentCallLog> cscpComponentCallLogs = cscpComponentCallLogRepository.selectByExample(new CscpComponentCallLogExample());
            for (CscpComponentCallLog callLog : cscpComponentCallLogs) {
                // 将对象转换为 byte 数组
                byte[] json = JSON.toJSONBytes(callLog);
                // 设置文档内容
                indexRequest.source(json, XContentType.JSON).id("" + callLog.getId());
                // 执行增加文档
                IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                log.info("创建状态：{}", response.status());
                cscpComponentCallLogRepository.updateById(callLog.getId());
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 批量添加文档
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddDoc() {
        try {
            boolean serverReachable = pingConnect();
            if (!serverReachable){
                log.info("ES server is unreachable now,please try again !");
                return;
            }
            //1.创建批量导入数据
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout("10s");
            CscpComponentCallLogExample callLogExample = new CscpComponentCallLogExample();
            IntegerCriteria criteria = new IntegerCriteria();
            criteria.setEquals(0);
            callLogExample.setUploadFlag(criteria);
            callLogExample.buildCriteria();
            // 查询上传标志为未上传（0）的 数据信息
            List<CscpComponentCallLog> cscpComponentCallLogs = cscpComponentCallLogRepository.selectByExample(callLogExample);
            List<Long> idList = new ArrayList<>();
            if (cscpComponentCallLogs.size() == 0) {
                log.info("component call log is empty !");
                return;
            }
            for (CscpComponentCallLog callLog : cscpComponentCallLogs) {
                bulkRequest.add(
                        new IndexRequest("hx-component-call").id("" + callLog.getId()).type("doc")
                                .source(JSON.toJSONBytes(callLog), XContentType.JSON)
                );
                idList.add(callLog.getId());
            }
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("batch insert result: {}", bulkResponse.status());
            // 批量修改数据
            cscpComponentCallLogRepository.updateByIds(idList);
            log.info("batch update component call log uploadFlag");
        } catch (IOException e) {
            log.error("failed to batch insert es document caused by: ", e);
        }

    }

    /**
     * 获取文档信息
     *
     * @param id
     */
    @Override
    public CscpComponentCallLog getDocument(String id) {
        CscpComponentCallLog cscpComponentCallLog = null;
        try {
            // 获取请求对象
            GetRequest getRequest = new GetRequest("hx-component-call", "doc" ,id);
            // 获取文档信息
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            // 将 JSON 转换成对象
            if (getResponse.isExists()) {
                cscpComponentCallLog = JSON.parseObject(getResponse.getSourceAsBytes(), CscpComponentCallLog.class);
                log.info("组件调用信息：{}", cscpComponentCallLog);
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return cscpComponentCallLog;
    }


    /**
     * 删除文档信息
     */
    @Override
    public void deleteDocument(String id) {
        try {
            boolean serverReachable = pingConnect();
            if (!serverReachable){
                log.info("ES server is unreachable now,please try again ");
                return;
            }
            // 创建删除请求对象
            DeleteRequest deleteRequest = new DeleteRequest("hx-component-call","doc",id);
            // 执行删除文档
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("删除文档状态：{}", response.status());
        } catch (IOException e) {
            log.error("", e);
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
