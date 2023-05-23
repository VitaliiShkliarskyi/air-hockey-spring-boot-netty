package com.example.airhockey.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.Puck;
import com.example.airhockey.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
@AllArgsConstructor
public class AirHockeyController {
    private final SocketIOServer server;
    private final GameService gameService;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Socket ID[{}] Connected to socket", client.getSessionId().toString());
        // TODO: create GameSession
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        // TODO: call method from gameService.removePlayer(client.getSessionId());
    }

    @OnEvent("player_move")
    public void onPlayerMoveEvent(SocketIOClient client, GameSession gameSession) {
        // TODO
    }

    @OnEvent("puck_move")
    public void onPuckMoveEvent(SocketIOClient client, Puck puck) {
        // TODO
    }
}
