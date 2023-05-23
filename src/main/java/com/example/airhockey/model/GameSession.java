package com.example.airhockey.model;

import lombok.Data;

@Data
public class GameSession {
    private String id;
    private Player firstPlayer;
    private Player secondPlayer;
    private GameStatus status;
}
