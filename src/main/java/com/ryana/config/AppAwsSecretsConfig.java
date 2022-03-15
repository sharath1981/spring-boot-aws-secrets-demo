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
    private String SECRET_KEY;
    @Value("${aws.access-key}")
    private String ACCESS_KEY;
    @Value("${aws.region}")
    private String REGION;
    @Value("${aws.secret-name}")
    private String SECRET_NAME;

    @Bean
    public AwsSecrets awsSecrets() {
        final var client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .build();

        final var getSecretValueRequest = new GetSecretValueRequest().withSecretId(SECRET_NAME);
        try {
            final var getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            if (Objects.nonNull(getSecretValueResult)) {
                final var mapper = new ObjectMapper();
                try {
                    final var readValue = mapper.readValue(getSecretValueResult.getSecretString(), AwsSecrets.class);
                    return readValue;
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
