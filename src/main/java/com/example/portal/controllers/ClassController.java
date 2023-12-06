package com.example.portal.controllers;

import com.example.portal.models.Classes;
import com.example.portal.models.User;
import com.example.portal.payload.request.RegisterClassesRequest;
import com.example.portal.payload.response.MessageResponse;
import com.example.portal.repository.ClassesRepository;
import com.example.portal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/class")
public class ClassController {
    @Autowired
    ClassesRepository classRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSubject(@Valid @RequestBody Classes classes){
        User teacher = userRepository.getUserById(classes.getIdTeacher());
        if(checkDuplicate(teacher, classes)){
            classRepository.save(classes);
            return ResponseEntity.ok(classes);}
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Teacher duplicate calendar"));
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSubject(@Valid @RequestBody Classes classes){
        User teacher = userRepository.getUserById(classes.getIdTeacher());
        if(checkDuplicate(teacher, classes)){
            classRepository.save(classes);
            return ResponseEntity.ok(classes);}
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Teacher duplicate calendar"));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSubject(@Valid @RequestBody Classes classes){
        classRepository.deleteById(classes.getId());
        return ResponseEntity.ok(classes);
    }
    @PostMapping("/register")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') ")
    public ResponseEntity<?> registerSubject(@Valid @RequestBody RegisterClassesRequest request){
        Classes classes = classRepository.getClassesById(request.getIdClasses());
        User user = userRepository.getUserById(request.getIdStudent());
        if(classes != null)
        {
            if (classes.getQuantity() > 0) {
                if (checkDuplicate(user, classes)) {
                    classes.setQuantity(classes.getQuantity() - 1);
                    user.getClassesID().add(classes.getId());
                    userRepository.save(user);
                    classes.getIdStudent().add(user.getId());
                    classRepository.save(classes);
                } else {
                    ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Duplicate calendar"));
                }
            }
            else {
                ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Classes is full"));
            }
        }
        else {
            ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not found classes"));
        }
        return ResponseEntity
                .badRequest().body(new MessageResponse("Error: "));
    }
    public Boolean checkDuplicate(User user, Classes classes){
        for (String id: user.getClassesID()) {
            Classes classesCheck = classRepository.getClassesById(id);
            if(classes.getTime() == classesCheck.getTime())
                if(classes.getTimeStart()<=classesCheck.getTimeEnd()&&classes.getTimeEnd()>=classesCheck.getTimeStart())
                    return false;
        }
        return true;
    }
}
