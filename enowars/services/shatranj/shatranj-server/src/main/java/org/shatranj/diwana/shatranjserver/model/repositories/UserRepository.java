package org.shatranj.diwana.shatranjserver.model.repositories;

import org.shatranj.diwana.shatranjserver.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findAllByUsername(String username);
    List<User> findAllByUsernameAndPassword(String username, String password);
    List<User> findAll();
    List<User> findAllByCreatedAtBefore(Date date);
}
