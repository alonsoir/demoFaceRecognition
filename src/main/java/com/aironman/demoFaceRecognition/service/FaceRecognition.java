package com.aironman.demoFaceRecognition.service;

import java.io.IOException;

public interface FaceRecognition {

    public void loadModel() throws Exception;
    public void registerNewMember(String memberId, String imagePath) throws IOException;
    public String whoIs(String imagePath) throws IOException;

}
