package com.example.airhockey.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.example.airhockey.event.OutboundEvent;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.Player;
import com.example.airhockey.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class ClientConnectListener implements ConnectListener {
    private final SocketIOServer server;
    private final GameService gameService;

    @Override
    public void onConnect(SocketIOClient client) {
        Player player = new Player();
        player.setId(client.getSessionId().toString());
        GameSession gameSession = gameService.addPlayerToTable(player);
        log.info("Player {} connected to server", client.getSessionId().toString());
        server.getBroadcastOperations().sendEvent(OutboundEvent.INIT_GAME.getName(), gameSession);
    }
}
