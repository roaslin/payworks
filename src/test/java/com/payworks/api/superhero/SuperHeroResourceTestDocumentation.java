package com.payworks.api.superhero;


import com.payworks.api.documentation.ApiMockMvcBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuperHeroResourceTestDocumentation extends ApiMockMvcBase {

    @Autowired
    SuperHeroRepository superHeroRepository;

    @Before
    public void init() {
        superHeroRepository.deleteAll();

    }

    @After
    public void end() {
        superHeroRepository.deleteAll();
    }


    @Test
    public void testCreateNewSuperHero() throws Exception {
        //given
        String deadPool = "{\n" +
                "  \"name\": \"Wade Winston Wilson\",\n" +
                "  \"pseudonym\": \"Deadpool\",\n" +
                "  \"publisher\": \"Marvel Comics\",\n" +
                "  \"skills\": [\n" +
                "    \"Regenerative healing factor\",\n" +
                "    \"Master martial artist, swordsman, and marksman\",\n" +
                "    \"Extended longevity\",\n" +
                "    \"Carries devices that grant him teleportation and holographic disguise and a magic satchel\"\n" +
                "  ],\n" +
                "  \"allies\": [\n" +
                "    \"Merc with a Mouth\",\n" +
                "    \"Regenerating Degenerate\",\n" +
                "    \"Jack\",\n" +
                "    \"Wade T. Wilson\",\n" +
                "    \"Mithras\",\n" +
                "    \"Johnny Silvini\",\n" +
                "    \"Thom Cruz\",\n" +
                "    \"Hulkpool\",\n" +
                "    \"Wildcard\",\n" +
                "    \"Zenpool\"\n" +
                "  ],\n" +
                "  \"firstAppearance\": \"1991-02-01\"\n" +
                "}";


        //when
        mockMvc.perform(post("/api/superheroes/")
                .contentType(APPLICATION_JSON)
                .content(deadPool))
                // then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/superheroes/Deadpool"))
                .andExpect(jsonPath("$.name", is("Wade Winston Wilson")))
                .andExpect(jsonPath("$.pseudonym", is("Deadpool")))
                .andExpect(jsonPath("$.publisher", is("Marvel Comics")))
                .andExpect(jsonPath("$.skills").isArray())
                .andExpect(jsonPath("$.skills", hasSize(4)))
                .andExpect(jsonPath("$.allies").isArray())
                .andExpect(jsonPath("$.allies", hasSize(10)))
                .andExpect(jsonPath("$.firstAppearance", is("1991-02-01")))
                        // document
                .andDo(document("superheroes/post",
                        preprocessResponse(prettyPrint())
                        , responseHeaders(headerWithName("Location").description(
                                        "Uri of the new super hero"))
                        , requestFields(fieldWithPath("name").description("Super hero's regular name")
                                , fieldWithPath("pseudonym").description("Super hero's pseudonym")
                                , fieldWithPath("publisher").description("Super hero's publisher")
                                , fieldWithPath("skills").description("Super hero's skills or super powers")
                                , fieldWithPath("allies").description("Super hero's allies")
                                , fieldWithPath("firstAppearance").description("Super hero's first comic appearance")
                        )
                        , responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("Super hero's regular name")
                                , fieldWithPath("pseudonym").type(JsonFieldType.STRING).description("Super hero's pseudonym")
                                , fieldWithPath("publisher").type(JsonFieldType.STRING).description("Super hero's publisher")
                                , fieldWithPath("skills").type(JsonFieldType.ARRAY).description("Super hero's skills or super powers")
                                , fieldWithPath("allies").type(JsonFieldType.ARRAY).description("Super hero's allies")
                                , fieldWithPath("firstAppearance").type(JsonFieldType.STRING).description("Super hero's first comic appearance")
                        )));
    }

    @Test
    public void testFindSuperHeroByPseudonym() throws Exception {
        //given

        String[] skills = {"Regenerative healing factor", "Master martial artist, swordsman, and marksman", "Extended longevity", "Carries devices that grant him teleportation and holographic disguise and a magic satchel"};
        String[] allies = {"Merc with a Mouth", "Regenerating Degenerate", "Jack", "Wade T. Wilson", "Mithras", "Johnny Silvini", "Thom Cruz", "Hulkpool", "Wildcard", "Zenpool"};
        SuperHeroEntity deadPool = SuperHeroEntity
                .builder()
                .name("Wade Winston Wilson")
                .pseudonym("Deadpool")
                .publisher("Marvel Comics")
                .skills(Arrays.asList(skills))
                .allies(Arrays.asList(allies))
                .firstAppearance(LocalDate.parse("1991-02-01", DateTimeFormatter.ISO_LOCAL_DATE))
                .build();

        superHeroRepository.save(deadPool);

        //when
        // Needed RestDocumentationRequestBuilders to document pathParamenters
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/superheroes/{pseudonym}", "Deadpool"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Wade Winston Wilson")))
                .andExpect(jsonPath("$.pseudonym", is("Deadpool")))
                .andExpect(jsonPath("$.publisher", is("Marvel Comics")))
                .andExpect(jsonPath("$.skills").isArray())
                .andExpect(jsonPath("$.skills", hasSize(4)))
                .andExpect(jsonPath("$.allies").isArray())
                .andExpect(jsonPath("$.allies", hasSize(10)))
                .andExpect(jsonPath("$.firstAppearance", is("1991-02-01")))
                        // document
                .andDo(document("superheroes/get",
                        preprocessResponse(prettyPrint())
                        , pathParameters(parameterWithName("pseudonym").description("Super hero's pseudonym"))
                        , responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("Super hero's regular name")
                                , fieldWithPath("pseudonym").type(JsonFieldType.STRING).description("Super hero's pseudonym")
                                , fieldWithPath("publisher").type(JsonFieldType.STRING).description("Super hero's publisher")
                                , fieldWithPath("skills").type(JsonFieldType.ARRAY).description("Super hero's skills or super powers")
                                , fieldWithPath("allies").type(JsonFieldType.ARRAY).description("Super hero's allies")
                                , fieldWithPath("firstAppearance").type(JsonFieldType.STRING).description("Super hero's first comic appearance")
                        )));
    }

    @Test
    public void testGetAllSuperHeroes() throws Exception {
        //given

        String[] skillsDeadpool = {"Regenerative healing factor", "Master martial artist, swordsman, and marksman", "Extended longevity", "Carries devices that grant him teleportation and holographic disguise and a magic satchel"};
        String[] alliesDeadpool = {"Merc with a Mouth", "Regenerating Degenerate", "Jack", "Wade T. Wilson", "Mithras", "Johnny Silvini", "Thom Cruz", "Hulkpool", "Wildcard", "Zenpool"};
        SuperHeroEntity deadPool = SuperHeroEntity
                .builder()
                .name("Wade Winston Wilson")
                .pseudonym("Deadpool")
                .publisher("Marvel Comics")
                .skills(Arrays.asList(skillsDeadpool))
                .allies(Arrays.asList(alliesDeadpool))
                .firstAppearance(LocalDate.parse("1991-02-01", DateTimeFormatter.ISO_LOCAL_DATE))
                .build();

        String[] skillsSuperLopez = {"Poderes de López", "super poderes de López", "extra poderes de López"};
        String[] alliesSuperLopez = {"Jaime González Lidenbrock", "Luis Lanas"};

        SuperHeroEntity superLopez = SuperHeroEntity
                .builder()
                .name("Juan López")
                .pseudonym("SuperLopez")
                .publisher("Euredit, Bruguera, Ediciones B")
                .skills(Arrays.asList(skillsSuperLopez))
                .allies(Arrays.asList(alliesSuperLopez))
                .firstAppearance(LocalDate.parse("1973-01-01", DateTimeFormatter.ISO_LOCAL_DATE))
                .build();


        superHeroRepository.save(deadPool);
        superHeroRepository.save(superLopez);

        //when
        mockMvc.perform(get("/api/superheroes/"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.superHeroes").isArray())
                .andExpect(jsonPath("$.superHeroes[0].name", is("Wade Winston Wilson")))
                .andExpect(jsonPath("$.superHeroes[0].pseudonym", is("Deadpool")))
                .andExpect(jsonPath("$.superHeroes[0].publisher", is("Marvel Comics")))
                .andExpect(jsonPath("$.superHeroes[0].skills").isArray())
                .andExpect(jsonPath("$.superHeroes[0].skills", hasSize(4)))
                .andExpect(jsonPath("$.superHeroes[0].allies").isArray())
                .andExpect(jsonPath("$.superHeroes[0].allies", hasSize(10)))
                .andExpect(jsonPath("$.superHeroes[0].firstAppearance", is("1991-02-01")))
                .andExpect(jsonPath("$.superHeroes[1].name", is("Juan López")))
                .andExpect(jsonPath("$.superHeroes[1].pseudonym", is("SuperLopez")))
                .andExpect(jsonPath("$.superHeroes[1].publisher", is("Euredit, Bruguera, Ediciones B")))
                .andExpect(jsonPath("$.superHeroes[1].skills").isArray())
                .andExpect(jsonPath("$.superHeroes[1].skills", hasSize(3)))
                .andExpect(jsonPath("$.superHeroes[1].allies").isArray())
                .andExpect(jsonPath("$.superHeroes[1].allies", hasSize(2)))
                .andExpect(jsonPath("$.superHeroes[1].firstAppearance", is("1973-01-01")))
                        // document
                .andDo(document("superheroes/get/getAll",
                        preprocessResponse(prettyPrint())
                        , responseFields(
                                fieldWithPath("superHeroes[0].name").type(JsonFieldType.STRING).description("Super hero's regular name")
                                , fieldWithPath("superHeroes[0].pseudonym").type(JsonFieldType.STRING).description("Super hero's pseudonym")
                                , fieldWithPath("superHeroes[0].publisher").type(JsonFieldType.STRING).description("Super hero's publisher")
                                , fieldWithPath("superHeroes[0].skills").type(JsonFieldType.ARRAY).description("Super hero's skills or super powers")
                                , fieldWithPath("superHeroes[0].allies").type(JsonFieldType.ARRAY).description("Super hero's allies")
                                , fieldWithPath("superHeroes[0].firstAppearance").type(JsonFieldType.STRING).description("Super hero's first comic appearance")
                        )));
    }
}
