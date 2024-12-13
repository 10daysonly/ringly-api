package com.tendaysonly.ringly.cqrs

import jakarta.annotation.PostConstruct
import org.springframework.aop.support.AopUtils
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.method.HandlerMethod
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.kotlinFunction

@Component
class SpringCommandBus(
    private val context: ApplicationContext
) : CommandBus {

    private val handlers = mutableMapOf<Class<*>, HandlerMethod>()

    @PostConstruct
    fun init() {

        this.context
            .getBeansWithAnnotation(Service::class.java)
            .values
            .filter { bean ->
                AopUtils.getTargetClass(bean)
                    .methods
                    .any { method ->
                        method.isAnnotationPresent(CommandHandler::class.java)
                    }
            }
            .forEach { bean ->

                AopUtils.getTargetClass(bean)
                    .methods
                    .filter { method ->
                        method.isAnnotationPresent(CommandHandler::class.java)
                    }
                    .forEach { method ->
                        method.kotlinFunction?.findAnnotation<CommandHandler>()?.let {
                            val parameterType = method.parameterTypes
                                .asList()
                                .find { type ->

                                    Command::class.java.isAssignableFrom(type)
                                }
                                ?: throw RuntimeException("@CommandHandler must have a command.")

                            handlers[parameterType] = HandlerMethod(bean, method)
                        }
                    }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R> execute(command: Command<R>): R {

        val handler = handlers[command.javaClass]
            ?: throw IllegalArgumentException("No handler found for ${command.javaClass}")

        try {

            return handler.method.invoke(handler.bean, command) as R
        } catch (e: InvocationTargetException) {
            
            throw e.targetException ?: e
        }
    }
}