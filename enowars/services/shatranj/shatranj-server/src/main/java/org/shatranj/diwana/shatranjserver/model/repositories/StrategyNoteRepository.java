package org.shatranj.diwana.shatranjserver.model.repositories;

import org.shatranj.diwana.shatranjserver.model.StrategyNote;
import org.shatranj.diwana.shatranjserver.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface StrategyNoteRepository extends CrudRepository<StrategyNote, String> {
    List<StrategyNote> findAllByUser(User user);
    List<StrategyNote> findAll();

    @Modifying
    void deleteByCreatedAtBefore(Date expiryDate);

    @Modifying
    void deleteAllByUser(User user);
}
