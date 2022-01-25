package org.shatranj.diwana.shatranjserver.services.chess;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {
    final private List<String> baseMoves = List.of(
            "d7d5",
            "b8c6",
            "d8d6",
            "e7e5",
            "g8f6",
            "a7a5",
            "a8a6",
            "a6b6",
            "c8g4",
            "e5e4",
            "d6e5",
            "f8e7",
            "O-O",
            "b6b2",
            "f8a8",
            "a8a6",
            "a6b6",
            "b6b3",
            "h7h5",
            "g7g5",
            "f6h7",
            "e5d4"
    );

    final private List<String> caseA1 = List.of(
            "b2b1",
            "b3b2"
    );

    final private String caseNotA1 = "d4d1";

    public String getAiMove(boolean isMahOnA1, String lastBlackMove, boolean didMoveRook) {
        var idx = baseMoves.indexOf(lastBlackMove);
        if (idx == 21) {
            if (isMahOnA1) {
                return caseA1.get(0);
            } else {
                return caseNotA1;
            }
        } else if (idx == -1) {
            if (lastBlackMove.equals(caseA1.get(0))) {
                return caseA1.get(1);
            } else if (lastBlackMove.equals("")) {
                return baseMoves.get(0);
            } else {
                return "Error";
            }
        } else {
            if (didMoveRook) {
                return baseMoves.get(baseMoves.subList(idx + 1, baseMoves.size() - 1).indexOf(lastBlackMove) + idx + 2);
            } else {
                return baseMoves.get(idx + 1);
            }
        }
    };

    public String getSignificantRookMove() {
        return "a6b6";
    }
}
