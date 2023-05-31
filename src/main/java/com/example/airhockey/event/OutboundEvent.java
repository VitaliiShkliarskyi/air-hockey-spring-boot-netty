package com.example.airhockey.event;

public enum OutboundEvent {
    INIT_GAME("init_game"),
    DISCONNECTED("disconnected"),
    GAME_STARTED("game_started"),
    STATE_UPDATED("state_updated");

    private final String name;

    OutboundEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
