package com.example.portal.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document("classes")
public class Classes {

    @Id
    private String id;
    @NotBlank
    private String idSubject;
    @NotBlank
    private String idTeacher;
    private List<String> idStudent;
    @NotBlank
    private int time;
    @NotBlank
    private int timeStart;
    @NotBlank
    private int timeEnd;
    @NotBlank
    private String location;
    @NotBlank
    private int quantity;

}
