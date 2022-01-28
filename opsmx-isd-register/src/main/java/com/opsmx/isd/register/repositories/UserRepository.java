package com.opsmx.isd.register.repositories;

import com.opsmx.isd.register.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByBusinessEmail(String businessEmail);
}
