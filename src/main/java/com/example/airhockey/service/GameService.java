package com.example.airhockey.service;

import com.example.airhockey.dto.Dto;
import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.Player;
import com.example.airhockey.model.Position;

public interface GameService {
    GameSession getGameSession();
    GameSession addPlayerToTable(Player player);
    void removePlayerFromTable(String playerId);
    void startGame();
    void updatePlayerPosition(MoveDto data, String playerId);
    void checkCollisionWithPuck(String playerId);
    void checkCollisionWithWall(String playerId);

}
