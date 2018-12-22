package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.factory.FaceEngineFactory;
import cn.hephaestus.smartmeetingroom.service.FaceEngineService;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
public class FaceEngineServiceImpl implements FaceEngineService {

    public String appId = "4ZzSSJDHnfAoUAG8UQPhWyGgnbHM6gnm5wufCjQm2fGc";

    public String sdkKey = "4L2GBLKuJ5mfdSesdvNsyWvtB5TVX8TsNRyVFHzAhBQX";

    public Integer threadPoolSize = 5;


    //private Integer passRate = 80;

    private ExecutorService executorService;

    //private GenericObjectPool<FaceEngine> extractFaceObjectPool;
    private GenericObjectPool<FaceEngine> compareFaceObjectPool;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);
        //extractFaceObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, FunctionConfiguration.builder().supportFaceDetect(true).supportFaceRecognition(true).supportAge(true).supportGender(true).build()), poolConfig);//底层库算法对象池
        compareFaceObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, FunctionConfiguration.builder().supportFaceRecognition(true).build()), poolConfig);//底层库算法对象池
    }

    private int plusHundred(Float value) {
        BigDecimal target = new BigDecimal(value);
        BigDecimal hundred = new BigDecimal(100f);
        return target.multiply(hundred).intValue();

    }

    @Override
    public Integer compareFaceFeature(FaceFeature targetFaceFeature, FaceFeature sourceFaceFeature) throws Exception {
        FaceEngine faceEngine = compareFaceObjectPool.borrowObject();
        FaceSimilar faceSimilar = new FaceSimilar();
        faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        Integer similarValue = plusHundred(faceSimilar.getScore());//获取相似值
        return similarValue;
    }
}
