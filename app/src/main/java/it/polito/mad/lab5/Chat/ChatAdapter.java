package it.polito.mad.lab5.Chat;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import it.polito.mad.lab5.R;

/**
 * Created by nicolapiano on 19/05/18.
 */

public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    // for logging ---------------------------------------
    String className = this.getClass().getSimpleName();
    String TAG = "--- " + className + " --- ";
    // ---------------------------------------------------

    public ChatAdapter(Context context, ArrayList<ChatMessage> msg) {
        super(context, 0, msg);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatMessage msg = getItem(position);
        Timestamp stamp = new Timestamp(msg.message.getTimestamp());
        Date date = new Date(stamp.getTime());

        Log.i(TAG, "message: " + msg.message.getBody());
        Log.i(TAG, "date: " + date.toString().substring(11,16));

        String str = msg.message.getBody() + "\n" + date.toString().substring(11,16);
        int lenMsg = msg.message.getBody().length();
        SpannableString ss1=  new SpannableString(str);
        ss1.setSpan(new RelativeSizeSpan(0.8f), 0,lenMsg, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, lenMsg, 0);// set color


        ss1.setSpan(new RelativeSizeSpan(0.5f), lenMsg, ss1.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.GRAY), lenMsg, ss1.length(), 0);// set color

        TextView text = null;

        // Check if an existing view is being reused, otherwise inflate the view

        if (msg.message.getFrom().equals(msg.getuID())) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_right, parent, false);
            text = (TextView) convertView.findViewById(R.id.msg_text_r);
            if (msg.getSeen()) {
                ImageView seen = (ImageView) convertView.findViewById(R.id.seen);
                seen.setVisibility(View.VISIBLE);
                seen.setColorFilter(Color.BLUE);
            }
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_left, parent, false);
            text = (TextView) convertView.findViewById(R.id.msg_text_l);

        }

        text.setText(ss1);


        /*
        if (msg.getuID().equals(msg.getMessageUser())) {
             //System.out.println(msg.getMessageText() + "RIGHT");
             text = (TextView) convertView.findViewById(R.id.message_text_r);
             //time = (TextView) convertView.findViewById(R.id.message_time_r);
             text.setText(msg.getMessageText());
             //time.setText(msg.getMessageTime());
             if (msg.getSeen()) time.setText(msg.getMessageTime() + " seen");
             else time.setText(msg.getMessageTime());
             //text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
             //time.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            System.out.println(msg.getMessageText() + "LEFT");
            text = (TextView) convertView.findViewById(R.id.message_text_l);
            time = (TextView) convertView.findViewById(R.id.message_time_l);
            text.setText(msg.getMessageText());
            time.setText(msg.getMessageTime());
            //text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            //time.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }*/

        return convertView;
    }
}