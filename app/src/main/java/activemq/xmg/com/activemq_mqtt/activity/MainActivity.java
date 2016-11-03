package activemq.xmg.com.activemq_mqtt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import activemq.xmg.com.activemq_mqtt.R;
import activemq.xmg.com.activemq_mqtt.callback.ConnectCallBackHandler;
import activemq.xmg.com.activemq_mqtt.callback.MqttCallbackHandler;
import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.port;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.ed_client_id)
    EditText edClientId;
    @Bind(R.id.ed_server)
    EditText edServer;
    @Bind(R.id.ed_port)
    EditText edPort;
    @Bind(R.id.btn_connect)
    Button btnConnect;
    private String clientID;
    private String serverIP;
    private String port;
    private static MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("即时通讯");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity","MainActivity/btnConnect");
                //获取用户id    
                 clientID = edClientId.getText().toString().trim();
                //获取ip地址
                serverIP = edServer.getText().toString().trim();
                //获取端口号
                port = edPort.getText().toString().trim();
                startConnect(clientID,serverIP,port);
            }
        });
    }

    private void startConnect(String clientID, String serverIP, String port) {
        //服务器地址
        String  uri ="tcp://";
        uri=uri+serverIP+":"+port;
        Log.d("MainActivity",uri+"  "+clientID);
        /**
         * 连接的选项
         */
        MqttConnectOptions conOpt = new MqttConnectOptions();
        /**设计连接超时时间*/
        conOpt.setConnectionTimeout(3000);
        /**设计心跳间隔时间300秒*/
        conOpt.setKeepAliveInterval(300);
        /**
         * 创建连接对象
         */
         client = new MqttAndroidClient(this,uri, clientID);
        /**
         * 连接后设计一个回调
         */
        client.setCallback(new MqttCallbackHandler(this, clientID));
        /**
         * 开始连接服务器，参数：ConnectionOptions,  IMqttActionListener
         */
        try {
            client.connect(conOpt, null, new ConnectCallBackHandler(this));
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取MqttAndroidClient实例
     * @return
     */
    public static MqttAndroidClient getMqttAndroidClientInstace(){
        if(client!=null)
            return  client;
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(client!=null)
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
    }
}
