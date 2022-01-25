package org.shatranj.diwana.shatranjserver.dto;

public class StrategyNoteDTO extends DTO {
    private String message;

    public StrategyNoteDTO() {
    }

    public StrategyNoteDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
