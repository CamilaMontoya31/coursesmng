package edu.academy.coursesmng.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;

import edu.academy.coursesmng.entity.Course;
import edu.academy.coursesmng.entity.Instructor;
import edu.academy.coursesmng.entity.InstructorDetail;
import edu.academy.coursesmng.service.InstructorService;

@SpringBootTest
public class InstructorRepositoryTest {

    @Autowired
    private InstructorRepository instructorRepository;

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "Ana;Garcia;ana@example.com;http://youtube.com/ana;reading;2023-01-15;true",
            "Jane;Jackson;jane@example.com;http://youtube.com/jane;swimming;2023-02-20;false",
            "Bob;Smith;bob@example.com;http://youtube.com/bob;cooking;2023-03-25;true"
    })
    void saveAndFindInstructor(String firstName, String lastName, String email,
            String youtubeChannel, String hobby, String hireDate, boolean active) {
        // Arrange
        Instructor instructor = new Instructor(firstName, lastName, email, LocalDate.parse(hireDate), active);
        InstructorDetail detail = new InstructorDetail(youtubeChannel, hobby);
        instructor.setInstructorDetail(detail);

        // Act
        Instructor savedInstructor = instructorRepository.save(instructor);

        // Assert
        assertAll("Verificaciones de inserción de un Instructor",
                () -> assertThat(savedInstructor.getId()).isGreaterThan(0),
                () -> assertThat(instructorRepository.findById(savedInstructor.getId())).isPresent(),
                () -> assertThat(instructorRepository.findById(savedInstructor.getId()).get().getFirstName())
                        .isEqualTo(firstName));
    }

    @Test
    void testFindAllByOrderByFirstNameAsc() {
        //en SQL hace  un ORDER BY first_name ASC
     //   boolean active = true;
        String firstName = "Ana";
        String email = "ana@example.com";

        String firstName1 = "Beatriz";
        String email1 = "bea@example.com";

        String firstName2 = "Camila";
        String email2 = "cam@example.com";

        Instructor instructor1 = new Instructor(firstName, "Garcia", email, LocalDate.of(2023, 1, 15), true);
        Instructor instructor2 = new Instructor(firstName1, "Lopez", email1, LocalDate.of(2023, 2, 20), true);
        Instructor instructor3 = new Instructor(firstName2, "Martinez", email2, LocalDate.of(2023, 3, 25), false);

        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        instructorRepository.save(instructor3);

        // Act
        List<Instructor> instructors = instructorRepository.findByFirstNameAndEmail(firstName, email);

        // Assert
        String expectedFirstName = "Ana";
        String expectedEmail = "ana@example.com";
        assertThat(instructors).isNotEmpty();
        assertThat(instructors).hasSize(1);
        // assertThat(instructors.get(0).getFirstName()).isEqualTo(expectedFirstName);
        // assertThat(instructors.get(0).getEmail()).isEqualTo(expectedEmail);

        assertThat(instructors)
                .singleElement()
                .satisfies(instructor -> {
                    assertThat(instructor.getFirstName()).isEqualTo(expectedFirstName);
                    assertThat(instructor.getEmail()).isEqualTo(expectedEmail);
                });
    }

    @Test
    void testFindByActiveOrderByFirstNameDesc() {
 //en SQL hace  un ORDER BY first_name DESC
        // Arrange
        boolean active = true;
        String firstName = "Ana";
        String email = "ana@example.com";

        String firstName1 = "Beatriz";
        String email1 = "bea@example.com";

        String firstName2 = "Camila";
        String email2 = "cam@example.com";

        Instructor instructor1 = new Instructor(firstName, "Garcia", email, LocalDate.of(2023, 1, 15), true);
        Instructor instructor2 = new Instructor(firstName1, "Lopez", email1, LocalDate.of(2023, 2, 20), true);
        Instructor instructor3 = new Instructor(firstName2, "Martinez", email2, LocalDate.of(2023, 3, 25), false);

        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        instructorRepository.save(instructor3);

      
        // Act
        List<Instructor> instructors = instructorRepository.findByActiveOrderByFirstNameDesc(active);

        // Assert
        assertThat(instructors).isNotEmpty();
        assertThat(instructors).allMatch(instructor -> instructor.isActive());
        assertThat(instructors).isSortedAccordingTo((i1, i2) -> i2.getFirstName().compareTo(i1.getFirstName()));
    }

    @Test
    void testFindByFirstNameAndEmail() {
        // Arrange
        String firstName = "Ana";
        String email = "ana@example.com";

        // Act
        List<Instructor> instructors = instructorRepository.findByFirstNameAndEmail(firstName, email);

        // Assert
        String expectedFirstName = "Ana";
        String expectedEmail = "ana@example.com";
        assertThat(instructors).isNotEmpty();
        assertThat(instructors).hasSize(1);
        // assertThat(instructors.get(0).getFirstName()).isEqualTo(expectedFirstName);
        // assertThat(instructors.get(0).getEmail()).isEqualTo(expectedEmail);

        assertThat(instructors)
                .singleElement()
                .satisfies(instructor -> {
                    assertThat(instructor.getFirstName()).isEqualTo(expectedFirstName);
                    assertThat(instructor.getEmail()).isEqualTo(expectedEmail);
                });

    }

    @Test
    void testFindByHireDateBetween() {

        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);

        List<Instructor> instructors = instructorRepository.findByHireDateBetween(start, end);

        assertThat(instructors).isNotEmpty();
        assertThat(instructors).allSatisfy(instructor -> {
            assertThat(instructor.getHireDate()).isAfterOrEqualTo(start);
            assertThat(instructor.getHireDate()).isBeforeOrEqualTo(end);
        });

    }

    @Test
    @DisplayName("Debe encontrar instructores cuya fecha de contratación esté dentro del rango especificado")
    void findByHireDateBetween(){
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 12, 31);
 
        //Instructor en el rango esperadp
        Instructor inst1 = new Instructor("Carlos", "Rojas", "carlos@example.com", LocalDate.of(2024, 6, 15), true);
        inst1.setHireDate(LocalDate.of(2024, 6, 15));
 
        //Instructor  por encima del limite
        Instructor inst2 = new Instructor("Elena", "Mora", "elena@example.com", LocalDate.of(2024, 12, 31), true);
        inst2.setHireDate(LocalDate.of(2024, 12, 31));
 
        //Instructor fuera del rango
        Instructor instFueraRango = new Instructor("Pedro", "Vargas", "pedro@example.com", LocalDate.of(2023, 11, 20), true);
        instFueraRango.setHireDate(LocalDate.of(2023, 11, 20));
 
        instructorRepository.saveAll(List.of(inst1, inst2, instFueraRango));
 
        //Act
        List<Instructor> resultado = instructorRepository.findByHireDateBetween(fechaInicio, fechaFin);
 
        //Assert
        assertAll("Verificaciones de búsqueda por rango de fechas de contratación",
            () -> assertThat(resultado).isNotEmpty(),
            () -> assertThat(resultado).hasSize(2),
            () -> assertThat(resultado)
                    .extracting(Instructor::getEmail)
                    .containsExactlyInAnyOrder("carlos@example.com", "elena@example.com"),
            () -> assertThat(resultado)
                    .allMatch(inst -> !inst.getHireDate().isBefore(fechaInicio) &&
                                      !inst.getHireDate().isAfter(fechaFin))
        );
    }
    @Test
    void testFindByHireDateIn() {
        //Arrange
        List<LocalDate> targetDates = Arrays.asList(
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 1, 3)
        );
 
        //Act
        List<Instructor> instructors = instructorRepository.findByHireDateIn(targetDates);
 
        //Assert
        assertThat(instructors).isNotEmpty();
        assertThat(instructors).allSatisfy(instructor -> {
            assertThat(instructor.getHireDate()).isIn(targetDates);
        });
 
    }
 
    @Test 
    void testsaveInstructor() {
      
        // Arrange
        Instructor instructor = new Instructor("John", "Doe", "john.doe@example.com", LocalDate.of(2023, 1, 1), true);

        //detalle
        InstructorDetail detail = new InstructorDetail("http://youtube.com/test", "reading");
    
        //cursos

        Course course1 = new Course("Math 101");
        Course course2 = new Course("Physics 101");
        
        // Act 

        InstructorService instructorService = new InstructorService(instructorRepository);

        Instructor savedInstructor = instructorService.saveInstructor(instructor);

        // Assert
        assertAll("Verificaciones de inserción de un Instructor",
                () -> assertThat(savedInstructor.getId()).isGreaterThan(0),
                () -> assertThat(instructorRepository.findById(savedInstructor.getId())).isPresent(),
                () -> assertThat(instructorRepository.findById(savedInstructor.getId()).get().getFirstName())
                        .isEqualTo("John"));
 
    }
    @Test 
    void findInstructorById() {
        //TODO 
        // Arrange
        

        // Act
        

        //arreglarlo
        // Assert
 
    }

     @Test
    public void crearInstructorConDetallesYCursos(){
        try{
            InstructorDetail instructorDetail = new InstructorDetail("https://www.youtube.com/channel/UC1234567890", "Guitar");
            Course course1 = new Course("Programación Orientada a Objetos");
            Course course2 = new Course("Bases de Datos I");
            List<Course> courses = new ArrayList<>();
            courses.add(course1);
            courses.add(course2);
            Instructor instructor = new Instructor("John", "Doe", "john.doe@example.com", LocalDate.of(2023, 1, 1), true);
            instructorRepository.save(instructor); //Se guarda el instructor, y por cascada se guardan los detalles y los cursos
        }
        catch(Exception e){ //Si cae aquí, hubo un problema innesperado o a nivel de base de datos, por lo que se imprime el stack trace para ver el error
            e.printStackTrace();
        }
    }
 
}