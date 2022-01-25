package org.shatranj.diwana.shatranjserver.model.repositories;

import org.shatranj.diwana.shatranjserver.model.MoveSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MoveRepository extends CrudRepository<MoveSet, Integer> {
    List<MoveSet> findAllByGameId(Integer id);

    @Modifying
    void deleteAllByGameId(Integer id);
}
