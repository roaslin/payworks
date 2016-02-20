package com.payworks.api.superhero;

import com.payworks.api.persistence.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Super hero entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity(name = "super_hero")
@NoArgsConstructor
@AllArgsConstructor
public class SuperHeroEntity extends BaseEntity {

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String pseudonym;

    @NotNull
    private String publisher;

    @ElementCollection
    @NotNull
    private List<String> skills;

    @ElementCollection
    private List<String> allies;

    @NotNull
    private LocalDate firstAppearance;// YYYY-MM-DD
}
