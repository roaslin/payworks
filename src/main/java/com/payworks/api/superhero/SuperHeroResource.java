package com.payworks.api.superhero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Resource that exposes existing super heroes into the databse.
 */
@RestController
@RequestMapping("/api/superheroes")
public class SuperHeroResource {

    @Autowired
    private SuperHeroService superHeroService;

    /**
     * Resource for creating new super hero.
     * @param request of type SuperHeroObject.
     * @return Serialized SuperHeroObject that is created and HttpStatus as Created.
     * The new resource URI into the Location header is also provided.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SuperHeroObject> createNewSuperHeroes(@RequestBody @NotNull SuperHeroObject request) {

        final SuperHeroObject response = superHeroService.createSuperHero(request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromUriString("/api/superheroes")
                .path("/{pseudonym}").buildAndExpand(response.getPseudonym()).toUri());

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Resource for fetching a particular super hero by pseudonym.
     *
     * @param pseudonym String containing the super hero pseudonym to be look up into the database.
     * @return Serialized SuperHeroObject with HttpStatus.OK status or message with HttpStatus.NOT_FOUND.
     */
    @RequestMapping(value = "{pseudonym}", method = RequestMethod.GET)
    public ResponseEntity<?> findSuperHero(@PathVariable @NotNull String pseudonym) {
        final Optional<SuperHeroObject> response = superHeroService.findSuperHero(pseudonym);

        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Super hero [" + pseudonym + "] not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Resource for fetching all existing super heroes into the database.
     *
     * @return SuperHeroesResponse containing a list of SuperHeroObjects.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SuperHeroesResponse> getAllSuperHeroes()
    {
        return new ResponseEntity<>(superHeroService.getAllSuperHeroes(), HttpStatus.OK);
    }
}
