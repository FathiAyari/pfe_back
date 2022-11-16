package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

   Optional<User> findByEmail(String email);
  List<User> findAllByNameOrLastName(String name, String lastName);

  default List<User> findByName(String name) {
    return findAllByNameOrLastName(name, name);
  }
}

