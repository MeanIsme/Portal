package com.example.portal.controllers;

import com.example.portal.models.Classes;
import com.example.portal.models.User;
import com.example.portal.payload.request.RegisterClassesRequest;
import com.example.portal.payload.response.MessageResponse;
import com.example.portal.repository.ClassesRepository;
import com.example.portal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
            List<String> classID = new ArrayList<>();
            if(teacher.getClassesID() != null)
                classID = teacher.getClassesID();
            classID.add(classes.getId());
            teacher.setClassesID(classID);
            userRepository.save(teacher);
            return ResponseEntity.ok(classes);
        }
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
            List<String> classID = new ArrayList<>();
            if(teacher.getClassesID() != null)
                classID = teacher.getClassesID();
            classID.add(classes.getId());
            teacher.setClassesID(classID);
            userRepository.save(teacher);
            return ResponseEntity.ok(classes);
        }
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
                    List<String> classesID = new ArrayList<>();
                    if(user.getClassesID()!= null)
                        classesID = user.getClassesID();
                    classesID.add(classes.getId());
                    user.setClassesID(classesID);
                    userRepository.save(user);
                    List<String> studentId = new ArrayList<>();
                    if(classes.getIdStudent()!= null)
                        studentId = classes.getIdStudent();
                    studentId.add(user.getId());
                    classes.setIdStudent(studentId);
                    classRepository.save(classes);
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Duplicate calendar"));
                }
            }
            else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Classes is full"));
            }
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not found classes"));
        }
    }
    @PostMapping("/unregister")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') ")
    public ResponseEntity<?> unregisterSubject(@Valid @RequestBody RegisterClassesRequest request) {
        Classes classes = classRepository.getClassesById(request.getIdClasses());
        User user = userRepository.getUserById(request.getIdStudent());
        if(classes != null) {
            if(classes.getIdStudent().removeIf(t -> t.equals(user.getId())) && user.getClassesID().removeIf(c -> c.equals(classes.getId()))){
                classes.setQuantity(classes.getQuantity()+1);
                userRepository.save(user);
                classRepository.save(classes);
                return ResponseEntity.ok(user);
            }

            else
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error"));
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not found classes"));
        }

    }
    @GetMapping("/calendar")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or  hasRole('TEACHER') ")
    public ResponseEntity<?> calendar(@RequestParam String id) {
        User user = userRepository.getUserById(id);
        if(user == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not found user"));
        else{
            List<Classes> calendar = new ArrayList<>();
            if(user.getClassesID()!=null)
            {
                for (String classID: user.getClassesID()) {
                    calendar.add(classRepository.getClassesById(classID));
                }
            }
            return ResponseEntity.ok(calendar);
        }

    }

    public Boolean checkDuplicate(User user, Classes classes){
        if(user.getClassesID() !=null)
            for (String id: user.getClassesID()) {
                Classes classesCheck = classRepository.getClassesById(id);
                if(classes.getTime() == classesCheck.getTime())
                    if(classes.getTimeStart()<=classesCheck.getTimeEnd()&&classes.getTimeEnd()>=classesCheck.getTimeStart())
                        return false;
            }
        return true;
    }
}
