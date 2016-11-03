package activemq.xmg.com.activemq_mqtt.callback;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.greenrobot.eventbus.EventBus;

import activemq.xmg.com.activemq_mqtt.event.MessageEvent;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2016/10/26 0026
 */

public class SubcribeCallBackHandler implements IMqttActionListener {

    private final Context context;
    /**
     * 构造器
     * @param context
     */
    public SubcribeCallBackHandler(Context context) {
        this.context=context;
    }

    /**
     * 订阅成功
     * @param iMqttToken
     */
    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Toast.makeText(context,"订阅成功",Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new MessageEvent("订阅成功"));
    }

    /**
     * 订阅失败
     * @param iMqttToken
     * @param throwable
     */
    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Toast.makeText(context,"订阅失败",Toast.LENGTH_SHORT).show();
    }
}
