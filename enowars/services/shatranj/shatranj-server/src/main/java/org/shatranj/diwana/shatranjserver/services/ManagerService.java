package org.shatranj.diwana.shatranjserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.shatranj.diwana.shatranjserver.dto.*;
import org.shatranj.diwana.shatranjserver.model.User;
import org.shatranj.diwana.shatranjserver.model.repositories.UserRepository;
import org.shatranj.diwana.shatranjserver.services.chess.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {
    private final ObjectMapper objectMapper;
    private final ChessService chessService;
    private final UserRepository userRepository;

    @Autowired
    public ManagerService(ChessService chessService, UserRepository userRepository) {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.chessService = chessService;
        this.userRepository = userRepository;
    }

    public Object handleDTOInput(String value, User user) throws JsonProcessingException {
        var dto = objectMapper.readValue(value, DTO.class);

        if (dto instanceof MoveReqDTO) {
            return chessService.makeMove(user, ((MoveReqDTO) dto).getMove());
        } else if (dto instanceof StrategyNoteDTO) {
            return chessService.saveStrategyNote(user, ((StrategyNoteDTO) dto).getMessage());
        } else if (dto instanceof StrategyNoteReqDTO) {
            return chessService.getStrategyNote(((StrategyNoteReqDTO) dto).getId());
        } else if (dto instanceof UserDTO) {
            return userRepository.findAllByUsername(((UserDTO) dto).getUsername())
                    .stream()
                    .map(chessService::getStrategyNoteIds)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } else {
            return "Input Error";
        }
    }
}
