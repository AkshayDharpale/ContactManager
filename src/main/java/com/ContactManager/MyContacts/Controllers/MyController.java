package com.ContactManager.MyContacts.Controllers;

import com.ContactManager.MyContacts.DataBase.Contact;
import com.ContactManager.MyContacts.DataBase.User;
import com.ContactManager.MyContacts.DataBase.UserRepository;
import com.ContactManager.MyContacts.Helper.Message;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String HomePage(Model model){
        System.out.println("you are inside the home page");
        model.addAttribute("title", "Home - SmartContactManager");

        return "HomePage";
    }

    @GetMapping("/signup")
    public String SignUp(Model model){

        System.out.println("you are inside the SignUp page");
        model.addAttribute("title", "Register - SmartContactManager");
        model.addAttribute("user" , new User());

        return "SignUp";
    }


    @PostMapping("/register")
    public String DoRegistration(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,
                                 @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                                 Model model, HttpSession session){
        System.out.println("you are inside the DoRegistration page");

        try {
            if (!agreement) {
                System.out.println("You have not agreed the terms and conditions");
                throw new ArithmeticException();

            }

            if (bindingResult.hasErrors()) {
                System.out.println("you are inside the binding results");
                System.out.println(bindingResult);
                model.addAttribute("user", user);
                throw new NullPointerException();

            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("profile.png");

            user.setPassword(this.passwordEncoder.encode(user.getPassword()));

            User result = this.userRepository.save(user);

            System.out.println(user);
            System.out.println("Agreement:-" + agreement);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered",
                    "alert-success"));

            return "SignUp";
        }catch (NullPointerException e) {

            return "SignUp";
        }catch (ArithmeticException e) {
            e.printStackTrace();
            model.addAttribute("user", user);

            session.setAttribute("message",new Message("Terms & Conditions are not accepted" ,
                    "alert-danger"));

            return "SignUp";

        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            model.addAttribute("EmailError", "error");
            session.setAttribute("message",new Message("This Email id is already exists " ,
                    "alert-danger"));


            return "SignUp";
        }
    }

    @GetMapping("/signin")
    public String SignIn(Model model){
        model.addAttribute("title", "Login Page");
        System.out.println("you are inside the SignIn page");

        return "SignIn";
    }




}