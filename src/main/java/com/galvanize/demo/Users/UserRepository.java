package com.galvanize.demo.Users;

import org.springframework.data.repository.CrudRepository;



public interface UserRepository extends CrudRepository<Users, Integer> {

    Users findUsersByEmail(String email);
}
