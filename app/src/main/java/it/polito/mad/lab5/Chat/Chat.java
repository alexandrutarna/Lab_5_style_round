package it.polito.mad.lab5.Chat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;


//import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.polito.mad.lab5.ChatList.ChatList;
import it.polito.mad.lab5.R;
import it.polito.mad.lab5.SearchBook.Book;
import it.polito.mad.lab5.SearchBook.BookAdapter;
import it.polito.mad.lab5.beans.Message;
import it.polito.mad.lab5.fcm.MessagingService;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    // for logging ---------------------------------------
    String className = this.getClass().getSimpleName();
    String TAG = "--- " + className + " --- ";
    // ---------------------------------------------------

    private String chatID;

    private DatabaseReference mDatabase;
    private String senderUid;
    private String receiverUid;
    private String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // ===== received from Notification ==================
        Bundle myBundle = getIntent().getExtras();
        String someData = myBundle.getString("someData");
        Log.i(TAG, "data from FCM: "+ someData);
        // ==================================================

        Intent intent = getIntent();
        receiverUid = intent.getStringExtra("otherUid");
        Log.i(TAG, "receiverUid: " + receiverUid);

        final SharedPreferences sharedPref = this.getSharedPreferences("shared_id", Context.MODE_PRIVATE);
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        senderUid = sharedPref.getString("uID", null);
        Log.i(TAG, "Sender of the messages is: " + senderUid);
        Log.i(TAG, "senderName: " + senderName);
        Log.i(TAG, "receiverUid of the messages is: " + receiverUid);

        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
        b.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*ScrollView sc = (ScrollView) findViewById(R.id.chat_sc);
                sc.setVerticalScrollbarPosition(sc.getBottom());*/

                // initialize the name of the sender
                senderName = dataSnapshot.child("users").child(senderUid).child("name").getValue(String.class);

                String uID = sharedPref.getString("uID", null);
                //String chatID = sharedPref.getString("chatID", null);


                ArrayList<ChatMessage> chatMessagesList = new ArrayList<ChatMessage>();

                // update stats in receiver messages
                DatabaseReference msgListRef = FirebaseDatabase.getInstance().getReference().child("messages").child(receiverUid).child(senderUid);
                try {
                    for (DataSnapshot issue : dataSnapshot.child("messages").child(receiverUid).child(senderUid).getChildren()) {
                        String from = issue.child("from").getValue(String.class);
                        String key = issue.getKey();
                        if ((!from.equals(senderUid))){
                            Log.i(TAG, "seen before: " + issue.child("seen").getValue(Boolean.class));
                            msgListRef.child(key).child("seen").setValue(true);
                            Log.i(TAG, "seen after: " + issue.child("seen").getValue(Boolean.class));
                        }


                    }
                } catch(NullPointerException e){
                    Log.i(TAG, "There are no messages in the DB yet or a refference problem");
                    e.printStackTrace();
                }
                //  seen status end

                try {
                    for (DataSnapshot issue : dataSnapshot.child("messages").child(senderUid).child(receiverUid).getChildren()) {

                        // retrieve data in order to create a message to add it to messages list
                        String body = issue.child("body").getValue(String.class);
                        Long dayTimestamp = issue.child("dayTimestamp").getValue(Long.class);
                        String from = issue.child("from").getValue(String.class);
                        Long negatedTimestamp = issue.child("negatedTimestamp").getValue(Long.class);
                        Long timestamp = issue.child("timestamp").getValue(Long.class);
                        String to = issue.child("to").getValue(String.class);
                        Boolean seen = issue.child("seen").getValue(Boolean.class);
                        Log.i(TAG, "seen in chat: " + seen);




                        Message msg = new Message(
                                timestamp,
                                negatedTimestamp,
                                dayTimestamp,
                                body,
                                from,
                                to,
                                seen
                        );


                        chatMessagesList.add(new ChatMessage(msg,senderUid,seen));

                }
                } catch(NullPointerException e){
                    Log.i(TAG, "There are no messages in the DB yet or a refference problem");
                    e.printStackTrace();
                }

                ChatAdapter adapter = new ChatAdapter(getApplicationContext(), chatMessagesList);
                ListView listView = findViewById(R.id.list_of_messages);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        Intent serviceintent = new Intent("it.polito.mad.lab5.MessagingService");
//        intent.putExtra("MyService.data", "myValue");
//        startService(serviceintent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                EditText input = (EditText)findViewById(R.id.input);
                Log.i(TAG, "send btn: msg " + input.getText().toString());

                Log.i(TAG, "send btn: Sender of the messages is: " + senderUid);
                Log.i(TAG, "send btn: receiverUid of the messages is: " + receiverUid);

                // create a message and push it to the DB
                createMessage(input.getText().toString(), senderUid, receiverUid);

                // Clear the input
                input.setText("");
                break;

            default:
                break;
        }
    }

    public void createMessage(String msg, String _ownerUid, String _userUid) {
        long timestamp = new Date().getTime();
        long dayTimestamp = getDayTimestamp(timestamp);

        Message message =
                new Message(timestamp,
                        -timestamp,
                        dayTimestamp,
                        msg,
                        _ownerUid,
                        _userUid,
                        senderName,
                        false);
        mDatabase
                .child("notifications")
                .child("messages")
                .push()
                .setValue(message);
        Log.i(TAG, "pushed in notifications.messages: " + message);

        mDatabase
                .child("messages")
                .child(_userUid)
                .child(_ownerUid)
                .push()
                .setValue(message);
        Log.i(TAG, "pushed messages in userUid: " + message);

        if (!_userUid.equals(_ownerUid)) {
            mDatabase
                    .child("messages")
                    .child(_ownerUid)
                    .child(_userUid)
                    .push()
                    .setValue(message);
            Log.i(TAG, "pushed messages in ownerUid: " + message);
        }
    }


    private long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public void onBackPressed() {
        goToChatList();
    }

    public void goToChatList() {
        Intent intent = new Intent(getApplicationContext(), ChatList.class);
        startActivity(intent);
    }


}
