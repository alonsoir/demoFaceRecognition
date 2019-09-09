package com.aironman.demoFaceRecognition.service;

import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;


/***
 * @author aironman
 * @author klevis
 */
public interface FaceRecognition {
    // TODO check this generic exception
    boolean loadModel() throws Exception;
    INDArray registerNewMember(String memberId, String imagePath) throws IOException;
    String whoIs(String imagePath) throws IOException;

}
