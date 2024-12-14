package com.tendaysonly.ringly.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.tendaysonly.ringly.controller.dto.MessageResponse
import com.tendaysonly.ringly.cqrs.CommandBus
import com.tendaysonly.ringly.exception.ErrorResponse
import com.tendaysonly.ringly.exception.GatheringNotFoundException
import com.tendaysonly.ringly.security.InvalidTokenException
import com.tendaysonly.ringly.service.usecase.PostMessageUseCase
import com.tendaysonly.ringly.util.TokenUtils
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor
import java.io.IOException

/**
 * @author oognuyh
 */
@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val participantRegistry: ParticipantWebSocketSessionRegistry,
    private val mapper: ObjectMapper,
    private val tokenUtils: TokenUtils,
    private val commandBus: CommandBus
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {

        registry
            .addHandler(
                GatheringWebSocketHandler(
                    participantRegistry = participantRegistry,
                    mapper = mapper,
                    commandBus = commandBus
                ),
                "/ws/gatherings/*"
            )
            .addInterceptors(
                ParticipantWebSocketHandshakeHandlerInterceptor(
                    tokenUtils = tokenUtils
                )
            )
            .setAllowedOrigins("*")
    }

    class ParticipantWebSocketHandshakeHandlerInterceptor(
        private val tokenUtils: TokenUtils
    ) : HttpSessionHandshakeInterceptor() {

        override fun beforeHandshake(
            request: ServerHttpRequest,
            response: ServerHttpResponse,
            wsHandler: WebSocketHandler,
            attributes: MutableMap<String, Any>
        ): Boolean {

            if (request is ServletServerHttpRequest) {
                val uri = request.uri

                return try {

                    val gatheringId = parseGatheringId(uri.path)
                    val token = parseToken(uri.query)
                    val email = tokenUtils.decode(token).subject

                    attributes["gatheringId"] = gatheringId
                    attributes["participant"] = email

                    true
                } catch (e: InvalidTokenException) {

                    response.setStatusCode(HttpStatus.UNAUTHORIZED)

                    false
                } catch (e: Exception) {
                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)

                    false
                }
            }

            return false
        }

        private fun parseGatheringId(path: String): String = path.substringAfterLast("/")

        private fun parseToken(query: String?): String {

            query ?: throw InvalidTokenException("Token is missing")

            return query.split("&")
                .map { it.split("=") }
                .find { it.size == 2 && it[0] == "token" }
                ?.get(1)
                ?: throw InvalidTokenException("Token is missing")
        }
    }

    @Component
    class ParticipantWebSocketSessionRegistry {

        private val store = mutableMapOf<String, MutableMap<String, WebSocketSession>>()

        fun register(session: WebSocketSession) {

            val gatheringId = session.attributes["gatheringId"]?.toString()
                ?: throw IllegalArgumentException("Missing gathering id")
            val participant = session.attributes["participant"]?.toString()
                ?: throw IllegalArgumentException("Missing participant id")

            store
                .getOrPut(gatheringId) { mutableMapOf() }
                .also { gathering -> gathering[participant] = session }
        }

        fun unregister(session: WebSocketSession) {

            val gatheringId = session.attributes["gatheringId"]?.toString()
                ?: throw IllegalArgumentException("Missing gathering id")
            val participant = session.attributes["participant"]?.toString()
                ?: throw IllegalArgumentException("Missing participant id")
            val gathering = this.store.getOrPut(gatheringId) { mutableMapOf() }

            gathering.remove(participant)

            if (gathering.isEmpty()) {

                store.remove(gatheringId)
            }
        }

        fun getGathering(gatheringId: String): MutableMap<String, WebSocketSession> {

            return store[gatheringId] ?: mutableMapOf()
        }
    }

    data class Payload(
        val type: PayloadType,
        val data: ObjectNode
    ) {

        enum class PayloadType {

            @JsonProperty("game_result")
            GAME_RESULT,

            @JsonProperty("message")
            MESSAGE,

            @JsonProperty("message_deleted")
            MESSAGE_DELETED,

            @JsonProperty("notification")
            NOTIFICATION,

            @JsonProperty("system")
            SYSTEM
        }
    }

    class GatheringWebSocketHandler(
        private val mapper: ObjectMapper,
        private val commandBus: CommandBus,
        private val participantRegistry: ParticipantWebSocketSessionRegistry
    ) : WebSocketHandler {

        private val log = LoggerFactory.getLogger(javaClass)

        override fun afterConnectionEstablished(session: WebSocketSession) {

            participantRegistry
                .register(session)
        }

        override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {

            val gatheringId =
                session.attributes["gatheringId"]?.toString() ?: throw GatheringNotFoundException()

            if (message is TextMessage) {

                try {

                    val payload = mapper.readValue(message.payload, Payload::class.java)

                    if (payload.type == Payload.PayloadType.MESSAGE) {

                        val savedMessage = this.commandBus.execute(
                            PostMessageUseCase.PostMessageCommand(
                                gatheringId = session.attributes["gatheringId"].toString(),
                                sender = session.attributes["participant"].toString(),
                                content = payload.data.at("/content")
                                    .asText(""),
                                recipient = payload.data.at("/recipient")
                                    .asText("all"),
                            )
                        )

                        participantRegistry.getGathering(gatheringId = gatheringId)
                            .entries
                            .forEach { entry ->

                                entry.value.sendMessage(
                                    TextMessage(
                                        mapper.writeValueAsString(
                                            Payload(
                                                type = payload.type,
                                                data = mapper.convertValue(
                                                    MessageResponse.from(savedMessage),
                                                    ObjectNode::class.java
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                    }
                } catch (e: IOException) {

                    log.error(e.message, e)

                    session.sendMessage(TextMessage(mapper.writeValueAsString(ErrorResponse(message = e.message))))
                }
            }
        }

        override fun handleTransportError(session: WebSocketSession, exception: Throwable) {

            session.close(CloseStatus.SERVER_ERROR)
        }

        override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {

            this.participantRegistry
                .unregister(session)
        }

        override fun supportsPartialMessages(): Boolean = false
    }
}