import com.aliyun.ons20190214.Client;
import com.aliyun.ons20190214.models.*;
import com.aliyun.teaopenapi.models.Config;

import java.util.List;

/**
 * @author Cocowwy
 * @create 2021-07-07-22:13
 */
public class MQUtil {

    /**
     * 消费组拷贝
     * @param client
     * @param oldInstanceId
     * @param newInstanceId
     * @throws Exception
     * @return 新实例下的Group
     */
    public static List<OnsGroupListResponseBody.OnsGroupListResponseBodyDataSubscribeInfoDo> copyGroupToNewInstance(Client client, String oldInstanceId, String newInstanceId) throws Exception {
        List<OnsGroupListResponseBody.OnsGroupListResponseBodyDataSubscribeInfoDo> consumeForInstance = getConsumeForInstance(client, oldInstanceId);
        consumeForInstance.forEach(old -> {
            OnsGroupCreateRequest onsGroupCreateRequest = new OnsGroupCreateRequest();
            onsGroupCreateRequest.setGroupId(old.getGroupId());
            onsGroupCreateRequest.setRemark(old.getRemark());
            onsGroupCreateRequest.setInstanceId(newInstanceId);
            onsGroupCreateRequest.setGroupType(old.getGroupType());
            try {
                Thread.sleep(3000);
                client.onsGroupCreate(onsGroupCreateRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return getConsumeForInstance(client, newInstanceId);
    }

    /**
     * 获取实例下消费组集合
     * @param client
     * @param instanceId
     * @throws Exception
     * @return
     */
    public static List<OnsGroupListResponseBody.OnsGroupListResponseBodyDataSubscribeInfoDo>
    getConsumeForInstance(Client client, String instanceId) throws Exception {
        OnsGroupListRequest onsGroupListRequest = new OnsGroupListRequest();
        onsGroupListRequest.setInstanceId(instanceId);
        return client.onsGroupList(onsGroupListRequest).getBody().getData().getSubscribeInfoDo();
    }

    /**
     * 实例下的Topic拷贝
     * 测试通过，需要加时间限制，不然会限流
     * @param client
     * @param oldInstanceId
     * @param newInstanceId
     * @return topics
     * @throws Exception
     */
    public static List<OnsTopicListResponseBody.OnsTopicListResponseBodyDataPublishInfoDo>
    copyTopicsToNewInstance(Client client, String oldInstanceId, String newInstanceId) throws Exception {
        // 获取源Topic集合
        List<OnsTopicListResponseBody.OnsTopicListResponseBodyDataPublishInfoDo> oldTopics =
                getTopicsForInstance(client, oldInstanceId);

        // 创建Topic
        oldTopics.forEach(old -> {
            try {
                // Request was denied due to user flow control.
                Thread.sleep(3000);
                createTopic(client, newInstanceId, old.getTopic(),
                        old.getMessageType(), old.getRemark());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //  获取新实例Topic集合
        return getTopicsForInstance(client, newInstanceId);
    }

    /**
     * 在指定Instance下创建Topic
     * @param client
     * @param instanceId
     * @param topic
     * @param type
     * @param remark
     * @throws Exception
     */
    public static String createTopic(Client client, String instanceId, String topic,
                                     Integer type, String remark) throws Exception {

        OnsTopicCreateRequest onsTopicCreateRequest = new OnsTopicCreateRequest();
        onsTopicCreateRequest.setInstanceId(instanceId);
        onsTopicCreateRequest.setMessageType(type);
        onsTopicCreateRequest.setTopic(topic);
        onsTopicCreateRequest.setRemark(remark);
        OnsTopicCreateResponse resp = client.onsTopicCreate(onsTopicCreateRequest);
        return resp.getBody().toString();
    }

    /**
     * 获取指定实例的Topic集合
     * @param client
     * @param instanceId
     * @return
     * @throws Exception
     */
    public static List<OnsTopicListResponseBody.OnsTopicListResponseBodyDataPublishInfoDo> getTopicsForInstance(Client client, String instanceId) throws Exception {
        OnsTopicListRequest onsTopicListRequest = new OnsTopicListRequest();
        onsTopicListRequest.setInstanceId(instanceId);
        return client.onsTopicList(onsTopicListRequest).getBody().getData().getPublishInfoDo();
    }

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new com.aliyun.ons20190214.Client(config);
    }
}
