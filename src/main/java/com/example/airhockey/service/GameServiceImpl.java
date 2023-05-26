package com.example.airhockey.service;

import com.example.airhockey.model.Table;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.model.Player;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Getter
public class GameServiceImpl implements GameService {
    private Table table;

    public Table addPlayerToTable(Player player) {
        if (table == null) {
            createGameSession();
        }
        if (table.getFirstPlayer() == null) {
            table.setFirstPlayer(player);
            if (table.getSecondPlayer() == null) {
                table.setStatus(GameStatus.NEW);
            }
        } else if (table.getSecondPlayer() == null) {
            table.setSecondPlayer(player);
            table.setStatus(GameStatus.READY);
        } else {
            throw new RuntimeException("Game session is already full");
        }
        return table;
    }

    public void removePlayerFromTable(String playerId) {
        if (table != null) {
            Player firstPlayer = table.getFirstPlayer();
            Player secondPlayer = table.getSecondPlayer();
            if (firstPlayer != null && firstPlayer.getId().equals(playerId)) {
                table.setFirstPlayer(null);
                table.setStatus(GameStatus.NEW);
            } else if (secondPlayer != null && secondPlayer.getId().equals(playerId)) {
                table.setSecondPlayer(null);
                table.setStatus(GameStatus.NEW);
            }
        }
        if (isSessionEmpty()) {
            closeGameSession();
        }
    }

    public void startGame() {
        switch (table.getStatus()) {
            case NEW -> throw new IllegalArgumentException("Not enough players to start the game");
            case IN_PROCESS -> throw new IllegalStateException("The game is already started");
            default -> table.setStatus(GameStatus.IN_PROCESS);
        }
    }

    private void createGameSession() {
        table = new Table();
        table.setStatus(GameStatus.NEW);
    }

    private boolean isSessionEmpty() {
        return table != null
                && table.getFirstPlayer() == null && table.getSecondPlayer() == null;
    }

    private void closeGameSession() {
        table = null;
    }
}
