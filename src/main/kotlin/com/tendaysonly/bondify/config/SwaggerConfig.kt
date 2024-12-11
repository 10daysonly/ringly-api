package com.tendaysonly.bondify.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author oognuyh
 */
@Configuration
class SwaggerConfig(
    @Value("\${spring.application.name}") private val title: String,
    @Value("\${spring.application.version}") private val version: String,
    @Value("\${server.servlet.context-path:/}") private val contextPath: String
) {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .components(Components())
        .addServersItem(Server().url(this.contextPath))
        .info(
            Info()
                .title(this.title)
                .version(this.version)
        )
}