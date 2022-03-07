package com.opsmx.isd.register.repositories;

import com.opsmx.isd.register.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByBusinessEmail(String businessEmail);
    List<User> findAllByCreatedAtBetween(Date createdAtStart, Date createdAtEnd);
}
