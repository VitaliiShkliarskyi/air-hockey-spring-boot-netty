package com.example.airhockey.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.model.Player;
import com.example.airhockey.model.Position;
import com.example.airhockey.model.Table;
import com.example.airhockey.service.GameServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SocketModule {
    private final SocketIOServer server;
    private final GameServiceImpl gameService;

    private final Table table = new Table();

    public SocketModule(SocketIOServer server, GameServiceImpl gameService) {
        this.server = server;
        this.gameService = gameService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("start_game", GameSession.class, onGameStarted());
        server.addEventListener("move_player", Position.class, onPlayerMoved());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            Player player = new Player();
            player.setId(client.getSessionId().toString());
            GameSession gameSession = gameService.addPlayerToTable(player);
            log.info("Player {} connected to server", client.getSessionId().toString());
            server.getBroadcastOperations().sendEvent("connected", gameSession);
        };
    }

    private DisconnectListener onDisconnected() {
        return (client) -> {
            String playerId = client.getSessionId().toString();
            gameService.removePlayerFromTable(playerId);
            log.info("Player {} disconnected from server", client.getSessionId().toString());
            server.getBroadcastOperations().sendEvent("disconnected", playerId);
        };

    }

    private DataListener<GameSession> onGameStarted() {
        return (client, data, ackSender) -> {
            gameService.startGame();
            server.getBroadcastOperations().sendEvent("game_started", table);
            log.info("Player {} started the game", client.getSessionId().toString());
        };
    }

    private DataListener<Position> onPlayerMoved() {
        return (client, data, ackSender) -> {
            if (gameService.getGameSession().getStatus() == GameStatus.IN_PROCESS) {
                gameService.updatePlayerPosition(data, client.getSessionId().toString());
                server.getBroadcastOperations().sendEvent("puck_moved", gameService.getGameSession());
                gameService.checkCollisionWithPuck(client.getSessionId().toString());
                server.getBroadcastOperations().sendEvent("puck_moved", gameService.getGameSession());
                gameService.checkCollisionWithWall(client.getSessionId().toString());
                server.getBroadcastOperations().sendEvent("puck_moved", gameService.getGameSession());
            }
        };
    }
}
