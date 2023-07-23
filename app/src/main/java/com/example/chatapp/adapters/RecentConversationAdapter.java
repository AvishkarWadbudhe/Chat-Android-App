package com.example.chatapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemContainerRecentConversionBinding;
import com.example.chatapp.listeners.ConversionListener;
import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;

import java.util.List;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final ConversionListener conversionListener;
    private PreferenceManager preferenceManager;

    public RecentConversationAdapter(Context context, List<ChatMessage> chatMessages, ConversionListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
        this.preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.setData(chatMessage);

        // Calculate unread message count
        int unreadMessageCount = 0;
        // Check if the conversation is unread and not sent by the current user
        if (chatMessage.unreadCount > 0 && !chatMessage.senderId.equals(preferenceManager.getString(Constants.KEY_USER_ID))) {
            unreadMessageCount = chatMessage.unreadCount;
        }

        // Display unread message count on UI
        if (unreadMessageCount > 0) {
            holder.binding.textUnreadMessageCount.setVisibility(View.VISIBLE);
            holder.binding.textUnreadMessageCount.setText(String.valueOf(unreadMessageCount));
        } else {
            holder.binding.textUnreadMessageCount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerRecentConversionBinding binding;

        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.conversionImage));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);

            // Display unread message count on UI
            if (chatMessage.unreadCount > 0) {
                binding.textUnreadMessageCount.setVisibility(View.VISIBLE);
                binding.textUnreadMessageCount.setText(String.valueOf(chatMessage.unreadCount));
            } else {
                binding.textUnreadMessageCount.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.conversionId;
                user.name = chatMessage.conversionName;
                user.image = chatMessage.conversionImage;
                conversionListener.onConversionClicked(user);
            });
        }


    }

    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
