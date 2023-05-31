package com.example.airhockey.service;

import com.example.airhockey.dto.MoveDto;

public interface GameEngine {
    void updatePlayerPosition(MoveDto data, String playerId);

    void checkCollisionWithPuck(String playerId);

    void checkCollisionWithWall(String playerId);
}
