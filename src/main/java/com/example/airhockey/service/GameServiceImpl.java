package com.example.airhockey.service;

import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.model.Player;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Log4j2
@Service
@Getter
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
            gameSession.setFirstPlayer(player);
            if (gameSession.getSecondPlayer() == null) {
                gameSession.setStatus(GameStatus.NEW);
            }
        } else if (gameSession.getSecondPlayer() == null) {
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

    public synchronized Player getPlayerById(String playerId) {
        Player firstPlayer = gameSession.getFirstPlayer();
        Player secondPlayer = gameSession.getSecondPlayer();
        if (firstPlayer != null && firstPlayer.getId().equals(playerId)) {
            return firstPlayer;
        } else if (secondPlayer != null && secondPlayer.getId().equals(playerId)) {
            return secondPlayer;
        }
        return null;
    }
    private void createGameSession() {
        gameSession = new GameSession();
        gameSession.setStatus(GameStatus.NEW);
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
