package com.cst438.domain;

public class StudentDTO {
	public int student_id;
	public String name;
	public String email;
	public int statusCode;
	public String status;
	
	public StudentDTO() {
		this.student_id = 0;
		this.name = null;
		this.email = null;
		this.status = null;
		this.statusCode = 0;
	}
	
	public StudentDTO(int student_id, String name, String email) {
		this.student_id = student_id;
		this.name = name;
		this.email =email;
	}
	
	public StudentDTO(int student_id, String name, String email, int statusCode, String status) {
		this.student_id = student_id;
		this.name = name;
		this.email =email;
		this.status = status;
		this.statusCode = statusCode;
	}
	
}
