package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.ScheduleController;
import com.cst438.controller.StudentController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class StudentControllerTest {

	static final String URL = "http://localhost:8080";
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";


	@MockBean
	StudentRepository studentRepository;

	@MockBean
	GradebookService gradebookService;

	@Autowired
	private MockMvc mvc;

	@Test
	public void addStudentSuccess()  throws Exception {
		
		MockHttpServletResponse response;
			
				
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStudent_id(1);
		
		// given  -- stubs for database repositories that return test data
	    given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
	    given(studentRepository.save(any(Student.class))).willReturn(student);
	  
	    // create the DTO (data transfer object) for the student to add.
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.name = TEST_STUDENT_NAME;
		studentDTO.email = TEST_STUDENT_EMAIL;
		
		// then do an http post request with body of studentDTO as JSON
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student/add")
			      .content(asJsonString(studentDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
		
		// verify that returned data has non zero primary key
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.student_id);
		
		// verify that repository save method was called.
		verify(studentRepository).save(any(Student.class));
	}
	
	@Test
	public void applyStudentHold()  throws Exception {
		
		MockHttpServletResponse response;
			
				
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStudent_id(1);
		
		Student updatedStudent = new Student();
		updatedStudent.setEmail(TEST_STUDENT_EMAIL);
		updatedStudent.setName(TEST_STUDENT_NAME);
		updatedStudent.setStudent_id(1);
		updatedStudent.setStatus("Alch");
		updatedStudent.setStatusCode(1);
		
		// given  -- stubs for database repositories that return test data
	    given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
	    given(studentRepository.save(any(Student.class))).willReturn(updatedStudent);
	  
	    // create the DTO (data transfer object) for the student to add.
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		
		// then do an http post request with body of studentDTO as JSON
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student/hold/update")
			      .content(asJsonString(studentDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
		
		// verify that returned data has non zero primary key
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.statusCode);
		
		// verify that repository save method was called.
		verify(studentRepository).save(any(Student.class));
	}
	
	@Test
	public void removeStudentHold()  throws Exception {
		
		MockHttpServletResponse response;
			
				
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStudent_id(1);
		student.setStatus("Alch");
		student.setStatusCode(1);
		
		Student updatedStudent = new Student();
		updatedStudent.setEmail(TEST_STUDENT_EMAIL);
		updatedStudent.setName(TEST_STUDENT_NAME);
		updatedStudent.setStudent_id(1);

		
		// given  -- stubs for database repositories that return test data
	    given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
	    given(studentRepository.save(any(Student.class))).willReturn(updatedStudent);
	  
	    // create the DTO (data transfer object) for the student to add.
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		
		// then do an http post request with body of studentDTO as JSON
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student/hold/update")
			      .content(asJsonString(studentDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
		
		// verify that returned data has non zero primary key
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertEquals( 0  , result.statusCode);
		
		// verify that repository save method was called.
		verify(studentRepository).save(any(Student.class));
	}
		
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
