package activemq.xmg.com.activemq_mqtt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import activemq.xmg.com.activemq_mqtt.R;
import activemq.xmg.com.activemq_mqtt.bean.Message;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2016/10/26 0026
 */

public class SubcriberAdapter extends BaseAdapter {

    private List<Message> listDate = null;

    private Context context = null;

    public SubcriberAdapter(List<Message> listDate, Context context) {
        this.listDate = listDate;
        this.context = context;
    }

    public List<Message> getListDate() {
        return listDate;
    }

    public void setListDate(List<Message> listDate) {
        this.listDate = listDate;
        notifyDataSetChanged();
    }

    public void addListDate(Message message) {
        this.listDate.add(message);
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return listDate.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.item_subscriber, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        /**
         * 操作数据
         */
        Message message = listDate.get(i);
        if(message!=null){
            if(message.isLeft){
                viewHolder.rlLeft.setVisibility(View.VISIBLE);
                viewHolder.rlRigth.setVisibility(View.GONE);
                viewHolder.tvLeft.setText(message.string);
            }else{
                viewHolder.rlLeft.setVisibility(View.GONE);
                viewHolder.rlRigth.setVisibility(View.VISIBLE);
                viewHolder.tvRigth.setText(message.string);
            }
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_left)
        TextView tvLeft;
        @Bind(R.id.rl_left)
        RelativeLayout rlLeft;
        @Bind(R.id.tv_rigth)
        TextView tvRigth;
        @Bind(R.id.rl_rigth)
        RelativeLayout rlRigth;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
