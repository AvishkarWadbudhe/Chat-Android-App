package com.example.chatapp.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USER="users";
    public static final String KEY_NAME="name";
    public static final String KEY_EMAIL="username";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_PREFERENCE_NAME="chatAppPreference";
    public static final String KEY_IS_SIGNED_IN="isSignedIn";
    public static final String KEY_USER_ID="userId";
    public static final String KEY_IMAGE="image";
    public static final String KEY_FCM_TOKEN="fcmToken";
    public static final String KEY_USER="user";
    public static final String KEY_COLLECTION_CHAT ="chat";
    public static final String KEY_SENDER_ID="senderId";
    public static final String KEY_RECEIVER_ID="receiverId";
    public static final String KEY_MESSAGE ="message";
    public static final String KEY_TIMESTAMP="timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversation";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME="receiverName";
    public static final String KEY_SENDER_IMAGE ="senderImage";
    public static final String KEY_RECEIVER_IMAGE="receiverImage";
    public static final String KEY_LAST_MESSAGE="lastMessage";
    public static final String KEY_AVAILABILITY="availability";
    public static final String REMOTE_MSG_AUTHORIZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";

    public static final int PERMISSIONS_REQUEST_CODE = 101;
    public static final String RECEIVE_NOTIFICATIONS = "receive_notifications";


    public static HashMap<String,String> remoteMsgHeaders = null;
    public static HashMap<String,String> getRemoteMsgHeaders()
    {
        if(remoteMsgHeaders==null)
        {
            remoteMsgHeaders=new HashMap<>();
        remoteMsgHeaders.put(
                REMOTE_MSG_AUTHORIZATION,"key=AAAAvdRUYsI:APA91bHU8Mns8l3-QriCu6ZaZzRteEqw41UMO9HpI-LV7J3H1CMyDURRerpoZKPS9MPB1zDtFSxUXrJbOz7RUaS1lmcNgp-3AydfNduyO8u7l_5LBKmuef6bKOiBjre9C0OQ5z_FJlha"
        );
        remoteMsgHeaders.put(
                REMOTE_MSG_CONTENT_TYPE,"application/json"
        );
        }
        return remoteMsgHeaders;
    }



}

