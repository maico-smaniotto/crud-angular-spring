package com.maicosmaniotto.crud_spring.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maicosmaniotto.crud_spring.enums.Category;
import com.maicosmaniotto.crud_spring.enums.RecordStatus;
import com.maicosmaniotto.crud_spring.enums.converters.CategoryConverter;
import com.maicosmaniotto.crud_spring.enums.converters.RecordStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String name;
    
    @NotNull
    @Convert(converter = CategoryConverter.class)
    @Column(name = "categoria", nullable = false)
    private Category category;

    @NotNull
    @Convert(converter = RecordStatusConverter.class)
    @Column(name = "status", length = 1, nullable = false)
    private RecordStatus status = RecordStatus.ACTIVE;
}
