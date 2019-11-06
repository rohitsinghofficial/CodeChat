package com.example.codechat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {


private Activity myActivity;
private DatabaseReference myDatabaseRef;
private String myUserName;
private ArrayList<DataSnapshot> mySnapShot;


//childEvent listener

    private ChildEventListener myListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mySnapShot.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


// Constructor goes here

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){
        myActivity = activity;
        myUserName = name;
        myDatabaseRef = ref.child("chats");
        mySnapShot = new ArrayList<>();



        //add listener
        myDatabaseRef.addChildEventListener(myListener);

    }

//Static class
    static class ViewHolder{
        TextView senderName;
        TextView chatBody;
        LinearLayout.LayoutParams layoutParams;

    }

    @Override
    public int getCount() {
        return mySnapShot.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mySnapShot.get(position);


        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null){
            LayoutInflater inflater= (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.senderName = (TextView) view.findViewById(R.id.author);
            holder.chatBody = (TextView) view.findViewById(R.id.message);
            holder.layoutParams = (LinearLayout.LayoutParams) holder.senderName.getLayoutParams();

            view.setTag(holder);

        }
            final  InstantMessage message = getItem(position);
        final  ViewHolder holder = (ViewHolder) view.getTag();



        //Styling
        boolean isMe = message.getAuthor().equals(myUserName);
        //call a func for styling
        chatRowStyling(isMe, holder);



        String author = message.getAuthor();
        holder.senderName.setText(author);


        String msg= message.getMessage();
        holder.chatBody.setText(msg);







        return view;
    }

    private void chatRowStyling (boolean isItMe, ViewHolder holder){
        if(isItMe){
            holder.layoutParams.gravity = Gravity.END;
            holder.senderName.setTextColor(Color.BLUE);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_green);

        }else{
            holder.layoutParams.gravity = Gravity.START;
            holder.senderName.setTextColor(Color.GREEN);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_orange);

        }
        //Forgot this setUp
        holder.senderName.setLayoutParams(holder.layoutParams);
        holder.chatBody.setLayoutParams(holder.layoutParams);

    }
    public void freeUpResource(){
        myDatabaseRef.removeEventListener(myListener);
    }


}
