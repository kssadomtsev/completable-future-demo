package ru.oneonyx.future.service;

import org.springframework.web.multipart.MultipartFile;
import ru.oneonyx.future.model.Film;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FilmService {

    CompletableFuture<List<Film>> saveFilmsAsync(final MultipartFile file) throws Exception;

    List<Film> saveFilms(final MultipartFile file) throws Exception;
}
