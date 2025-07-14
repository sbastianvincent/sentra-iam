package com.svincent7.sentraiam.identity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SentraIamIdentityApplicationTests {

	@Autowired
	private SentraIamIdentityApplication identityApplication;

	@Test
	public void contextLoads() {
		// Ensure that the application context loads successfully
		Assertions.assertNotNull(identityApplication);
	}

	@Test
	public void testApplicationStart() {
		identityApplication.main(new String[] {});
	}

}
