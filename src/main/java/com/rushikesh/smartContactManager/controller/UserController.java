package com.rushikesh.smartContactManager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rushikesh.smartContactManager.dao.ContactsRepository;
import com.rushikesh.smartContactManager.dao.UserRepository;
import com.rushikesh.smartContactManager.entities.Contact;
import com.rushikesh.smartContactManager.entities.User;
import com.rushikesh.smartContactManager.helper.message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void commonData(Model model, Principal principal){

        //get user and add name on navbar
        String name = principal.getName();
        User user = userRepository.getUserByEmail(name);

        model.addAttribute("user", user);

    }
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        
        
        return "normal/user_dashboard";
    }

    @RequestMapping("/addContact")
    public String addContact(Model model) {

        model.addAttribute("title", "Add contact - Smart Contact Manager");
        model.addAttribute("contact",new Contact()); 
        
        return "normal/add_contact";
    }

    @PostMapping("/process-contact")
    public String saveContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file ,Principal principal, HttpSession session) {

        try {
            //get user and add contact in that user
        String name = principal.getName();

        User user = userRepository.getUserByEmail(name);

        contact.setUser(user);

        if(file.isEmpty()){

            contact.setImage("contact.png");
        }
        else{

           contact.setImage(file.getOriginalFilename());

           File saveFile = new ClassPathResource("static/img").getFile();

           Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

           Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

       

        user.getContacts().add(contact);

        this.userRepository.save(user);

        session.setAttribute("message", new message("Contact Added Successfully", "alert-success"));



        } catch (Exception e) {
            
            e.printStackTrace();
            session.setAttribute("message", new message("Something went wrong!!", "alert-danger"));
        }
        
        return "normal/add_contact";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal){

        model.addAttribute("title", "Show contacts - Smart Contact Manager");

        String name = principal.getName();

        User user = this.userRepository.getUserByEmail(name);

        Pageable pageable = PageRequest.of(page, 5);

        Page<Contact> contacts =  this.contactsRepository.findContactsByUser(user.getId(), pageable);
        
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage", contacts.getTotalPages());
        return "normal/show_contacts";
    } 

    @GetMapping("/{cID}/contacts")
    public String contactDetails(@PathVariable("cID") Integer cID, Model model, Principal principal){

        Contact contact = this.contactsRepository.getReferenceById(cID);

        String name = principal.getName();

        User user = this.userRepository.getUserByEmail(name);

        //check that contact user and logged in user ID is same 
        //so that user cannot view the page with url hit and trail
        if(user.getId()==contact.getUser().getId()){

            model.addAttribute("contact", contact);
        }

       
        
        return "normal/contact_details";
    } 

    //delete contact

    @GetMapping("/delete/{cID}")
    public String deleteContact(@PathVariable("cID") Integer cID, Model model,  Principal principal, HttpSession session){

        Optional<Contact> contactOptional = this.contactsRepository.findById(cID);
        Contact contact = contactOptional.get();

        String name = principal.getName();

        User user = this.userRepository.getUserByEmail(name);

        //check that contact user and logged in user ID is same 
        //so that user cannot view the page with url hit and trail
        if(user.getId()==contact.getUser().getId()){

            this.contactsRepository.delete(contact);
            session.setAttribute("message", new message("Contact Deleted Successfully", "alert-success"));

        }

    return "redirect:/user/show-contacts/0";
}

//open update form

@PostMapping("/update-contact/{cId}")
public String openUpdateContacts(@PathVariable("cId") Integer cId, Model model){
    
    model.addAttribute("title", "Update contacts - Smart Contact Manager");

    Contact contact = this.contactsRepository.findById(cId).get();

    model.addAttribute("contact", contact);

    return "normal/update_contacts";
}

//send updated data to DB

@PostMapping("/update-contact")
public String updateContacts(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session, Principal principal ){
    
    try {

        Contact oldcontact = this.contactsRepository.getReferenceById(contact.getCId());
        
        if(file.isEmpty()){

            contact.setImage(oldcontact.getImage());
        }
        else{

            //delete old photo
            File deleteFile = new ClassPathResource ("static/img"). getFile();
            File file1 = new File(deleteFile, oldcontact.getImage()); 
            file1.delete();


            //update new photo
            File saveFile = new ClassPathResource("static/img").getFile();
 
            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
 
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            contact.setImage(file.getOriginalFilename());
        }

        User user = this.userRepository.getUserByEmail(principal.getName());

        contact.setUser(user);

        this.contactsRepository.save(contact);

        session.setAttribute("message", new message("Contact Updated Successfully", "alert-success"));



    } catch (Exception e) {
        // TODO: handle exception

        e.printStackTrace();

        session.setAttribute("message", new message("Something Went Wrong", "alert-danger"));

    }
  

    return "redirect:/user/"+ contact.getCId() +"/contacts";
}

    @GetMapping("/profile")
    public String yourProfile(Model model){

        model.addAttribute("title", "Your profile - Smart Contact Manager");

        return "normal/profile";
    }


    //settings handler

    @GetMapping("/settings")
    public String setting(){

        return "normal/setting";
    }


    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session){

        User user = this.userRepository.getUserByEmail(principal.getName());

        if(bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){

            user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(user);
            

        }
        else{

            session.setAttribute("message", new message("Please Enter Correct Old Password","alert-danger"));
            return "redirect:/user/settings";

        }

        return "redirect:/logout";
    }



}

