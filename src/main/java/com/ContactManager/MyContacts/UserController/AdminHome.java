package com.ContactManager.MyContacts.UserController;

import com.ContactManager.MyContacts.DataBase.Contact;
import com.ContactManager.MyContacts.DataBase.ContactRepository;
import com.ContactManager.MyContacts.DataBase.User;
import com.ContactManager.MyContacts.DataBase.UserRepository;
import com.ContactManager.MyContacts.Helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/admin")
public class AdminHome {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @ModelAttribute
    public void AddCommonData(Model model, Principal principal){

        String username = principal.getName();
        User user =  this.userRepository.getUserByUserName(username);
        model.addAttribute("user", user);


    }


    @GetMapping("/YourProfile")
    public String YourProfile(Model model, Principal principal){
        System.out.println("you are inside the admin profile page");
        model.addAttribute("title", "Admin-Profile");

        String username =  principal.getName();

        User FetchedUser = this.userRepository.getUserByUserName(username);
        List<Contact> list =    FetchedUser.getContacts();
        int NoOfContactsSaved = list.size();
        System.out.println("Number of contacts saved are:- " + NoOfContactsSaved);

        model.addAttribute("TotalContacts",NoOfContactsSaved);
        model.addAttribute("FetchedUser", FetchedUser);


        return "normal/admin/YourProfile";
    }


    @GetMapping("/UpdateYourDetails")
    public String UpdateYourDetails(Model model, Principal principal){
        System.out.println("you are inside the Updating Admin Details page");
        model.addAttribute("title", "Admin-Update Details");

        String username =  principal.getName();

        User User = this.userRepository.getUserByUserName(username);

        model.addAttribute("User", User);


        return "normal/admin/UpdateYourDetails";
    }

    @PostMapping("/CompleteUpdateYourDetails")
    public String CompleteUpdateYourDetails(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                                            Model model, Principal principal, HttpSession session,
                                            @RequestParam("profileImage")MultipartFile multipartFile){

        System.out.println("you are inside the CompleteUpdateYourDetails page");
        model.addAttribute("title", "Admin-Profile");

        try {

            if (bindingResult.hasErrors()) {
                System.out.println("catched the erorr");
                model.addAttribute("user", user);
                throw new NullPointerException();
            }

            String username = principal.getName();
            User FetchedUser = this.userRepository.getUserByUserName(username);


            FetchedUser.setEmail(user.getEmail());
            FetchedUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
            FetchedUser.setDescription(user.getDescription());
            FetchedUser.setName(user.getName());


            //image setting
            Date date = new Date();
            long dateLong = date.getTime();

            if(multipartFile.isEmpty()){

                System.out.println("Image is empty");

            }else{

                File saveFile= new ClassPathResource("static/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator
                        + dateLong + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);


                FetchedUser.setImageUrl(dateLong + multipartFile.getOriginalFilename());
                System.out.println("Image is uploaded");

            }


            this.userRepository.save(FetchedUser);
            model.addAttribute("user",FetchedUser);


            return "normal/admin/ProfileUpdateSuccessful";

        }catch (NullPointerException e) {

            return "normal/admin/UpdateYourDetails";

        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            model.addAttribute("EmailError", "error");
            session.setAttribute("message",new Message("Email Id is already Exist",
                    "alert-danger"));


            return "normal/admin/UpdateYourDetails";
        }

    }



    @GetMapping("/ProfileRemoved")
    public String ProfileRemoved(Model model, Principal principal){
        System.out.println("you are inside the Admin Profile Removed page");
        model.addAttribute("title", "Admin_Removed");

        String username = principal.getName();
        User user = this.userRepository.getUserByUserName(username);

        this.userRepository.delete(user);


        return "normal/admin/ProfileRemoved";
    }



}
