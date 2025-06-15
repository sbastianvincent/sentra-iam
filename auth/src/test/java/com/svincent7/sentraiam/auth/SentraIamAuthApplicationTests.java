package com.svincent7.sentraiam.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SentraIamAuthApplicationTests {

	@Autowired
	private SentraIamAuthApplication authApplication;

	@Test
	public void contextLoads() {
		// Ensure that the application context loads successfully
		Assertions.assertNotNull(authApplication);
	}

	@Test
	public void testApplicationStart() {
		authApplication.main(new String[] {});
	}

}
