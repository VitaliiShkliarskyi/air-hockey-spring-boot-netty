package com.example.airhockey.model;

import lombok.Data;

@Data
public class Puck {
    private final double radius;
    private Position position;
    private double velocityX;
    private double velocityY;

    public Puck() {
        this.radius = 2;
        this.position = new Position(25, 12.5);
        this.velocityX = 0;
        this.velocityY = 0;
    }
}
