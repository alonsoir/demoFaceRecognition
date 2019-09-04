package com.aironman.demoFaceRecognition.service;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.graph.vertex.GraphVertex;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 * @author aironman
 * @author klevis
 */
public abstract class FaceRecognitionUtils {


    public static final double THRESHOLD = 0.57;
    public FaceNetSmallV2Model faceNetSmallV2Model;
    public ComputationGraph computationGraph;
    public static final NativeImageLoader LOADER = new NativeImageLoader(96, 96, 3);
    public final HashMap<String, INDArray> memberEncodingsMap = new HashMap<>();

    public INDArray transpose(INDArray indArray1) {
        INDArray one = Nd4j.create(new int[]{1, 96, 96});
        one.assign(indArray1.get(NDArrayIndex.point(0), NDArrayIndex.point(2)));
        INDArray two = Nd4j.create(new int[]{1, 96, 96});
        two.assign(indArray1.get(NDArrayIndex.point(0), NDArrayIndex.point(1)));
        INDArray three = Nd4j.create(new int[]{1, 96, 96});
        three.assign(indArray1.get(NDArrayIndex.point(0), NDArrayIndex.point(0)));
        return Nd4j.concat(0, one, two, three).reshape(new int[]{1, 3, 96, 96});
    }

    public INDArray read(String pathname) throws IOException {
        opencv_core.Mat imread = opencv_imgcodecs.imread(new File(pathname).getAbsolutePath(), 1);
        INDArray indArray = LOADER.asMatrix(imread);
        return transpose(indArray);
    }

    public INDArray forwardPass(INDArray indArray) {
        Map<String, INDArray> output = computationGraph.feedForward(indArray, false);
        GraphVertex embeddings = computationGraph.getVertex("encodings");
        INDArray dense = output.get("dense");
        embeddings.setInputs(dense);
        INDArray embeddingValues = embeddings.doForward(false, LayerWorkspaceMgr.builder().defaultNoWorkspace().build());
        System.out.println("dense =                 " + dense);
        System.out.println("encodingsValues =                 " + embeddingValues);
        return embeddingValues;
    }

    public double distance(INDArray a, INDArray b) {
        return a.distance2(b);
    }

    public static INDArray normalize(INDArray read) {
        return read.div(255.0);
    }


}
