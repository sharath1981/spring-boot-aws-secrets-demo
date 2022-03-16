package com.ryana.config;

import java.util.Objects;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryana.dto.AwsSecrets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppAwsSecretsConfig {

    @Value("${aws.secret-key}")
    private String secretKey;
    @Value("${aws.access-key}")
    private String accessKey;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.secret-name}")
    private String secretName;

    @Bean
    public AwsSecrets awsSecrets() {
        final var client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        final var getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        try {
            final var getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            if (Objects.nonNull(getSecretValueResult)) {
                final var mapper = new ObjectMapper();
                try {
                    return mapper.readValue(getSecretValueResult.getSecretString(), AwsSecrets.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (DecryptionFailureException e) {
            throw e;
        } catch (InternalServiceErrorException e) {
            throw e;
        } catch (InvalidParameterException e) {
            throw e;
        } catch (InvalidRequestException e) {
            throw e;
        } catch (ResourceNotFoundException e) {
            throw e;
        }
        return null;
    }
}
