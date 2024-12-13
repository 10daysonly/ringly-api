package com.tendaysonly.ringly.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.tendaysonly.ringly.exception.ErrorResponse
import com.tendaysonly.ringly.security.AuthenticationFilter
import com.tendaysonly.ringly.util.TokenUtils
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * @author oognuyh
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val mapper: ObjectMapper,
    private val tokenUtils: TokenUtils
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .httpBasic { customizer ->
            customizer.disable()
        }
        .csrf { customizer ->
            customizer.disable()
        }
        .cors { customizer ->
            customizer.disable()
        }
        .formLogin { customizer ->
            customizer.disable()
        }
        .sessionManagement { customizer ->
            customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .addFilterAt(
            AuthenticationFilter(tokenUtils = tokenUtils),
            UsernamePasswordAuthenticationFilter::class.java
        )
        .exceptionHandling { customizer ->
            customizer
                .authenticationEntryPoint { _, response, e ->

                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        mapper.writeValueAsString(ErrorResponse(e.localizedMessage))
                    )
                }
                .accessDeniedHandler { _, response, e ->

                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        mapper.writeValueAsString(ErrorResponse(e.localizedMessage))
                    )
                }
        }
        .authorizeHttpRequests { customizer ->
            customizer
                .anyRequest()
                .permitAll()
        }
        .build()
}