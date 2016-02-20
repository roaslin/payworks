package com.payworks.api.superhero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuperHeroObject {

    @NotNull
    private String name;

    @NotNull
    private String pseudonym;

    @NotNull
    private String publisher;

    @NotNull
    private List<String> skills;

    private List<String> allies;

    @NotNull
    private String firstAppearance;// YYYY-MM-DD
}
