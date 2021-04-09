package ru.oneonyx.future.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.oneonyx.future.model.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
}
