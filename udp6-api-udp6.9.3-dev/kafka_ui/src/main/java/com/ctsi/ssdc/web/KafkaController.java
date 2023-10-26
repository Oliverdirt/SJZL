package com.ctsi.ssdc.web;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.domain.KafkaSource;
import com.ctsi.ssdc.dto.ResponseDto;
import com.ctsi.ssdc.service.KafkaService;
import com.ctsi.ssdc.util.KafkaUtil;
import io.swagger.annotations.Api;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-05 14:06
 **/
@Api(tags = "kafka操作接口")
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired
    KafkaService kafkaService;

    @PostMapping("/getTopics")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getTopics")
    @Log
    public ResponseDto getTopics(Long sourceId) {
        String brokers = kafkaService.getBroker(sourceId);
        return KafkaUtil.listTopicsWithOptions(brokers, null);
    }

    @PostMapping("/getIp")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getIpAndPort")
    @Log
    public String getIpAndPort(HttpServletRequest request) {
        //获取浏览器访问地址中的ip和端口，防止容器运行时候产生问题
        return request.getServerName() + ":" + request.getServerPort();
    }

    @GetMapping("/getSource")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getAllSource")
    @Log
    public List<KafkaSource> getAllSource() {
        return kafkaService.getAllSource();
    }

    @PostMapping("/getSource")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getAllSource2")
    @Log
    public List<KafkaSource> getAllSource2() {
        return kafkaService.getAllSource();
    }

    @GetMapping("/getAllSourceAuth")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getSourceAuth")
    @Log
    public List<KafkaSource> getSourceAuth() {
        return kafkaService.getAllSourceAuth();
    }

    @PostMapping("/getAllSourceAuth")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getSourceAuth2")
    @Log
    public List<KafkaSource> getSourceAuth2() {
        return kafkaService.getAllSourceAuth();
    }

    @PostMapping("/deleteSource/{id}")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "deleteSource")
    @Log
    public String deleteSource(@PathVariable Long id) {
        kafkaService.deleteSource(id);
        return "success";
    }

    @PostMapping("/add")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "addSource")
    @Log
    public String addSource(KafkaSource source) {
        List<KafkaSource> sources = kafkaService.getSourceByname(source.getName());
        if (sources.size()>0){
            return "该集群名称已存在！";
        }
        kafkaService.add(source);
        return "success";
    }

    @PostMapping("/getBroker")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getBroker")
    @Log
    public String getBroker(Long sourceId) {
        String broker = kafkaService.getBroker(sourceId);
        return broker;
    }

    @PostMapping("/createTopic")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "createTopic")
    @Log
    public String createTopic(Long sourceId, String name,
                              @RequestParam(defaultValue = "1") Integer partition,
                              @RequestParam(defaultValue = "1") Integer replica) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        KafkaUtil.createTopic(broker, name, partition, replica);
        return "success";

    }

    @PostMapping("/deleteTopic")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "deleteTopic")
    @Log
    public boolean deleteTopic(Long sourceId, String topic) {
        String broker = kafkaService.getBroker(sourceId);
        KafkaUtil.deleteTopic(broker, topic);
        return true;

    }

    @PostMapping("/searchTopic")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "searchTopic")
    @Log
    public ResponseDto searchTopic(Long sourceId, String topic) {
        String broker = kafkaService.getBroker(sourceId);
        ResponseDto responseDto = KafkaUtil.listTopicsWithOptions(broker, topic);
        return responseDto;

    }

    @PostMapping("/getTopicDetail")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getTopicDetail")
    @Log
    public JSONObject getTopicDetail(Long sourceId, String topic) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        return KafkaUtil.getTopicDetail(broker, topic);
    }

    @PostMapping("/produce")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "produce")
    @Log
    public String produce(Long sourceId, String topic, String message, Boolean batch) throws ExecutionException, InterruptedException {
        String broker = kafkaService.getBroker(sourceId);
        Producer<String, String> producer = KafkaUtil.getProducer(broker);
        if (batch) {
            String[] messages = message.split("\\\\n");
            for (String ms : messages) {
                Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, ms));
                send.get();
            }
        } else {
            Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, message));
            send.get();
        }
        producer.close();
        return "success";
    }

    @PostMapping("/cluster/info")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getClusterInfo")
    @Log
    public ResponseDto getClusterInfo(Long sourceId) {
        String broker = kafkaService.getBroker(sourceId);
        return KafkaUtil.clusterInfo(broker);
    }

    @PostMapping("/group/all")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getAllGroups")
    @Log
    public ResponseDto getAllGroups(Long sourceId) {
        String broker = kafkaService.getBroker(sourceId);
        ResponseDto allGroups = KafkaUtil.getAllGroups(broker, null);
        return allGroups;
    }

    @PostMapping("/group/search")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getAllGroups")
    @Log
    public ResponseDto getAllGroups(Long sourceId, String keyword) {
        String broker = kafkaService.getBroker(sourceId);
        ResponseDto allGroups = KafkaUtil.getAllGroups(broker, keyword);
        return allGroups;
    }

    @PostMapping("/group/detail")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getGroupDetail")
    @Log
    public ResponseDto getGroupDetail(Long sourceId, String group) {
        String broker = kafkaService.getBroker(sourceId);
        ResponseDto groupInfo = KafkaUtil.getGroupInfo(broker, group);
        return groupInfo;
    }

    @PostMapping("/group/delete")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getTableFields")
    @Log
    public ResponseDto deleteGroup(Long sourceId, String group) {
        String broker = kafkaService.getBroker(sourceId);
        return KafkaUtil.deleteGroup(broker, group);
    }

    @PostMapping("/auth")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "auth")
    @Log
    public void auth(String param) throws Exception {
        kafkaService.auth(param);
    }

    @PostMapping("/getGroupsByTopic")
    @ComponentCallAnno(component = HxComponentConstant.MQ,method = "getGroupByTopic")
    @Log
    public ResponseDto getGroupByTopic(Long sourceId, String topic) {
        String broker = kafkaService.getBroker(sourceId);
        return KafkaUtil.getGroupByTopic(broker, topic);
    }
}
