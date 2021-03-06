package cn.hephaestus.smartmeetingroom.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.*;

public class COSUtils {

    private static final String secretId="AKIDHWcgptGqmstxXFg0zUuPrRGFCD6ssfgi";
    private static final String secretKey="HcccYgls2Eew7tdQxNk3KcIA1iJza1XR";
    private static final String region_name="ap-guangzhou";
    private static final String bucketName="smartmeetingroom-1257009269";

    //上传文件
    public static String addFile(String key,InputStream inputStream){
        COSCredentials cred = new BasicCOSCredentials(secretId,secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region_name));
        COSClient cosClient = new COSClient(cred, clientConfig);
        PutObjectRequest objectRequest=new PutObjectRequest(bucketName,key,inputStream,new ObjectMetadata());
        try {
           PutObjectResult objectResult=cosClient.putObject(objectRequest);
        }catch (Exception e){
            return null;
        }finally {
            try {
                inputStream.close();
            }catch (Exception e){
                LogUtils.getExceptionLogger().error(e.toString());
            }
        }
        return "https://smartmeetingroom-1257009269.cos.ap-guangzhou.myqcloud.com/"+key;
    }
}
