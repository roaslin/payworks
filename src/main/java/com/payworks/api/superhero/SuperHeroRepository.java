package com.payworks.api.superhero;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for SuperHeroEntity objects.
 */
public interface SuperHeroRepository extends JpaRepository<SuperHeroEntity, Long> {
    SuperHeroEntity findByPseudonym(String pseudonym);
}
