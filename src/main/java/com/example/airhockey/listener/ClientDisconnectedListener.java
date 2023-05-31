package com.example.airhockey.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.airhockey.event.OutboundEvent;
import com.example.airhockey.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class ClientDisconnectedListener implements DisconnectListener {
    private final SocketIOServer server;
    private final GameService gameService;

    @Override
    public void onDisconnect(SocketIOClient client) {
        String playerId = client.getSessionId().toString();
        gameService.removePlayerFromTable(playerId);
        log.info("Player {} disconnected from server", client.getSessionId().toString());
        server.getBroadcastOperations().sendEvent(OutboundEvent.DISCONNECTED.getName(), playerId);
    }
}
