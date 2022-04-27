package com.ContactManager.MyContacts.UserController;

import com.ContactManager.MyContacts.Configuration.UserDetailServiceImpl;
import com.ContactManager.MyContacts.DataBase.Contact;
import com.ContactManager.MyContacts.DataBase.ContactRepository;
import com.ContactManager.MyContacts.DataBase.User;
import com.ContactManager.MyContacts.DataBase.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserHome {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void AddCommonData(Model model, Principal principal){

        String username = principal.getName();
        User user =  this.userRepository.getUserByUserName(username);
        model.addAttribute("user", user);


    }


    @GetMapping("/Dashboard")
    public String dashboard(Model model){
        System.out.println("you are inside the user dashboard");
        model.addAttribute("title" , "Add Contact");
        model.addAttribute("contact" , new Contact());

        return "normal/Dashboard";
    }


    @RequestMapping("/AddContacts")
    public String dashboard(Model model, Principal principal){

        System.out.println("you are inside the Add contacts");

        model.addAttribute("title" , "Add Contact");
        model.addAttribute("contact" , new Contact());

        return "normal/AddContacts";
    }


    @PostMapping("/ContactRegistration")
    public String ContactRegistration(@Valid @ModelAttribute("contact") Contact contact,
                                      BindingResult bindingResult, Model model, HttpSession session,
                                      Principal principle, @RequestParam("profileImage")MultipartFile multipartFile){

        System.out.println("you are inside the user ContactRegistration");

        model.addAttribute("contact" , contact);

        Date date = new Date();
        long dateLong = date.getTime();

        try{
            if(bindingResult.hasErrors()){
                throw new NullPointerException();
            }

            if(multipartFile.isEmpty()){

                System.out.println("Image is empty");
                contact.setImageUrl("profile.png");


            }else{

                File saveFile= new ClassPathResource("static/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator
                        + dateLong + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

//                FileInputStream fis = (FileInputStream)multipartFile.getInputStream();
//                fis.read();
//                byte[] b = new byte[fis.available()];
//
//                FileOutputStream fos = new FileOutputStream("D:\\desktop  2\\UDEMY\\SpringBoot\\DataBase Images"
//                        + File.separator +dateLong +multipartFile.getOriginalFilename());
//
//                fos.write(b);
//                fis.close();
//                fos.close();

                contact.setImageUrl(dateLong + multipartFile.getOriginalFilename());
                System.out.println("Image is uploaded");

            }

            String username = principle.getName();
            User user = this.userRepository.getUserByUserName(username);

            if(user==null){
                System.out.println("****User object is null*****");
            }

            contact.setUser(user);


            List<Contact> list = new ArrayList<>();
            list.add(contact);

            user.setContacts(list);

            this.userRepository.save(user);
            session.setAttribute("message", new helper("Contact added Successfully", "alert-success"));
            model.addAttribute("contact",new Contact());

            System.out.println("********SUCCESSFUL*************");

            return "normal/AddContacts";

        }catch (NullPointerException e){
            return "normal/AddContacts";

        } catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new helper("Something went wrong","alert-danger"));
            return "normal/AddContacts";

        }
    }


    @GetMapping("/ViewContacts/{id}")
    public String ViewContacts(Model model, Principal principal, @PathVariable("id") int PageId){
        System.out.println("you are inside the View contacts");

        model.addAttribute("title" , "View Contacts");
        model.addAttribute("contact" , new Contact());

        String username = principal.getName();
        User user = this.userRepository.getUserByUserName(username);
        int id = user.getId();
        System.out.println("id: " + id);

       //List<Contact> contacts =  this.contactRepository.findContactByUser(id1);
        //model.addAttribute("contacts", contacts);

        Pageable pageable = PageRequest.of(PageId, 3);

        Page<Contact> ContactList  =  this.contactRepository.findContactByUserPagination(id,pageable);
        for(Contact c : ContactList){
            System.out.println(c);
        }

        int totalPages = ContactList.getTotalPages();

        System.out.println("Total page are " + totalPages);

        model.addAttribute("contacts",ContactList);
        model.addAttribute("CurrentPage", PageId);
        model.addAttribute("TotalPages",totalPages);

        return "normal/ViewContacts";
    }

    @GetMapping("/ContactDetails/{id}")
    public String ContactDetails(Model model, Principal principal, @PathVariable("id") int id){
        System.out.println("you are inside the Contact Details page ");
        System.out.println("Id Received from the URL:- " + id);

        model.addAttribute("title" , "Contact Details");
        Optional<Contact> option = this.contactRepository.findById(id);
        Contact contact = option.get();
        String description = contact.getDescription();
        System.out.println(contact);


       String username =  principal.getName();
       User user = this.userRepository.getUserByUserName(username);
        int UserId = user.getId();

        if(UserId == contact.getUser().getId()){
            model.addAttribute("contact", contact);
            model.addAttribute("description",description);
        }

        return "normal/ContactDetails";
    }

    @GetMapping("/UpdateDetails/{id}")
    public String UpdateDetails(Model model, Principal principal, @PathVariable("id") int id){
        System.out.println("you are inside the UPDATE contacts");
        System.out.println("Id Received from the URL:- " + id);

        model.addAttribute("title" , "Update Contact");

        Optional<Contact> option = this.contactRepository.findById(id);
        Contact contact = option.get();
        System.out.println(contact);
        model.addAttribute("contact",contact);

        return "normal/UpdateDetails";
    }

    @PostMapping("/UpdateDetailsCompletion/{id}")
    public String UpdateDetailsCompletion(@Valid @ModelAttribute("contact") Contact contact,
                                          BindingResult bindingResult, Model model, HttpSession session,
                                          Principal principle, @RequestParam("profileImage")MultipartFile multipartFile,
                                          @PathVariable("id") int id ){
        model.addAttribute("contact" , contact);


        System.out.println("you are inside the Update Details Completion page");
        System.out.println("Id Received from the URL:- " + id);


        //Getting the object from the database
        model.addAttribute("title" , "Update Contact");
        Optional<Contact> option = this.contactRepository.findById(id);
        Contact contact1 = option.get();
        System.out.println(contact1);


        try{
            if(bindingResult.hasErrors()){
                System.out.println("binding result has an error");
                throw new NullPointerException();
            }

            Date date = new Date();
            long dateLong = date.getTime();

            if(multipartFile.isEmpty()){

                System.out.println("Image is empty");


            }else{

                File saveFile= new ClassPathResource("static/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator
                        + dateLong + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);


                contact1.setImageUrl(dateLong + multipartFile.getOriginalFilename());
                System.out.println("Image is uploaded");

            }

            contact1.setName(contact.getName());
            contact1.setPhoneNumber(contact.getPhoneNumber());
            contact1.setEmail(contact.getEmail());
            contact1.setWork(contact.getWork());
            contact1.setNickName(contact.getNickName());
            contact1.setDescription(contact.getDescription());

            System.out.println(contact1);


            //updating the object on contact Detail page


            //handling the error

            this.contactRepository.save(contact1);
            session.setAttribute("message", new helper("Contact Updated Successfully", "alert-success"));
            model.addAttribute("contact",new Contact());



            return "normal/UpdateDetails";

        }catch (NullPointerException e){
            return "normal/UpdateDetails";

        } catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new helper("Duplicate Phone Number","alert-danger"));
            return "normal/UpdateDetails";

        }

    }

    @GetMapping("/DeleteContact/{id}")
    public String DeleteContact(Model model, Principal principal, @PathVariable("id") int id){

        model.addAttribute("title" , "View Contacts");
        Optional<Contact> optional =   this.contactRepository.findById(id);
        Contact contact = optional.get();
        String name = contact.getName();


        String username =  principal.getName();
        User user = this.userRepository.getUserByUserName(username);
        int UserId = user.getId();

        if(UserId == contact.getUser().getId()){
            System.out.println("we are inside the if loop");
            model.addAttribute("name", name);
            //this.contactRepository.deleteById(id);
        }

        return "normal/DeleteResult";
    }


}


