package edu.academy.coursesmng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.academy.coursesmng.entity.Instructor;
import edu.academy.coursesmng.repository.InstructorRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }
    
    public Instructor getInstructorById(int id) {
        return instructorRepository.findById(id).orElse(null);
    }
    
    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
}
