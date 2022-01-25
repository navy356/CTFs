package org.shatranj.diwana.shatranjserver.dto;

public class MoveDTO {
    private String whiteMove;

    private String blackMove;

    private String boardStateBefore;

    public MoveDTO() {
    }

    public MoveDTO(String whiteMove, String blackMove, String boardStateBefore) {
        this.whiteMove = whiteMove;
        this.blackMove = blackMove;
        this.boardStateBefore = boardStateBefore;
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

    public String getBoardStateBefore() {
        return boardStateBefore;
    }

    public void setBoardStateBefore(String boardStateBefore) {
        this.boardStateBefore = boardStateBefore;
    }
}
