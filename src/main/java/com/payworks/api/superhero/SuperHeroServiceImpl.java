package com.payworks.api.superhero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuperHeroServiceImpl implements SuperHeroService {
    @Autowired
    SuperHeroRepository superHeroRepository;

    @Override
    @Transactional
    public SuperHeroObject createSuperHero(SuperHeroObject request) {

        SuperHeroEntity newSuperHero = SuperHeroEntity
                .builder()
                .name(request.getName())
                .pseudonym(request.getPseudonym())
                .publisher(request.getPublisher())
                .skills(request.getSkills())
                .allies(request.getAllies())
                .firstAppearance(LocalDate.parse(request.getFirstAppearance(), DateTimeFormatter.ISO_LOCAL_DATE))
                .build();

        return buildSuperHeroObjectFromEntity(superHeroRepository.save(newSuperHero));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SuperHeroObject> findSuperHero(String pseudonym) {

        final Optional<SuperHeroEntity> superHero = Optional.ofNullable(superHeroRepository.findByPseudonym(pseudonym));

        if(superHero.isPresent()) {
            return Optional.of(buildSuperHeroObjectFromEntity(superHero.get()));
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public SuperHeroesResponse getAllSuperHeroes() {
        final List<SuperHeroObject> superHeroes = superHeroRepository.findAll()
                .stream()
                .map(this::buildSuperHeroObjectFromEntity)
                .collect(Collectors.toList());

        return new SuperHeroesResponse(superHeroes);
    }

    private SuperHeroObject buildSuperHeroObjectFromEntity(final SuperHeroEntity superHero) {
        return SuperHeroObject
                .builder()
                .name(superHero.getName())
                .pseudonym(superHero.getPseudonym())
                .publisher(superHero.getPublisher())
                .skills(superHero.getSkills())
                .allies(superHero.getAllies())
                .firstAppearance(superHero.getFirstAppearance().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .build();
    }
}
