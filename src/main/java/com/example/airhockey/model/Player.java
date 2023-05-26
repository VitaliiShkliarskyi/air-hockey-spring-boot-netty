package com.example.airhockey.model;

import lombok.Data;

@Data
public class Player {
    private String id;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private short score;
}
