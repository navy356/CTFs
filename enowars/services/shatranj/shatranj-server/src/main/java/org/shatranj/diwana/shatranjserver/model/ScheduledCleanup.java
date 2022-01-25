package org.shatranj.diwana.shatranjserver.model;

import org.shatranj.diwana.shatranjserver.model.repositories.GameRepository;
import org.shatranj.diwana.shatranjserver.model.repositories.MoveRepository;
import org.shatranj.diwana.shatranjserver.model.repositories.StrategyNoteRepository;
import org.shatranj.diwana.shatranjserver.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class ScheduledCleanup {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledCleanup.class);

    private GameRepository gameRepository;
    private UserRepository userRepository;
    private StrategyNoteRepository strategyNoteRepository;
    private MoveRepository moveRepository;

    public ScheduledCleanup(final GameRepository gameRepository,
                            final UserRepository userRepository,
                            final StrategyNoteRepository strategyNoteRepository,
                            final MoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.strategyNoteRepository = strategyNoteRepository;
        this.moveRepository = moveRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void cleanUpDatabase() {
        var date = Date.from(Instant.now().minus(Duration.ofMinutes(30)));
        logger.info("Cleaning up all entries older than " + date + ".");

        strategyNoteRepository.deleteByCreatedAtBefore(date);
        logger.info("Cleaned up notes.");

        var games = gameRepository.findAllByCreatedAtBefore(date);
        games.forEach(game -> {
            moveRepository.deleteAllByGameId(game.getId());
        });
        gameRepository.deleteAll(games);
        logger.info("Cleaned up games including their moves.");

        var users = userRepository.findAllByCreatedAtBefore(date);
        users.forEach(user -> {
            strategyNoteRepository.deleteAllByUser(user);
            var userGames = gameRepository.findAllByUser(user);
            userGames.forEach(game -> moveRepository.deleteAllByGameId(game.getId()));
            gameRepository.deleteAll(userGames);
        });
        userRepository.deleteAll(users);
        logger.info("Cleaned up users including their games, strategy notes and moves.");
    }
}
