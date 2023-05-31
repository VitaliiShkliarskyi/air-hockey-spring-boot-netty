package com.example.airhockey.event;

public enum InboundEvent {
    START_GAME("start_game"),
    PLAYER_MOVE("player_move");


    private final String name;

    InboundEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
