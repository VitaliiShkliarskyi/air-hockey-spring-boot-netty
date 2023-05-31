package com.example.airhockey.service.impl;

import java.util.Timer;
import java.util.TimerTask;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.model.Player;
import com.example.airhockey.model.Position;
import com.example.airhockey.service.GameService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class GameServiceImpl implements GameService {
    private static final int GAME_TIME_IN_NANOS = 300000;
    private GameSession gameSession;
    private Timer timer;
    private TimerTask timerTask;

    public synchronized GameSession addPlayerToTable(Player player) {
        if (gameSession == null) {
            createGameSession();
        }
        if (gameSession.getFirstPlayer() == null) {
            player.setPosition(new Position(4, 6.25));
            gameSession.setFirstPlayer(player);
            if (gameSession.getSecondPlayer() == null) {
                gameSession.setStatus(GameStatus.NEW);
            } else {
                gameSession.setStatus(GameStatus.READY);
            }
        } else if (gameSession.getSecondPlayer() == null) {
            player.setPosition(new Position(21, 6.25));
            gameSession.setSecondPlayer(player);
            gameSession.setStatus(GameStatus.READY);
        } else {
            throw new RuntimeException("Game session is already full");
        }
        return gameSession;
    }

    public synchronized void removePlayerFromTable(String playerId) {
        if (gameSession != null) {
            Player firstPlayer = gameSession.getFirstPlayer();
            Player secondPlayer = gameSession.getSecondPlayer();
            if (firstPlayer != null && firstPlayer.getId().equals(playerId)) {
                gameSession.setFirstPlayer(null);
                gameSession.setStatus(GameStatus.NEW);
            } else if (secondPlayer != null && secondPlayer.getId().equals(playerId)) {
                gameSession.setSecondPlayer(null);
                gameSession.setStatus(GameStatus.NEW);
            }
        }
        if (isSessionEmpty()) {
            closeGameSession();
        }
    }

    public synchronized void startGame() {
        switch (gameSession.getStatus()) {
            case NEW -> throw new RuntimeException("Not enough players to start the game");
            case IN_PROCESS -> throw new RuntimeException("The game is already started");
            default -> gameSession.setStatus(GameStatus.IN_PROCESS);
        }
        startTimer();
    }

    private void createGameSession() {
        gameSession = new GameSession();
    }

    private boolean isSessionEmpty() {
        return gameSession != null
                && gameSession.getFirstPlayer() == null && gameSession.getSecondPlayer() == null;
    }

    private void closeGameSession() {
        gameSession = null;
    }

    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                stopTimer();
            }
        };
        timer.schedule(timerTask, GAME_TIME_IN_NANOS);
    }

    private void stopTimer() {
        gameSession.setStatus(GameStatus.FINISHED);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
