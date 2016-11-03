package activemq.xmg.com.activemq_mqtt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import activemq.xmg.com.activemq_mqtt.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.activity_home)
    LinearLayout activityHome;
    @Bind(R.id.btn_sub)
    Button btnSub;
    @Bind(R.id.btn_pub)
    Button btnPub;
    @Bind(R.id.btn_chat)
    Button btnChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setTitle("首页");

        /**
         * 订阅
         */
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubscriberActivity.startAction(HomeActivity.this);
            }
        });

        /**
         * 发布
         */
        btnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishActivity.startAction(HomeActivity.this);
            }
        });

        /**
         * 群聊
         */
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.startAction(HomeActivity.this);
            }
        });

    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
