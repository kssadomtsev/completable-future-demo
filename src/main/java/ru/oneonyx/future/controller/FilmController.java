package ru.oneonyx.future.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.oneonyx.future.model.Film;
import ru.oneonyx.future.repository.DirectorRepository;
import ru.oneonyx.future.repository.FilmRepository;
import ru.oneonyx.future.service.FilmService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/film")
public class FilmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    private final FilmRepository filmRepository;

    private final DirectorRepository directorRepository;

    @Autowired
    public FilmController(FilmService filmService, FilmRepository filmRepository, DirectorRepository directorRepository) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
        this.directorRepository = directorRepository;
    }


    @PostMapping(value = "/async", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    //@ResponseBody
    public ResponseEntity<String> uploadFileAsync(@RequestParam(value = "files") MultipartFile[] files) {
        filmRepository.deleteAll();
        directorRepository.deleteAll();
        LOGGER.info("Начато получение и асинхроннная обработка данных");
        LOGGER.info("Поток {}", Thread.currentThread().getName());
        final long start = System.currentTimeMillis();
        try {
            List<CompletableFuture<List<Film>>> cfList = new ArrayList<>();
            for (final MultipartFile file : files) {
                cfList.add(filmService.saveFilmsAsync(file));
            }
            CompletableFuture.allOf(cfList.toArray(new CompletableFuture[0])).join();
            LOGGER.info("Total elapsed time: {}", (System.currentTimeMillis() - start));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(value = "/sync", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    //@ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam(value = "files") MultipartFile[] files) {
        filmRepository.deleteAll();
        directorRepository.deleteAll();
        LOGGER.info("Начато получение и синхроннная обработка данных");
        final long start = System.currentTimeMillis();
        try {
            List<Film> films = new ArrayList<>();
            for (final MultipartFile file : files) {
                films.addAll(filmService.saveFilms(file));
            }
            LOGGER.info("Total elapsed time: {}", (System.currentTimeMillis() - start));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

//    @RequestMapping(method = RequestMethod.GET, consumes = {MediaType.APPLICATION_JSON_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE})
//    public @ResponseBody
//    CompletableFuture<ResponseEntity> getAllCars() {
//        return carService.getAllCars().<ResponseEntity>thenApply(ResponseEntity::ok)
//                .exceptionally(handleGetCarFailure);
//    }
//
//    private static Function<Throwable, ResponseEntity<? extends List<Car>>> handleGetCarFailure = throwable -> {
//        LOGGER.error("Failed to read records: {}", throwable);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    };
}
