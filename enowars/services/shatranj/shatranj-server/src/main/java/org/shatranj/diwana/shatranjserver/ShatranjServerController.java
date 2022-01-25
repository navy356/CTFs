package org.shatranj.diwana.shatranjserver;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.shatranj.diwana.shatranjserver.dto.*;
import org.shatranj.diwana.shatranjserver.model.Game;
import org.shatranj.diwana.shatranjserver.model.StrategyNote;
import org.shatranj.diwana.shatranjserver.model.User;
import org.shatranj.diwana.shatranjserver.services.ManagerService;
import org.shatranj.diwana.shatranjserver.services.chess.ChessService;
import org.shatranj.diwana.shatranjserver.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ShatranjServerController
 */
@RestController
@Validated
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ShatranjServerController {
    private final UserService userService;
    private final ChessService chessService;
    private final ManagerService managerService;
    private final Logger logger = LoggerFactory.getLogger(ShatranjServerController.class);

    @Autowired
    public ShatranjServerController(UserService userService,
                                    ChessService chessService,
                                    ManagerService managerService) {
        this.userService = userService;
        this.chessService = chessService;
        this.managerService = managerService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody UserRegisterDTO userDTO) {
        logger.info("Trying to register user " + userDTO.getUsername() + ".");
        userService.registerUser(userDTO.getUsername(), userDTO.getPassword());
        return Map.ofEntries(Map.entry("message", "Successfully registered User " + userDTO.getUsername() + "."));
    }

    private User getUser() {
        var context = SecurityContextHolder.getContext();
        var id = (Integer) context.getAuthentication().getPrincipal();
        return userService.getUserFromId(id).orElseThrow();
    }

    private String removeQuotes(String input, boolean checkDouble) {
        if (input.length() > 2 && !checkDouble) {
            return input.replaceAll("^\"|\"$", "");
        } else if (input.length() > 4 && input.matches("^\"\".*\"\"$")) {
            return input.replaceAll("^\"|\"$", "");
        } else {
            return input;
        }
    }

    @PostMapping("/initialize")
    public Map<String, String> init(@RequestBody String name) {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + "is trying to initialize a game .");
        var game = chessService.initializeGame(user, removeQuotes(name, false));

        return Map.ofEntries(Map.entry("board_state", game.getBoardState()));
    }

    @PostMapping("/move")
    public Object move(@RequestBody String message) throws JsonProcessingException {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + " is trying to perform a move.");

        message = removeQuotes(message.replace("\\\"", "\""), true);

        if (!message.contains("\"move\": ")) {
            return Map.ofEntries(
                    Map.entry("message", "Not a proper chess move"));
        }

        var input = "{\n" + message + ","
                + "\n\"@class\": \"" + MoveReqDTO.class.getCanonicalName() + "\""
                + "\n" + "}";

        return managerService.handleDTOInput(input, user);
    }

    @RequestMapping("/pastgames")
    public List<Game> getPastGames() {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + "is trying to get his past games.");
        return chessService.getGames(user);
    }

    @RequestMapping("/replay/{id}")
    public List<MoveDTO> getGameReplay(@PathVariable(value="id") String id) {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + "is trying to get a replay of the game with the ID " + id + ".");
        return chessService.getReplay(user, Integer.parseInt(id));
    }

    @PostMapping("/strategynote")
    public Object addStrategyNote(@RequestBody String message) throws JsonProcessingException {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + "is trying to add a strategy note.");

        var json = "{\n" +
                "\"@class\": \"" + StrategyNoteDTO.class.getCanonicalName() + "\",\n" +
                "\"message\": \"" + removeQuotes(message, false).replace("\"", "\\\"") + "\"\n" +
                "}";

        var note = managerService.handleDTOInput(json, user);

        return note == null ? Map.ofEntries(Map.entry("message", "Could not post the strategy note")) : (StrategyNote) note;
    }

    @RequestMapping("/strategynote/{id}")
    public StrategyNoteDTO readStrategyNote(@PathVariable(value="id") String id) {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + "is trying to get the note with id " + id + ".");
        var note = chessService.getStrategyNote(id);
        if (note != null && note.getUser().getId().equals(user.getId())) {
            var stratNote = new StrategyNoteDTO();
            stratNote.setMessage(note.getMessage());
            return stratNote;
        }

        return null;
    }

    @RequestMapping("/strategynotes")
    public List<StrategyNoteDTO> getStrategyNotes() {
        var user = this.getUser();
        logger.info("User " + user.getUsername() + "is trying to retrieve all their strategy notes.");
        return chessService
                .getStrategyNotes(user)
                .stream()
                .map(note -> new StrategyNoteDTO(note.getMessage()))
                .collect(Collectors.toList());
    }

    @RequestMapping("/login")
    public Map<String, String> login() {
        return Map.ofEntries(Map.entry("message", "Success!"));
    }
}