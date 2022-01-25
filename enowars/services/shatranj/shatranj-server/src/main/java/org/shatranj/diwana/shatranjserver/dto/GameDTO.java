package org.shatranj.diwana.shatranjserver.dto;

import com.github.bhlangonijr.chesslib.Side;

public class GameDTO {
    private String boardState;

    private Side winner;

    private String name;

    public GameDTO() {
    }

    public GameDTO(String boardState, Side winner, String name) {
        this.boardState = boardState;
        this.winner = winner;
        this.name = name;
    }

    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }

    public Side getWinner() {
        return winner;
    }

    public void setWinner(Side winner) {
        this.winner = winner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
