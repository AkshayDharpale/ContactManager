package com.ContactManager.MyContacts.DataBase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CONTACTS")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Please Enter the Name")
    @Size(min = 2, max = 25, message = "Name size is Not Acceptable")
    private String name;

    @NotBlank(message = "Please Enter the Nick Name")
    private String nickName;


    @NotBlank(message = "Please Enter the Mobile Number")
    @Size(max = 10, message = "Mobile Number should not be greater than 10 Digits")
    private String phoneNumber;

    @NotBlank(message = "Please Enter the Email-Id")
    private String email;

    @NotBlank(message = "Please Enter the Designation")
    private String work;

    @Size(max = 50, message = "Description should not be greater than 50 words")
    private String description;

    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", work='" + work + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", user=" + user +
                '}';
    }
}
