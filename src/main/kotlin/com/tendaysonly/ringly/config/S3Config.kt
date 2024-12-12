package com.tendaysonly.ringly.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author oognuyh
 */
@Configuration
@EnableConfigurationProperties(S3Config.S3Properties::class)
class S3Config(
    private val properties: S3Properties
) {

    @ConfigurationProperties(prefix = "s3")
    data class S3Properties(

        val endpoint: String = "https://kr.object.ncloudstorage.com",

        val regionName: String = "kr-standard",

        val accessKey: String,

        val secretKey: String,

        val bucketName: String
    )

    @Bean
    fun amazonS3Client(): AmazonS3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(
            EndpointConfiguration(
                this.properties.endpoint,
                this.properties.regionName
            )
        )
        .withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    this.properties.accessKey,
                    this.properties.secretKey
                )
            )
        )
        .build()
}