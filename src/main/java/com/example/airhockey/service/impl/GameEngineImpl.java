package com.example.airhockey.service.impl;

import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.model.*;
import com.example.airhockey.service.GameEngine;
import com.example.airhockey.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameEngineImpl implements GameEngine {
    private final GameService gameService;
    private final Table table = new Table();

    public synchronized void updatePlayerPosition(MoveDto data, String playerId) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            player.setPosition(new Position(data.getX(), data.getY()));
        }
        savePlayerToGameSession(player);
    }

    public synchronized void checkCollisionWithPuck(String playerId) {
        Player player = getPlayerById(playerId);
        Position playerPosition = player.getPosition();
        Puck puck = gameService.getGameSession().getPuck();
        Position puckPosition = puck.getPosition();
        double distance = Math.sqrt(Math.pow(puckPosition.getX() - playerPosition.getX(), 2)
                + Math.pow(puckPosition.getY() - playerPosition.getY(), 2));
        // collision with the puck occurs
        if (distance <= player.getRadius() + puck.getRadius()) {
            puck.setVelocityX(5);
            puck.setVelocityY(5);
            // determination of the puck motion vector
            double directionX = puckPosition.getX() - playerPosition.getX();
            double directionY = puckPosition.getY() - playerPosition.getY();
            // normalization of the puck motion vector
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
            directionX /= magnitude;
            directionY /= magnitude;
            // Changing the direction of movement of the puck
            puckPosition.setX(directionX);
            puckPosition.setY(directionY);
            puck.setPosition(puckPosition);
            savePuckToGameSession(puck);
            // additional logic after collision with the puck
            // ...
        }
    }

    public synchronized void checkCollisionWithWall(String playerId) {
        Puck puck = gameService.getGameSession().getPuck();
        Position puckPosition = puck.getPosition();
        double puckPositionX = puckPosition.getX();
        double puckPositionY = puckPosition.getY();
        // collision with right or left wall occurs
        if (puckPositionX - puck.getRadius() < 0 || puckPositionX + puck.getRadius() > table.getHeight()) {
            puck.setVelocityX(-puck.getVelocityX());
        }
        // collision with upper or lower wall occurs
        if (puckPositionY - puck.getRadius() < 0 || puckPositionY + puck.getRadius() > table.getWidth()) {
            puck.setVelocityY(-puck.getVelocityY());
        }
        puckPosition.setX(puckPositionX + puck.getVelocityX());
        puckPosition.setY(puckPositionY + puck.getVelocityY());
        puck.setPosition(puckPosition);
        savePuckToGameSession(puck);
    }

    private void savePlayerToGameSession(Player updatedPlayer) {
        GameSession gameSession = gameService.getGameSession();
        Player firstPlayer = gameSession.getFirstPlayer();
        Player secondPlayer = gameSession.getSecondPlayer();
        if (firstPlayer != null && firstPlayer.getId().equals(updatedPlayer.getId())) {
            gameSession.setFirstPlayer(updatedPlayer);
            gameService.setGameSession(gameSession);
        } else if (secondPlayer != null && secondPlayer.getId().equals(updatedPlayer.getId())) {
            gameSession.setSecondPlayer(updatedPlayer);
            gameService.setGameSession(gameSession);
        }
    }

    private void savePuckToGameSession(Puck puck) {
        GameSession gameSession = gameService.getGameSession();
        gameSession.setPuck(puck);
        gameService.setGameSession(gameSession);
    }

    private Player getPlayerById(String playerId) {
        Player firstPlayer = gameService.getGameSession().getFirstPlayer();
        Player secondPlayer = gameService.getGameSession().getSecondPlayer();
        if (firstPlayer != null && firstPlayer.getId().equals(playerId)) {
            return firstPlayer;
        } else if (secondPlayer != null && secondPlayer.getId().equals(playerId)) {
            return secondPlayer;
        }
        return null;
    }
}
