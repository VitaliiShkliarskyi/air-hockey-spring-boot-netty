package com.example.airhockey.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.dto.StartGameDto;
import com.example.airhockey.event.InboundEvent;
import com.example.airhockey.event.OutboundEvent;
import com.example.airhockey.model.Table;
import com.example.airhockey.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class StartGameEventHandler implements EventHandler<StartGameDto> {
    private final SocketIOServer server;
    private final GameService gameService;
    private final Table table = new Table();

    @Override
    public InboundEvent getEventType() {
        return InboundEvent.START_GAME;
    }

    @Override
    public void onEvent(SocketIOClient client, String event, StartGameDto moveDto) {
        gameService.startGame();
        server.getBroadcastOperations().sendEvent(OutboundEvent.GAME_STARTED.getName(), table);
        log.info("Player {} started the game with message {}",
                client.getSessionId().toString(), moveDto.getMessage());
    }

    @Override
    public Class<StartGameDto> getCommandType() {
        return StartGameDto.class;
    }
}
