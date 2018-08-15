package com.juankyapps.wschat;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView nameDate;
        TextView message;
    }

    public MessageAdapter(ArrayList<Message> data, Context context) {
        super(context, R.layout.message, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message msg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.message, parent, false);
            viewHolder.nameDate = (TextView) convertView.findViewById(R.id.name_date);
            viewHolder.message = (TextView) convertView.findViewById(R.id.msgcontent);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/
        lastPosition = position;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String showDate = sdf.format(msg.timestamp);
        viewHolder.nameDate.setText(Html.fromHtml("<b>"+msg.username+"</b> " + showDate));
        viewHolder.message.setText(msg.data);
        // Return the completed view to render on screen
        return convertView;
    }

}
