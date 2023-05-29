package com.example.airhockey.model;

import lombok.Data;

@Data
public class Player {
    private String id;
    private final double radius;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private short score;

    public Player() {
        this.radius = 4;
    }
}
