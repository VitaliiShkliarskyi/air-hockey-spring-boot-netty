package com.example.airhockey.service;

import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.model.GameSession;
import com.example.airhockey.model.Player;

public interface GameService {
    GameSession getGameSession();

    void setGameSession(GameSession gameSession);

    GameSession addPlayerToTable(Player player);

    void removePlayerFromTable(String playerId);

    void startGame();
}
