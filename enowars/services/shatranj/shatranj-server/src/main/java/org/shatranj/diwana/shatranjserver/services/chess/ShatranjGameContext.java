package org.shatranj.diwana.shatranjserver.services.chess;

import com.github.bhlangonijr.chesslib.game.GameContext;
import com.github.bhlangonijr.chesslib.move.Move;

public class ShatranjGameContext extends GameContext {
    @Override
    public boolean isCastleMove(Move move) {
        return move.equals(getBlackoo()) ||
                move.equals(getBlackooo());
    }
}
