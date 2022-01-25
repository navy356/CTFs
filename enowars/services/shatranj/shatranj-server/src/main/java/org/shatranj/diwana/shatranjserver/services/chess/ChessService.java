package org.shatranj.diwana.shatranjserver.services.chess;

import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import org.shatranj.diwana.shatranjserver.dto.GameDTO;
import org.shatranj.diwana.shatranjserver.dto.MoveDTO;
import org.shatranj.diwana.shatranjserver.model.Game;
import org.shatranj.diwana.shatranjserver.model.MoveSet;
import org.shatranj.diwana.shatranjserver.model.StrategyNote;
import org.shatranj.diwana.shatranjserver.model.User;
import org.shatranj.diwana.shatranjserver.model.repositories.GameRepository;
import org.shatranj.diwana.shatranjserver.model.repositories.MoveRepository;
import org.shatranj.diwana.shatranjserver.model.repositories.StrategyNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {
    private GameRepository gameRepository;
    private MoveRepository moveRepository;
    private StrategyNoteRepository strategyNoteRepository;
    private AiService aiService;

    @Autowired
    public ChessService(GameRepository gameRepository,
                        MoveRepository moveRepository,
                        StrategyNoteRepository strategyNoteRepository,
                        AiService aiService) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
        this.strategyNoteRepository = strategyNoteRepository;
        this.aiService = aiService;
    }

    public GameDTO initializeGame(User user, String name) {
        var games = gameRepository.findAllByUserAndActive(user, true);

        games.forEach(game -> {
            game.setActive(false);
            gameRepository.save(game);
        });

        var board = new SDSBoard(new ShatranjGameContext());
        var game = new Game(user, true, board.getFen(), name);
        var savedGame = gameRepository.save(game);

        return new GameDTO(savedGame.getBoardState(), null, name);
    }

    private MoveSet getLastMove(Game game, List<MoveSet> moves) {
        moves.sort(Comparator.comparingInt(MoveSet::getId));
        return moves.size() > 0 ? moves.get(moves.size() - 1) : null;
    }

    private boolean didMoveRooks(Game game, List<MoveSet> moves) {
        return moves.stream().filter(move -> move.getBlackMove().equals(aiService.getSignificantRookMove())).count() > 1;
    }

    private List<Move> generateWhiteLegalMoves(SDSBoard board) {
        var side = board.getSideToMove();

        if (side != Side.WHITE) {
            return List.of();
        }

        var maharajahSq = board.getKingSquare(Side.WHITE);

        List<Move> moveList = new java.util.ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var move = new Move(maharajahSq,
                        Square.encode(
                                Rank.fromValue("RANK_" + (i + 1)),
                                File.fromValue("FILE_" + Character.toString((char)j + 65))));

                if (board.isMoveLegal(move, true)) {
                    moveList.add(move);
                }
            }
        }

        return moveList;
    }

    public Map<String, String> makeMove(User user, String move) {
        var games = gameRepository.findAllByUserAndActive(user, true);

        if (games.isEmpty()) {
            return Map.ofEntries(Map.entry("message", "User does not have any active games."));
        }

        var game = games.get(0);

        var board = new SDSBoard(new ShatranjGameContext());
        board.loadFromFen(game.getBoardState());

        var chessMove = new Move(move, board.getSideToMove());

        if (board.doMove(chessMove, true)) {
            var moves = moveRepository.findAllByGameId(game.getId());

            var isMahOnA1 = move.substring(2).contains("a1");
            var lastMoveSet = getLastMove(game, moves);
            var lastBlackMove = lastMoveSet != null ? lastMoveSet.getBlackMove() : "";

            var aiMove = aiService.getAiMove(isMahOnA1, lastBlackMove, didMoveRooks(game, moves));
            if (aiMove.equals("Error")) {
                return Map.ofEntries(Map.entry("message", "Server error. The Ai move failed"));
            }

            var aiBoardMove =  aiMove.equals("O-O") ? board.getContext().getBlackoo()
                    : aiMove.equals("O-O-O") ? board.getContext().getBlackooo()
                    : new Move(aiMove, Side.BLACK);

            if (!board.doMove(aiBoardMove, false)) {
                return Map.ofEntries(Map.entry("message", "Ai Move was invalid."));
            }

            var moveSet = new MoveSet();
            moveSet.setBoardStateBefore(game.getBoardState());
            moveSet.setGame(game);
            moveSet.setWhiteMove(move);
            moveSet.setBlackMove(aiMove);
            moveRepository.save(moveSet);

            game.setBoardState(board.getFen());

            if (generateWhiteLegalMoves(board).isEmpty()) {
                game.setActive(false);
                game.setWinner(Side.BLACK);
                gameRepository.save(game);
                return Map.ofEntries(
                        Map.entry("message", move + " was successfully executed. Sadly you got mated by the almighty AI." +
                        " Better luck next time!"),
                        Map.entry("board_state", board.getFen())
                );
            } else {
                System.out.println("Legal moves: " + generateWhiteLegalMoves(board));
            }
            gameRepository.save(game);

            return Map.ofEntries(
                    Map.entry("message", move + " was successfully executed."),
                    Map.entry("board_state", board.getFen())
            );
        }

        return Map.ofEntries(Map.entry("message", "Move was invalid."));
    }

    public List<Game> getGames(User user) {
        return gameRepository.findAllByActiveAndUserUsername(false, user.getUsername());
    }

    public List<MoveDTO> getReplay(User user, Integer id) {
        var moves = moveRepository.findAllByGameId(id);

        return moves
            .stream()
            .map(move -> new MoveDTO(move.getWhiteMove(), move.getBlackMove(), move.getBoardStateBefore()))
            .collect(Collectors.toList());
    }

    public StrategyNote saveStrategyNote(User user, String message) {
        var strategyNote = new StrategyNote(user, message);
        return strategyNoteRepository.save(strategyNote);
    }

    public StrategyNote getStrategyNote(String id) {
        var note = strategyNoteRepository.findById(id);
        return note.orElse(null);
    }

    public List<StrategyNote> getStrategyNotes(User user) {
        var notes = strategyNoteRepository.findAllByUser(user);
        return notes;
    }

    public List<String> getStrategyNoteIds(User user) {
        var noteIds = strategyNoteRepository
                .findAllByUser(user)
                .stream()
                .map(StrategyNote::getId)
                .collect(Collectors.toList());
        return noteIds;
    }
}
