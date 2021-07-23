import com.aliyun.ons20190214.Client;

/**
 * Util For RocketMQ!
 * 【使用方式，运行main函数，执行MQUtil内的copy方法即可对
 *   实例下的topic和group进行拷贝，需要传入新旧的实例ID即可】
 * @author Cocowwy
 * @create 2021-07-07-23:32
 */
public class DoMain {
    // key secret
    private final static String KEY = "###################";
    private final static String SECRET = "####################";
    // region
    private final static String REGION_ZHANGJIAKOU = "#####################";

    // instanecId
    private final static String MQ_TEST_INSTANCE_ID = "###################";

    private final static String MQ_UAT_INSTANCE_ID = "#####################";

    private final static String MQ_PRO_INSTANCE_ID = "#####################";

    public static void main(String[] args) throws Exception {
        Client client = MQUtil.createClient(KEY, SECRET, REGION_ZHANGJIAKOU);
        MQUtil.copyGroupToNewInstance(MQUtil.createClient(KEY, SECRET, REGION_ZHANGJIAKOU), MQ_UAT_INSTANCE_ID, MQ_PRO_INSTANCE_ID);
    }
}
