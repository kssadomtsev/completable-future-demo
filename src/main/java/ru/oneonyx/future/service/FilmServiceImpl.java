package ru.oneonyx.future.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.oneonyx.future.model.Director;
import ru.oneonyx.future.model.Film;
import ru.oneonyx.future.repository.FilmRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilmServiceImpl.class);

    private final FilmRepository filmRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Async
    public CompletableFuture<List<Film>> saveFilmsAsync(MultipartFile file) throws Exception {
        LOGGER.info("Получение данных по файлу = {}", file.getOriginalFilename());
        LOGGER.info("Поток {}", Thread.currentThread().getName());
        final long start = System.currentTimeMillis();

        List<Film> films = parseCSVFile(file.getInputStream());

        LOGGER.info("Saving a list of films {} records", films.size());

        try {
            films = filmRepository.saveAll(films);
        } catch (Exception exception) {
            LOGGER.info("" + exception);
        }


        LOGGER.info("Elapsed time: {}, для потока: {}", (System.currentTimeMillis() - start), Thread.currentThread().getName());
        return CompletableFuture.completedFuture(films);

    }


    @Override
    public List<Film> saveFilms(MultipartFile file) throws Exception {
        LOGGER.info("Получение данных по файлу = {}", file.getOriginalFilename());
        List<Film> films = parseCSVFile(file.getInputStream());
        LOGGER.info("Saving a list of films {} records", films.size());
        return filmRepository.saveAll(films);
    }

    private List<Film> parseCSVFile(final InputStream inputStream) throws Exception {
        List<Film> films = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(";");
                    final Film film = new Film();
                    if (tryParse(data[0]) != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(Calendar.YEAR, tryParse(data[0]));
                        film.setYear(cal);
                    }
                    film.setLength(tryParse(data[1]));
                    film.setTitle(data[2]);
                    film.setSubject(data[3]);
                    film.setActor(data[4]);
                    film.setActress(data[5]);
                    film.setDirector(new Director());
                    film.setPopularity(tryParse(data[7]));
                    films.add(film);
                }
                return films;
            }
        } catch (final IOException e) {
            LOGGER.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

    private static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


//https://run.mocky.io/v3/50fcfb21-8408-47b5-bc63-4195ef5fc536
//
//
//        https://run.mocky.io/v3/b0f6948f-10e2-47b5-8573-1c9b111870f7

