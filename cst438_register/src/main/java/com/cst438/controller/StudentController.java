package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.Enrollment;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	
	@PostMapping("/student/add")
	@Transactional
	public StudentDTO addCourse( @RequestBody StudentDTO studentDTO  ) { 

		String student_email = studentDTO.email;   // student's email
		String student_name = studentDTO.name;
		
		Student student = studentRepository.findByEmail(student_email);
	
		//check if the given email belongs to any other student, if not, continue otherwise throw bad request
		if (student == null) {
			//build Student to save
			student = new Student();
			student.setName(student_name);
			student.setEmail(student_email);

			Student savedStudent = studentRepository.save(student);
			StudentDTO result = new StudentDTO(savedStudent.getStudent_id(), savedStudent.getName(), savedStudent.getEmail());
			result.message = "Student added successfully.";
			return result; //return the auto completed student object
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student email already exists.  ");
		}
		
	}
	
	@PostMapping("/student/hold/update")
	@Transactional
	public StudentDTO applyHold( @RequestBody StudentDTO studentDTO  ) { 

		String student_email = studentDTO.email; //Get the email
		String status = studentDTO.status;
		int status_code = studentDTO.statusCode;
		
		Student student = studentRepository.findByEmail(student_email);
	
		//if student exists 
		if (student != null) {
			//build Student to save
			student.setStatus(status);
			student.setStatusCode(status_code);

			Student savedStudent = studentRepository.save(student);
			StudentDTO result = new StudentDTO(savedStudent.getStudent_id(), savedStudent.getName(), savedStudent.getEmail(), savedStudent.getStatusCode(), savedStudent.getStatus());
			return result; //return the auto completed student object
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student does not exists");
		}
		
	}
}
