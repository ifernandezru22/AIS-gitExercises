package es.codeurjc.web.nitflex.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import es.codeurjc.web.nitflex.dto.film.CreateFilmRequest;
import es.codeurjc.web.nitflex.dto.film.FilmMapper;
import es.codeurjc.web.nitflex.model.*;
import es.codeurjc.web.nitflex.repository.*;
import es.codeurjc.web.nitflex.service.FilmService;
import es.codeurjc.web.nitflex.service.exceptions.FilmNotFoundException;


@ExtendWith(MockitoExtension.class)
class unitTest {
    private FilmMapper filmMapper;
    private FilmService filmService;
    private FilmRepository filmRepository;
    
    @BeforeEach
    void setUp() {
        filmRepository = mock(FilmRepository.class);
        filmMapper = Mappers.getMapper(FilmMapper.class);
        filmService = new FilmService(filmRepository, null, null, filmMapper);
    }

    @Test
    void saveFilmWithoutImage(){
        //Given
        Film film = new Film("Deadpool", "A funny movie", 2016, "18");

        //When
        CreateFilmRequest createFilmRequest = filmMapper.toCreateFilmRequest(film);
        filmService.save(createFilmRequest);
        
        //Then
        verify(filmRepository).save(film);
    }

    @Test
    void deleteNonExistingFilm(){
        //Given
        //Given in beforeEach
        Long id = anyLong();

        //When
        when(filmRepository.findById(id)).thenReturn(Optional.empty());

        //Then
        Exception ex = assertThrows(FilmNotFoundException.class, () -> {
            filmService.delete(id);
        });
        assertEquals(ex.getMessage(), "Film not found with id: " + id);
    }
}
