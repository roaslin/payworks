package com.payworks.api.superhero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperHeroesResponse {
    private List<SuperHeroObject> superHeroes;
}
