package activemq.xmg.com.activemq_mqtt.callback;

import android.content.Context;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import activemq.xmg.com.activemq_mqtt.activity.PublishActivity;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2016/10/26 0026
 */

public class PublishCallBackHandler  implements IMqttActionListener{

    private final Context context;

    public PublishCallBackHandler(Context context) {
        this.context=context;
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {

    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

    }
}
