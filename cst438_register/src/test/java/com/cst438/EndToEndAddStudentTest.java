package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndAddStudentTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "addtest@csumb.edu";
	public static final String TEST_USER_NAME = "Hikaru Nakamora";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	StudentRepository studentRepository;


	/*
	 * Student add course TEST_COURSE_ID to schedule for 2021 Fall semester.
	 */
	
	@Test
	public void addStudentTest() throws Exception {

		/*
		 * if student is already enrolled, then delete the enrollment.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// select the last of the radio buttons on the list of semesters page.
			
			WebElement we = driver.findElement(By.xpath("//*[@id='goToAddStudentButton']"));
			we.click();
			
			driver.findElement(By.xpath("//*[@id='addStudentButton']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			String Message = driver.findElement(By.xpath("//h3[@id='respMessage']")).getText();
			boolean messageCorrect = Message.equals("Enter complete info");
			
			assertTrue(messageCorrect, "Incorrect response message displayed after attemtping to add incomplete information");
			
			driver.findElement(By.xpath("//input[@id='nameIn']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@id='emailIn']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//*[@id='addStudentButton']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			Student stu = studentRepository.findByEmail(TEST_USER_EMAIL);
			boolean stuFound = (stu != null);
			
			assertTrue(stuFound, "Student not found in repository after adding");
			
			
			Message = driver.findElement(By.xpath("//h3[@id='respMessage']")).getText();
			messageCorrect = Message.equals("Student added successfully.");
			
			assertTrue(messageCorrect, "Incorrect response message displayed after adding");
			
			driver.findElement(By.xpath("//*[@id='addStudentButton']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			Message = driver.findElement(By.xpath("//h3[@id='respMessage']")).getText();
			messageCorrect = Message.equals("Student email already exists.");
			
			assertTrue(messageCorrect, "Incorrect response message displayed after attemtping to add a duplicate student email");

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			
			Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (s != null)
				studentRepository.delete(s);

			driver.quit();
		}

	}
}
