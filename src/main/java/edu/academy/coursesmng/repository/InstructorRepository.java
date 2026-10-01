package edu.academy.coursesmng.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.academy.coursesmng.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    // add custom finder methods if needed, e.g.List<Instructor>
    
    List<Instructor> findByFirstNameAndEmail(String firstName, String email);

    List<Instructor> findByHireDateBetween(LocalDate start, LocalDate end);

    List<Instructor> findAllByOrderByFirstNameAsc();

    List<Instructor> findByActiveOrderByFirstNameDesc(boolean active);

    List<Instructor> findByHireDateIn(Collection<LocalDate> dates);

}