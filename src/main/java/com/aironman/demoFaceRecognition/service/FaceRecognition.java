package com.aironman.demoFaceRecognition.service;

import java.io.IOException;


/***
 * @author aironman
 * @author klevis
 */
public interface FaceRecognition {
    // TODO check this generic exception
    boolean loadModel() throws Exception;
    void registerNewMember(String memberId, String imagePath) throws IOException;
    String whoIs(String imagePath) throws IOException;

}
