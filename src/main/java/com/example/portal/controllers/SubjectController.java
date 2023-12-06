package com.example.portal.controllers;

import com.example.portal.models.Subject;
import com.example.portal.payload.response.MessageResponse;
import com.example.portal.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSubject(@Valid @RequestBody Subject subject){
        subjectRepository.save(subject);
        return ResponseEntity.ok(subject);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSubject(@Valid @RequestBody Subject subject){
        subjectRepository.save(subject);
        return ResponseEntity.ok(subject);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSubject(@Valid @RequestBody Subject subject){
        subjectRepository.deleteById(subject.getId());
        return ResponseEntity.ok(subject);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public String  handleAccessDeniedException(AccessDeniedException ex) {
        return "Unauthorized error: Full authentication is required to access this resource";
    }
}
