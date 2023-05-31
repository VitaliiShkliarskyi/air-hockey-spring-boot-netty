package com.example.airhockey.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.airhockey.dto.Dto;
import com.example.airhockey.event.InboundEvent;

public interface EventHandler<T extends Dto> {
    InboundEvent getEventType();
    void onEvent(SocketIOClient client, String event, T t);
    Class<T> getCommandType();
}
