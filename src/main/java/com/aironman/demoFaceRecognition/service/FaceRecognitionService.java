package com.aironman.demoFaceRecognition.service;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FaceRecognitionService extends FaceRecognitionUtils implements FaceRecognition  {

    @Autowired
    private FaceNetSmallV2Model faceNetSmallV2Model;
/*
    private static final double THRESHOLD = 0.57;
    private ComputationGraph computationGraph;
    private static final NativeImageLoader LOADER = new NativeImageLoader(96, 96, 3);
    private final HashMap<String, INDArray> memberEncodingsMap = new HashMap<>();
*/
    @Override
    public void loadModel() throws Exception {

        //faceNetSmallV2Model = new FaceNetSmallV2Model();
        computationGraph = faceNetSmallV2Model.init();
        System.out.println(computationGraph.summary());

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
            System.out.println("distance of " + entry.getKey() + " with " + new File(imagePath).getName() + " is " + distance);
            if (distance < minDistance) {
                minDistance = distance;
                foundUser = entry.getKey();
            }
        }
        if (minDistance > THRESHOLD) {
            foundUser = "Unknown user";
        }
        System.out.println(foundUser + " with distance " + minDistance);
        return foundUser;
    }
}
