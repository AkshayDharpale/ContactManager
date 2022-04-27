package com.ContactManager.MyContacts.UserController;

import com.ContactManager.MyContacts.DataBase.Contact;
import com.ContactManager.MyContacts.DataBase.ContactRepository;
import com.ContactManager.MyContacts.DataBase.User;
import com.ContactManager.MyContacts.DataBase.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
        System.out.println("you are inside the search:- " + query );
        System.out.println(query);
        String username = principal.getName();
        User user =  this.userRepository.getUserByUserName(username);

       List<Contact> contactList =  this.contactRepository.findByNameContainingAndUser(query, user);
       for(Contact c : contactList){
           System.out.println(c);
       }

        return ResponseEntity.ok(contactList);
    }
}
