package org.shatranj.diwana.shatranjserver.model.repositories;

import org.shatranj.diwana.shatranjserver.model.Game;
import org.shatranj.diwana.shatranjserver.model.User;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Integer> {
    List<Game> findAllByUserAndActive(@NotNull User user, boolean active);
    List<Game> findAllByActiveAndUserUsername(boolean active, String username);
    Optional<Game> findById(@NotNull Integer id);
    List<Game> findAllByCreatedAtBefore(Date date);
    List<Game> findAllByUser(User user);
}
