package com.aironman.demoFaceRecognition;

import com.aironman.demoFaceRecognition.service.FaceRecognitionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoFaceRecognitionApplicationTests {

    @Autowired
	FaceRecognitionService service;

	@Test
	public void contextLoads() {
	}

	@Test
	public void loadModel() throws Exception {
		Assert.assertTrue(service.loadModel());
	}

	@Test
	public void loadPhotoIntoModel() throws IOException {
		String memberId = "";
		String imagePath = "";
		service.registerNewMember(memberId,imagePath);
		String shouldBeSameMemberId = service.whoIs(imagePath);
		Assert.assertEquals("They are the same!.",memberId,shouldBeSameMemberId);
	}

}
