package com.example.airhockey.model;

import lombok.Getter;

@Getter
public class Table {
    private final double width;
    private final double height;

    public Table() {
        this.width = 25;
        this.height = 50;
    }
}
