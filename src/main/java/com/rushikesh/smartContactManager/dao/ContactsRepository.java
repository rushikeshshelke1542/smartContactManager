package com.rushikesh.smartContactManager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rushikesh.smartContactManager.entities.Contact;
import com.rushikesh.smartContactManager.entities.User;

public interface ContactsRepository extends JpaRepository<Contact, Integer>  {
    

    @Query("from Contact as c where c.user.id =:userId")
    public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);

    // search
    //public List<Contact> findByNameContainingAndUser(String name, User user);

    @Query("SELECT c FROM Contact c WHERE c.name LIKE %:name% AND c.user = :user")
    List<Contact> findByNameContainingAndUserCustom(String name, User user);    
}
