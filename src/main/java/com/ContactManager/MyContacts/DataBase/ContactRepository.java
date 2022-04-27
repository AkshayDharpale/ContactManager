package com.ContactManager.MyContacts.DataBase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;

@Service
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    //We are going to implement the method for pagination
    @Query( "from Contact as d where d.user.id=:userID")
    public List<Contact> findContactByUser(@Param("userID") int userID);

    @Query( "from Contact as d where d.user.id=:userID")
    public Page<Contact> findContactByUserPagination(@Param("userID") int userID, Pageable pageable);

    public List<Contact> findByNameContainingAndUser(String name, User user);




}
