package com.example.airhockey.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Puck {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    public Puck() {
        this.x = 0;
        this.y = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
}
