package com.opsmx.isd.register.repositories;

import java.util.List;

import com.opsmx.isd.register.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByEmail(String name);
}
