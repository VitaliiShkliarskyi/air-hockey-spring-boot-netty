package com.example.airhockey.model;

import lombok.Data;

@Data
public class Puck {
    private final double radius;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    public Puck() {
        this.radius = 2;
        this.x = 25;
        this.y = 12.5;
        this.velocityX = 0;
        this.velocityY = 0;
    }
}
