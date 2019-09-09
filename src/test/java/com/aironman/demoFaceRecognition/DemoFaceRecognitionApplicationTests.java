package com.aironman.demoFaceRecognition;

import com.aironman.demoFaceRecognition.service.FaceRecognitionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoFaceRecognitionApplicationTests {

	private static String PATH = "src/main/resources/images";

    @Autowired
	FaceRecognitionService service;

	@Test
	public void loadModel() throws Exception {
		Assert.assertTrue(service.loadModel());
	}

	@Test
	public void registerNewMember() throws Exception {
		String memberId = "Aaron Peirsol 1";
		String imagePath = PATH + "/Aaron_Peirsol_0001.jpg";
		Assert.assertTrue(service.loadModel());
		INDArray registered = service.registerNewMember(memberId,imagePath);
		Assert.assertNotNull(registered);
	}

	@Test
	public void shouldBeSameMemberId_after_WhoIs() throws Exception{
		String memberId = "Aaron Peirsol 1";
		String imagePath = PATH + "/Aaron_Peirsol_0001.jpg";
		Assert.assertTrue(service.loadModel());
		INDArray registered = service.registerNewMember(memberId,imagePath);
		Assert.assertNotNull(registered);
		String shouldBeSameMemberId = service.whoIs(imagePath);
		Assert.assertEquals("They are the same!.",memberId,shouldBeSameMemberId);
	}

	@Test
	public void shouldNotBeSameMemberId_after_WhoIs() throws Exception{
		String memberId1 = "Aaron Peirsol 1";
		String imagePath1 = PATH + "/Aaron_Peirsol_0001.jpg";

		String memberId2 = "Arnold_Schwarzenegger";
		String imagePath2 = PATH + "/Arnold_Schwarzenegger/Arnold_Schwarzenegger_0001.jpg";

		service.loadModel();

		service.registerNewMember(memberId1,imagePath1);
		service.registerNewMember(memberId2,imagePath2);

		String whoIsId1 = service.whoIs(imagePath1);
		String whoIsId2 = service.whoIs(imagePath2);

		Assert.assertNotEquals("They are Not the same!.",whoIsId1,whoIsId2);
	}
}
