package org.shatranj.diwana.shatranjserver.dto;

public class MoveReqDTO extends DTO {
    private String move;

    public MoveReqDTO() {
    }

    public MoveReqDTO(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
