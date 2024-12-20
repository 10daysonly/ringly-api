package com.tendaysonly.ringly.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * @author oognuyh
 */
@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {

        return Jackson2ObjectMapperBuilder.json()
            .modules(
                JavaTimeModule(),
                KotlinModule.Builder()
                    .build()
            )
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
    }
}