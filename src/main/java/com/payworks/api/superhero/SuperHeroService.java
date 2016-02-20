package com.payworks.api.superhero;

import java.util.Optional;

public interface SuperHeroService {
    SuperHeroObject createSuperHero(SuperHeroObject request);

    Optional<SuperHeroObject> findSuperHero(String pseudonym);

    SuperHeroesResponse getAllSuperHeroes();
}
