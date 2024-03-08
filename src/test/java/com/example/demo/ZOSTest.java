package com.example.demo;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author 何凯
 * @date 2024/3/8 0008
 */
@Slf4j
public class ZOSTest extends AbstractBaseTest {

    @Autowired
    private AmazonS3 s3_client;

    @Test
    void testDownload() {
        String file_name = "index.zip";
        String bucket_name = "bucket-6843";
        GetObjectRequest request = new GetObjectRequest(bucket_name, file_name);
        S3Object object = s3_client.getObject(request);

        String file_save_path = "D:\\upload" + File.separator + file_name;
        try (OutputStream output_stream = Files.newOutputStream(Paths.get(file_save_path));
            S3ObjectInputStream objectContent = object.getObjectContent();) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = objectContent.read(buffer)) > 0) {
                output_stream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("file download error, ", e);
        }
    }

    @Test
    void testUpload() {
        String file_path = "D:\\upload\\sdk_java.zip";
        String bucket_name = "bucket-6843";
        PutObjectRequest request = new PutObjectRequest(bucket_name, "test/sdk_java.zip111", new File(file_path));
        PutObjectResult result = s3_client.putObject(request);
        String md5 = result.getContentMd5();
        System.out.println(md5);
    }
}
