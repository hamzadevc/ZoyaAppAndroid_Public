package com.grappetite.zoya.helpers;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.MessageData;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.restapis.parsers.IncomingMessageParser;
import com.grappetite.zoya.tubesock.WebSocket;
import com.grappetite.zoya.tubesock.WebSocketEventHandler;
import com.grappetite.zoya.tubesock.WebSocketException;
import com.grappetite.zoya.tubesock.WebSocketMessage;

import java.net.URI;
import java.util.ArrayList;

public class ChatHelper {
    private static final String TAG = "ChatHelper";
    private static ChatHelper chatHelper;
    private long userId;
    private WebSocket ws;
    private Listener listener;

    private ChatHelper(Long userId) {
        this.userId = userId;
        connectToWebSocket();
    }

    @NonNull
    public static ChatHelper getInstance() {
        if (chatHelper == null)
            throw new IllegalStateException("Chat helper is not initialized");
        return chatHelper;
    }

    public static boolean isInitialized() {
        return chatHelper != null;
    }

    public static void initialize() {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (chatHelper == null && pd!=null)
            chatHelper = new ChatHelper(pd.getId());
    }

    public static void terminate() {
        if (chatHelper != null)
            chatHelper.release();
        chatHelper = null;
    }

    private void connectToWebSocket() {
        try {
            ws = new WebSocket(URI.create("ws://zoyahealthapp.com:8080"));
            ws.setEventHandler(new SocketListener());
            ws.connect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void reconnectToWebSocket() {
        connectToWebSocket();
    }

    public boolean isConnected() {
        return ws != null && ws.isRunning();
    }

    private void release() {
        if (ws != null)
            ws.close();
        ws = null;
        listener = null;
    }

    public boolean sendMessage(long expertId, String message) {
        if (isConnected()) {
            ws.send(new ChatPurpose(expertId, message).toString());
            return true;
        } else
            return false;
    }

    public void requestChatHistory(long expertId) {
        if (ws != null && ws.isRunning()) {
            ws.send(new ExpertHistoryChatPurpose(expertId).toString());
        } else if (listener != null)
            listener.onSocketConnectionFail();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public interface Listener {
        void onSocketConnected();

        void onHistoryReceived(ArrayList<MessageData> messageHistory);

        void onMessageReceived(MessageData message);

        void onSocketConnectionFail();
    }

    private class SocketListener implements WebSocketEventHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "onOpen");
            if (listener != null)
                listener.onSocketConnected();
            if (ws != null)
                ws.send(new AttachPurpose(userId).toString());
            else if (listener != null)
                listener.onSocketConnectionFail();
        }

        @Override
        public void onMessage(WebSocketMessage message) {
            Log.e(TAG, "onMessage: " + message.getText());
            IncomingMessageParser parser = new IncomingMessageParser(message.getText());
            if (parser.getPurpose() != null)
                switch (parser.getPurpose()) {
                    case ATTACHED:
                        if (parser.hasMessagesKey() && listener != null)
                            listener.onHistoryReceived(parser.getMessageHistory());
                        break;
                    case CHAT:
                        if (listener != null)
                            listener.onMessageReceived(parser.getMessage());
                        break;
                }
        }

        @Override
        public void onClose() {
            Log.e(TAG, "onClose");
            ws = null;
            if (listener != null)
                listener.onSocketConnectionFail();
        }

        @Override
        public void onError(WebSocketException e) {
            Log.e(TAG, "onError: " + e.toString());
        }

        @Override
        public void onLogMessage(String msg) {
            Log.e(TAG, "onLogMessage: " + msg);
        }
    }

    private class AttachPurpose {
        private String purpose = "attach_user";
        @SerializedName("user_id")
        private long userId;

        private AttachPurpose(long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    private class ChatPurpose {
        private String purpose = "chat";
        @SerializedName("message_to")
        private String messageTo = "expert";
        @SerializedName("expert_id")
        private long expertId;
        @SerializedName("message")
        private String message;

        private ChatPurpose(long expertId, String message) {
            this.expertId = expertId;
            this.message = message;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    private class ExpertHistoryChatPurpose {
        private String purpose = "expert_chat_history";
        @SerializedName("user_id")
        private long userId;
        @SerializedName("expert_id")
        private long expertId;

        private ExpertHistoryChatPurpose(long expertId) {
            this.expertId = expertId;
            this.userId = ChatHelper.this.userId;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
