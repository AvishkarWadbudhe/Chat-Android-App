package com.example.chatapp.activities;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import com.example.chatapp.adapters.RecentConversationAdapter;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.listeners.ConversionListener;
import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class MainActivity extends BaseActivities implements ConversionListener {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversation;
    private RecentConversationAdapter conversationAdapter;
    private FirebaseFirestore database;
    private static final String RECEIVE_NOTIFICATIONS = "receive_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager= new PreferenceManager(getApplicationContext());
        init();
        loadUserDetails();
        getToken();
        setListeners();
        listenerConversation();

    }

    private void init(){
        conversation = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(this,conversation,this);
        binding.conversationsRecycleView.setAdapter(conversationAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void setListeners()
    {

        binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),UserActivity.class)));
    }

    private void loadUserDetails(){
        binding.textname.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes= Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
      Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
      binding.imageProfile.setImageBitmap(bitmap);

    }
    private void showTost(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void listenerConversation()
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);

    }


private final EventListener<QuerySnapshot> eventListener = (value, error) -> {

        if(error!=null)
        {
            return;
        }
        if(value!=null) {
            for(DocumentChange documentChange :value.getDocumentChanges())
            {
                if(documentChange.getType()==DocumentChange.Type.ADDED)
                {
                    String senderId=documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId=documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId=senderId;
                    chatMessage.receiverId= receiverId;
                    if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName= documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }
                    else{
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversionName= documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message =documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject= documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                   conversation.add(chatMessage);
                }else if(documentChange.getType()==DocumentChange.Type.MODIFIED){
                    for (int i = 0; i < conversation.size(); i++) {
                        String senderId=documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId=documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversation.get(i).senderId.equals(senderId)&&conversation.get(i).receiverId.equals(receiverId))
                        {
                            conversation.get(i).message= documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversation.get(i).dateObject= documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }

            Collections.sort(conversation,(obj1,obj2) ->obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationsRecycleView.smoothScrollToPosition(0);
            binding.conversationsRecycleView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }

};

    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USER).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token)

                .addOnFailureListener(e -> showTost("Unable To Update Token"));
    }

    private void signOut()
    {
        showTost("Signing out..");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USER).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showTost("Unable to sign out"));
    }

    @Override
    public void onConversionClicked(User user) {
        Intent intent = new Intent(getApplicationContext(),ChatAcivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
    }
}