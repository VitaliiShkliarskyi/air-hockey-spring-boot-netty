package com.example.airhockey.model;

import lombok.Data;

@Data
public class GameSession {
    private Player firstPlayer;
    private Player secondPlayer;
    private Puck puck;
    private GameStatus status;

    public GameSession() {
        puck = new Puck();
        status = GameStatus.NEW;
    }
}
