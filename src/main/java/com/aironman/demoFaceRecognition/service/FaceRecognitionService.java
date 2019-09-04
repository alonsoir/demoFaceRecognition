package com.aironman.demoFaceRecognition.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/***
 * @author aironman
 * @author klevis
 */
@Service
public class FaceRecognitionService extends FaceRecognitionUtils implements FaceRecognition  {

    private static final Logger logger = LogManager.getLogger(FaceRecognitionService.class);

    @Autowired
    private FaceNetSmallV2Model faceNetSmallV2Model;

    public FaceRecognitionService(FaceNetSmallV2Model faceNetSmallV2Model){
        this.faceNetSmallV2Model=faceNetSmallV2Model;
    }

    @Override
    public boolean loadModel() throws Exception {

        boolean ret = false;
        try{
            computationGraph = faceNetSmallV2Model.init();
            logger.info(computationGraph.summary());
            ret = true;
        }catch (IOException e){
            logger.error("loadModel failed!",e);
        }
        return ret;

    }

    @Override
    public void registerNewMember(String memberId, String imagePath) throws IOException {
        INDArray read = read(imagePath);
        memberEncodingsMap.put(memberId, forwardPass(normalize(read)));
    }

    @Override
    public String whoIs(String imagePath) throws IOException {
        INDArray read = read(imagePath);
        INDArray encodings = forwardPass(normalize(read));
        double minDistance = Double.MAX_VALUE;
        String foundUser = "";
        for (Map.Entry<String, INDArray> entry : memberEncodingsMap.entrySet()) {
            INDArray value = entry.getValue();
            double distance = distance(value, encodings);
            logger.info("distance of " + entry.getKey() + " with " + new File(imagePath).getName() + " is " + distance);
            if (distance < minDistance) {
                minDistance = distance;
                foundUser = entry.getKey();
            }
        }
        if (minDistance > THRESHOLD) {
            foundUser = "Unknown user";
        }
        logger.info(foundUser + " with distance " + minDistance);
        return foundUser;
    }
}
