package com.example.portal.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document("subject")
public class Subject {
    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private int lesson;
}
