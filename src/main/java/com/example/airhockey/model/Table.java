package com.example.airhockey.model;

import lombok.Data;

@Data
public class Table {
    private Player firstPlayer;
    private Player secondPlayer;
    private Puck puck;
    private GameStatus status;
    private double width;
    private double height;
}
