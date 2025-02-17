package com.maicosmaniotto.crud_spring.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "cursos")
@SQLDelete(sql = "update cursos set status = 'X' where id = ?")
@SQLRestriction("status <> 'X'")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("_id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Column(name = "nome", length = 100, nullable = false)
    private String name;
    
    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 30, message = "Category can be at most 30 characters")
    @Pattern(regexp = "Front-end|Back-end|Database", message = "Category must be one of Front-end, Back-end or Database")
    @Column(name = "categoria", length = 30, nullable = false)
    private String category;

    @NotBlank(message = "Status is required")
    @Size(min = 1, max = 1, message = "Status must have 1 character")
    @Pattern(regexp = "[AIX]", message = "Status must be one of A (Ativo), I (Inativo) or X (Excluído)")
    @Column(name = "status", length = 1, nullable = false)
    private String status = "A";
}
