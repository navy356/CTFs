package org.shatranj.diwana.shatranjserver.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = StrategyNoteDTO.class, name = "StrategyNoteDTO"),
        @JsonSubTypes.Type(value = StrategyNoteReqDTO.class, name = "StrategyNoteReqDTO"),
        @JsonSubTypes.Type(value = MoveReqDTO.class, name = "MoveReqDTO")
})
public abstract class DTO {
}
