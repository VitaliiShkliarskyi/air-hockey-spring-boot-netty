package com.example.airhockey.service;

import java.util.Timer;
import java.util.TimerTask;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.model.Player;
import com.example.airhockey.model.Position;
import com.example.airhockey.model.Puck;
import com.example.airhockey.model.Table;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Getter
public class GameServiceImpl implements GameService {
    private static final int GAME_TIME_IN_NANOS = 300000;
    private GameSession gameSession;
    private Timer timer;
    private TimerTask timerTask;
    private final Table table = new Table();

    public synchronized GameSession addPlayerToTable(Player player) {
        if (gameSession == null) {
            createGameSession();
        }
        if (gameSession.getFirstPlayer() == null) {
            player.setPosition(new Position(4, 6.25));
            gameSession.setFirstPlayer(player);
            if (gameSession.getSecondPlayer() == null) {
                gameSession.setStatus(GameStatus.NEW);
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

    public synchronized void updatePlayerPosition(Position data, String playerId) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            player.setPosition(new Position(data.getX(), data.getY()));
        }
        updateGameSession(player);
    }

    public synchronized void checkCollisionWithPuck(String playerId) {
        Player player = getPlayerById(playerId);
        Position playerPosition = player.getPosition();
        Puck puck = gameSession.getPuck();
        Position puckPosition = puck.getPosition();
        double distance = Math.sqrt(Math.pow(puckPosition.getX() - playerPosition.getX(), 2)
                + Math.pow(puckPosition.getY() - playerPosition.getY(), 2));
        // collision with the puck
        if (distance <= player.getRadius() + puck.getRadius()) {
            // TODO
        }
    }

    public synchronized void checkCollisionWithWall(String playerId) {
        Puck puck = gameSession.getPuck();
        Position puckPosition = puck.getPosition();
        double puckPositionX = puckPosition.getX();
        double puckPositionY = puckPosition.getY();
        // collision with right or left wall
        if (puckPositionX - puck.getRadius() < 0 || puckPositionX + puck.getRadius() > table.getHeight()) {
            puck.setVelocityX(-puck.getVelocityX());
        }
        // collision with upper or lower wall
        if (puckPositionY - puck.getRadius() < 0 || puckPositionY + puck.getRadius() > table.getWidth()) {
            puck.setVelocityY(-puck.getVelocityY());
        }
        puckPosition.setX(puckPositionX + puck.getVelocityX());
        puckPosition.setY(puckPositionY + puck.getVelocityY());
        puck.setPosition(puckPosition);
        gameSession.setPuck(puck);
    }

    private void updateGameSession(Player updatedPlayer) {
        Player firstPlayer = gameSession.getFirstPlayer();
        Player secondPlayer = gameSession.getSecondPlayer();
        if (firstPlayer != null && firstPlayer.getId().equals(updatedPlayer.getId())) {
            gameSession.setFirstPlayer(updatedPlayer);
        } else if (secondPlayer != null && secondPlayer.getId().equals(updatedPlayer.getId())) {
            gameSession.setSecondPlayer(updatedPlayer);
        }
    }

    private Player getPlayerById(String playerId) {
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
