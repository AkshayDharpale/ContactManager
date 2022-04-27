package com.ContactManager.MyContacts.Configuration;

import com.ContactManager.MyContacts.DataBase.User;
import com.ContactManager.MyContacts.DataBase.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user =  this.userRepository.getUserByUserName(username);

       if(user== null){
           throw new UsernameNotFoundException("User Not found !!");
       }

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);

        return userDetailsImpl;
    }
}
