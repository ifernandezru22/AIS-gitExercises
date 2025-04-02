package es.codeurjc.web.nitflex.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Blob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.web.nitflex.ImageTestUtils;
import es.codeurjc.web.nitflex.dto.film.CreateFilmRequest;
import es.codeurjc.web.nitflex.dto.film.FilmDTO;
import es.codeurjc.web.nitflex.dto.film.FilmMapper;
import es.codeurjc.web.nitflex.dto.film.FilmSimpleDTO;
import es.codeurjc.web.nitflex.model.Film;
import es.codeurjc.web.nitflex.repository.FilmRepository;
import es.codeurjc.web.nitflex.service.FilmService;
import es.codeurjc.web.nitflex.utils.ImageUtils;

@SpringBootTest
class integrationTests {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ImageUtils imageUtils;

    private Film film;
    private FilmMapper filmMapper;
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        film = new Film("Deadpool", "A funny movie", 2016, "18");
        filmMapper = Mappers.getMapper(FilmMapper.class);
        filmService = new FilmService(filmRepository, null, imageUtils, filmMapper);
        
    }

    @Test
    void saveFilmTest() {
        // Given
        CreateFilmRequest createFilmRequest = filmMapper.toCreateFilmRequest(film);

        // When
        FilmDTO savedFilmDTO = filmService.save(createFilmRequest);
        Film savedFilm = filmMapper.toDomain(savedFilmDTO);
        Film foundFilm = filmRepository.findById(savedFilm.getId()).orElse(null);

        // Then
        assertNotNull(foundFilm);
    }
    @Test
    void updateFilmTest() {
        //Given
        MultipartFile sampleImage = ImageTestUtils.createSampleImage(); 
        Blob blob = imageUtils.multiPartFileImageToBlob(sampleImage);
        film.setPosterFile(blob);
        CreateFilmRequest createFilmRequest = filmMapper.toCreateFilmRequest(film); 

        // When
        FilmDTO savedFilmDTO = filmService.save(createFilmRequest, film.getPosterFile()); 

        film.setTitle("nuevo titulo");
        film.setSynopsis("nueva sinopsis");

        FilmSimpleDTO updated = filmMapper.toFilmSimpleDTO(film);
        filmService.update(savedFilmDTO.id(), updated);

        Film savedFilm = filmMapper.toDomain(savedFilmDTO);
        Film foundFilm = filmRepository.findById(savedFilm.getId()).orElse(null);

        // Then
        assertEquals(foundFilm.getTitle(), film.getTitle());
        assertEquals(foundFilm.getSynopsis(), film.getSynopsis());
        boolean areSameBlob = false;
        try {
            areSameBlob = ImageTestUtils.areSameBlob(foundFilm.getPosterFile(), film.getPosterFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(areSameBlob);
    }
}
