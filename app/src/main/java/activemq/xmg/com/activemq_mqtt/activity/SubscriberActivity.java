package activemq.xmg.com.activemq_mqtt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
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
import activemq.xmg.com.activemq_mqtt.callback.SubcribeCallBackHandler;
import activemq.xmg.com.activemq_mqtt.event.MessageEvent;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2016/10/25 0025
 */

public class SubscriberActivity extends AppCompatActivity {

    @Bind(R.id.ed_topic)
    EditText edTopic;
    @Bind(R.id.btn_start_sub)
    Button btnStartSub;
    @Bind(R.id.lv_content)
    ListView lvContent;

    /**订阅的主题*/
    public String topic=null;
    public static final String SA="SubscriberActivity";
    private SubcriberAdapter subcriberAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        ButterKnife.bind(this);
        setTitle("订阅");
        
        initDate();

        btnStartSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**获取订阅的主题*/
                topic = edTopic.getText().toString().trim();
                if(topic==null||"".equals(topic)) {
                    Toast.makeText(SubscriberActivity.this,"请输入订阅的主题",Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] split = topic.split(",");
                /**一共有多少个主题*/
                int length = split.length;
                String [] topics=new String[length];/**订阅的主题*/
                int [] qos =new int [length];/**服务器的质量*/
                for(int i=0;i<length;i++){
                    topics[i]=split[i];
                    qos[i]=0;
                }
                /**获取client对象*/
                MqttAndroidClient client = MainActivity.getMqttAndroidClientInstace();
                if(client!=null){
                    try {
                        if(length>1) {
                            /**订阅多个主题,服务的质量默认为0*/
                            Log.d(SA,"topics="+ Arrays.toString(topics));
                            client.subscribe(topics, qos, null, new SubcribeCallBackHandler(SubscriberActivity.this));
                        }else{
                            Log.d(SA,"topic="+topic);
                            /**订阅一个主题，服务的质量默认为0*/
                          client.subscribe(topic,0,null,new SubcribeCallBackHandler(SubscriberActivity.this));
                        }
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e(SA,"MqttAndroidClient==null");
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

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SubscriberActivity.class);
        context.startActivity(intent);
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

}
