package org.shatranj.diwana.shatranjserver.services.chess;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.game.GameContext;
import com.github.bhlangonijr.chesslib.move.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shatranj Diwana Shah Baord
 */
public class SDSBoard extends Board {
    private final String START_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/8/4K3 w kq - 0 1";

    private static final Logger logger = LoggerFactory.getLogger(SDSBoard.class);

    public SDSBoard() {
        super();
        loadFromFen(START_BOARD);
    }

    public SDSBoard(GameContext gameContext) {
        super(gameContext, true);
        loadFromFen(START_BOARD);
    }

    @Override
    public boolean isMoveLegal(Move move, boolean fullValidation) {
        return !move.getFrom().equals(move.getTo())
        && switch (getSideToMove()) {
            case BLACK -> isBlackMoveLegal(move);
            case WHITE -> isWhiteMoveLegal(move);
        };
    }

    private boolean isBlackMoveLegal(Move move) {
        logger.info("Starting black validation. Move from: " + move.getFrom() + ", to: " + move.getTo()
                + ". Piece " + (getPiece(move.getFrom()) == null ? "null" : getPiece(move.getFrom())));
        boolean isNormallyLegal = super.isMoveLegal(move, true);

        if (isNormallyLegal) {

            // The Ai always makes legal moves unless its a pawn promotion
            return true;
        } else {
            var fromPiece = getPiece(move.getFrom());
            var toRank = move.getTo().getRank().ordinal();
            var toPiece = getPiece(move.getTo());

            return fromPiece.getPieceType() == PieceType.PAWN && toRank == 0 && toPiece == null;
        }
    }

    private boolean isWhiteMoveLegal(Move move) {
        var fromPiece = getPiece(move.getFrom());

        if (fromPiece == null) {
            logger.info("Invalid white chess move: " + move + ". FromPiece is empty!");
            return false;
        }

        var fromSide = fromPiece.getPieceSide();

        if (fromSide != Side.WHITE) {
            logger.info("Invalid white chess move: " + move + ". Wrong side!");
            return false;
        }

        if (!isKnightMove(move) && !isDiagonalMove(move) && !isRookMove(move)) {
            logger.info("Invalid white chess move: " + move + ". Not knight, diagonal or rook move!");
            return false;
        }

        unsetPiece(fromPiece, move.getFrom());

        if (squareAttackedBy(move.getTo(), Side.BLACK) != 0L) {
            setPiece(fromPiece, move.getFrom());
            logger.info("Invalid white chess move: " + move + ". Piece attacked!");
            return false;
        }
        setPiece(fromPiece, move.getFrom());
        return true;
    }

    private boolean isDiagonalMove(Move move) {
        var fromRank = move.getFrom().getRank().ordinal();
        var fromFile = move.getFrom().getFile().ordinal();

        var toRank = move.getTo().getRank().ordinal();
        var toFile = move.getTo().getFile().ordinal();

        if (Math.abs(fromFile - toFile) != Math.abs(fromRank - toRank)) {
            return false;
        }

        if (isBlackPieceOnQueenPath(fromRank, fromFile, toRank, toFile)) return false;

        return true;
    }

    private boolean isKnightMove(Move move) {
        var fromRank = move.getFrom().getRank().ordinal();
        var fromFile = move.getFrom().getFile().ordinal();

        var toRank = move.getTo().getRank().ordinal();
        var toFile = move.getTo().getFile().ordinal();
        return Math.abs(toRank - fromRank) == 2 && Math.abs(toFile - fromFile) == 1
                || Math.abs(toRank - fromRank) == 1 && Math.abs(toFile - fromFile) == 2;
    }

    private boolean isRookMove(Move move) {
        var fromRank = move.getFrom().getRank().ordinal();
        var fromFile = move.getFrom().getFile().ordinal();

        var toRank = move.getTo().getRank().ordinal();
        var toFile = move.getTo().getFile().ordinal();

        if (notEvenRookPath(fromRank, fromFile, toRank, toFile)) {
            return false;
        }

        if (isBlackPieceOnRookPath(fromRank, fromFile, toRank, toFile)) {
            return false;
        }

        return true;
    }

    private boolean notEvenRookPath(int fromRank, int fromFile, int toRank, int toFile) {
        return (Math.abs(toRank - fromRank) != 0 || Math.abs(toFile - fromFile) <= 0)
                && (Math.abs(toRank - fromRank) <= 0 || Math.abs(toFile - fromFile) != 0);
    }

    private boolean isBlackPieceOnQueenPath(int fromRank, int fromFile, int toRank, int toFile) {
        for (int i = fromRank; i != toRank; i += (toRank - fromRank) / Math.abs(toRank - fromRank)) {
            for (int j = fromFile; j != toFile; j += (toFile - fromFile) / Math.abs(toFile - fromFile)) {
                if (i != j) {
                    continue;
                }
                if (blackPieceOnSquare(i, (char) j)) return true;
            }
        }
        return false;
    }

    private boolean isBlackPieceOnRookPath(int fromRank, int fromFile, int toRank, int toFile) {
        if (fromRank == toRank) {
            for (int j = fromFile; j != toFile; j += (toFile - fromFile) / Math.abs(toFile - fromFile)) {
                if (blackPieceOnSquare(toRank, (char) j)) return true;
            }
        } else {
            for (int i = fromRank; i != toRank; i += (toRank - fromRank) / Math.abs(toRank - fromRank)) {
                if (blackPieceOnSquare(i, (char) toFile)) return true;
            }
        }
        return false;
    }

    private boolean blackPieceOnSquare(int i, char j) {
        var square = Square.encode(
                Rank.fromValue("RANK_" + (i + 1)),
                File.fromValue("FILE_" + Character.toString(j + 65)));
        var piece = getPiece(square);
        if (piece != null && piece.getPieceSide() == Side.BLACK) {
            return true;
        }
        return false;
    }
}
