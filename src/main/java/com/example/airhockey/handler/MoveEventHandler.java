package com.example.airhockey.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.event.InboundEvent;
import com.example.airhockey.event.OutboundEvent;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.service.GameEngine;
import com.example.airhockey.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MoveEventHandler implements EventHandler<MoveDto> {

    private final SocketIOServer server;
    private final GameService gameService;
    private final GameEngine gameEngine;

    @Override
    public InboundEvent getEventType() {
        return InboundEvent.PLAYER_MOVE;
    }

    @Override
    public void onEvent(SocketIOClient client, String event, MoveDto data) {
        if (gameService.getGameSession().getStatus() == GameStatus.IN_PROCESS) {
            gameEngine.updatePlayerPosition(data, client.getSessionId().toString());
            server.getBroadcastOperations().sendEvent(OutboundEvent.STATE_UPDATED.toString(), gameService.getGameSession());
            gameEngine.checkCollisionWithPuck(client.getSessionId().toString());
            server.getBroadcastOperations().sendEvent(OutboundEvent.STATE_UPDATED.toString(), gameService.getGameSession());
            gameEngine.checkCollisionWithWall(client.getSessionId().toString());
            server.getBroadcastOperations().sendEvent(OutboundEvent.STATE_UPDATED.toString(), gameService.getGameSession());
        }
    }

    @Override
    public Class<MoveDto> getCommandType() {
        return MoveDto.class;
    }
}
