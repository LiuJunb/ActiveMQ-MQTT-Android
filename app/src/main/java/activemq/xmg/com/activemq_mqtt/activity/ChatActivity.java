package activemq.xmg.com.activemq_mqtt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activemq.xmg.com.activemq_mqtt.R;
import activemq.xmg.com.activemq_mqtt.adapter.SubcriberAdapter;
import activemq.xmg.com.activemq_mqtt.bean.Message;
import activemq.xmg.com.activemq_mqtt.callback.PublishCallBackHandler;
import activemq.xmg.com.activemq_mqtt.callback.SubcribeCallBackHandler;
import activemq.xmg.com.activemq_mqtt.event.MessageEvent;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    /**订阅*/
    @Bind(R.id.ed_topic)
    EditText edTopic;
    @Bind(R.id.btn_start_sub)
    Button btnStartSub;
    /**发布*/
    @Bind(R.id.ed_pub_topic)
    EditText edPubTopic;
    @Bind(R.id.ed_pub_message)
    EditText edPubMessage;
    @Bind(R.id.btn_start_pub)
    Button btnStartPub;
    /**记录*/
    @Bind(R.id.lv_content)
    ListView lvContent;

    public static final String PA="PublishActivity";
    private SubcriberAdapter subcriberAdapter;
    public String topic;
    private String pubTopic;
    private String pubMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setTitle("模拟服务器发布主题");
        initDate();

        /**1.开始订阅*/
        btnStartSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //获取订阅的主题
                topic = edTopic.getText().toString().trim();
                if(topic==null||"".equals(topic)) {
                    Toast.makeText(ChatActivity.this,"请输入订阅的主题",Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] split = topic.split(",");
                /**一共有多少个主题*/
                int length = split.length;
                String [] topics=new String[length];//订阅的主题
                int [] qos =new int [length];// 服务器的质量
                for(int i=0;i<length;i++){
                    topics[i]=split[i];
                    qos[i]=0;
                }

                MqttAndroidClient client = MainActivity.getMqttAndroidClientInstace();
                if(client!=null){
                    try {
                        if(length>1) {
                            /**订阅多个主题,服务的质量默认为0*/
                            Log.d(PA,"topics="+ Arrays.toString(topics));
                            client.subscribe(topics, qos, null, new SubcribeCallBackHandler(ChatActivity.this));
                        }else{
                            Log.d(PA,"topic="+topic);
                            /**订阅一个主题，服务的质量默认为0*/
                            client.subscribe(topic,0,null,new SubcribeCallBackHandler(ChatActivity.this));
                        }
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e(PA,"MqttAndroidClient==null");
                }

            }
        });

        /**2.开始发布*/
        btnStartPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**获取发布的主题*/
                pubTopic = edPubTopic.getText().toString().trim();
                /**获取发布的消息*/
                pubMessage = edPubMessage.getText().toString().trim();
                /**消息的服务质量*/
                int qos=0;
                /**消息是否保持*/
                boolean retain=false;
                /**要发布的消息内容*/
                byte[] message=pubMessage.getBytes();
                if(pubTopic!=null&&!"".equals(pubTopic)){
                    /**获取client对象*/
                    MqttAndroidClient client = MainActivity.getMqttAndroidClientInstace();
                    if(client!=null){
                        try {
                            /**发布一个主题:如果主题名一样不会新建一个主题，会复用*/
                            client.publish(pubTopic,message,qos,retain,null,new PublishCallBackHandler(ChatActivity.this));
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.e(PA,"MqttAndroidClient==null");
                    }
                }else{
                    Toast.makeText(ChatActivity.this,"发布的主题不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        List<Message> list=new ArrayList<Message>();
        if(subcriberAdapter==null) {
            subcriberAdapter = new SubcriberAdapter(list, this);
            lvContent.setAdapter(subcriberAdapter);
        }else{
            subcriberAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 运行在主线程
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        String string = event.getString();
        /**接收到服务器推送的信息，显示在右边*/
        if("".equals(string)){
            String topic = event.getTopic();
            MqttMessage mqttMessage = event.getMqttMessage();
            String s = new String(mqttMessage.getPayload());
            topic=topic+" : "+s;
            subcriberAdapter.addListDate(new Message(topic,false));
            /**接收到订阅成功的通知,订阅成功，显示在左边*/
        }else{
            subcriberAdapter.addListDate(new Message("Me : "+string,true));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }
}
