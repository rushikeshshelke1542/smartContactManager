package com.rushikesh.smartContactManager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rushikesh.smartContactManager.dao.ContactsRepository;
import com.rushikesh.smartContactManager.dao.UserRepository;
import com.rushikesh.smartContactManager.entities.Contact;
import com.rushikesh.smartContactManager.entities.User;

@RestController
public class SearchController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactsRepository contactsRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){

        User user = this.userRepository.getUserByEmail(principal.getName());

        List<Contact> contacts = this.contactsRepository.findByNameContainingAndUserCustom(query, user);
        
    return ResponseEntity.ok(contacts);}
}
