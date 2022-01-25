package org.shatranj.diwana.shatranjserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class MoveSet {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @NotNull
    private Game game;

    @NotBlank
    private String boardStateBefore;

    @NotBlank
    private String whiteMove;

    @NotBlank
    private String blackMove;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getBoardStateBefore() {
        return boardStateBefore;
    }

    public void setBoardStateBefore(String boardState) {
        this.boardStateBefore = boardState;
    }

    public String getWhiteMove() {
        return whiteMove;
    }

    public void setWhiteMove(String whiteMove) {
        this.whiteMove = whiteMove;
    }

    public String getBlackMove() {
        return blackMove;
    }

    public void setBlackMove(String blackMove) {
        this.blackMove = blackMove;
    }
}
