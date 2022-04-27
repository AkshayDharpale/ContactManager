package com.ContactManager.MyContacts.DataBase;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from User u  where u.email = :email")
    public User getUserByUserName(@Param("email") String email);

}
