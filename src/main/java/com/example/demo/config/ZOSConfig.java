package com.example.demo.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author 何凯
 * @date 2024/3/8 0008
 */
@Slf4j
@Configuration
public class ZOSConfig {

    @Value("${zos.accessKey}")
    private String access_key;

    @Value("${zos.secretKey}")
    private String secret_key;

    @Value("${zos.endPoint}")
    private String end_point;

    private AmazonS3 s3_client;

    @Bean
    public AmazonS3 amazonS3() {
        return s3_client;
    }

    @PostConstruct
    public void init() {
        try {
            // 当使用 https 协议且采用自签名认证时，需关闭证书检查
            // System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
            // 使用凭据和配置建立连接
            AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
            ClientConfiguration client_configuration = new ClientConfiguration();

            // 使用 V2 签名时，采用 "S3SignerType"
            // client_configuration.setSignerOverride("S3SignerType");
            // 使用 V4 签名时，采用 "AWSS3V4SignerType"
            client_configuration.setSignerOverride("AWSS3V4SignerType");
            // 连接默认使用 HTTPS 协议，使用 HTTP 协议连接时需要显式指定
            // client_configuration.setProtocol(Protocol.HTTP);
            s3_client = AmazonS3ClientBuilder.standard() //
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) //
                .withClientConfiguration(client_configuration) //
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(end_point, "")) //
                .disableChunkedEncoding() //
                .build();
            log.info("zos connect successful");
        } catch (Exception e) {
            log.error("zos connect fail: ", e);
        }

    }
}
