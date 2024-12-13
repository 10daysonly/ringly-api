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
class SpringQueryBus(
    private val context: ApplicationContext
) : QueryBus {

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
                        method.isAnnotationPresent(QueryHandler::class.java)
                    }
            }
            .forEach { bean ->

                AopUtils.getTargetClass(bean)
                    .methods
                    .filter { method ->
                        method.isAnnotationPresent(QueryHandler::class.java)
                    }
                    .forEach { method ->
                        method.kotlinFunction?.findAnnotation<QueryHandler>()?.let {
                            val parameterType = method.parameterTypes
                                .asList()
                                .find { type ->

                                    Query::class.java.isAssignableFrom(type)
                                }
                                ?: throw RuntimeException("@QueryHandler must have a query.")

                            handlers[parameterType] = HandlerMethod(bean, method)
                        }
                    }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R> execute(query: Query<R>): R {

        val handler = handlers[query.javaClass]
            ?: throw IllegalArgumentException("No handler found for ${query.javaClass}")

        try {

            return handler.method.invoke(handler.bean, query) as R
        } catch (e: InvocationTargetException) {

            throw e.targetException ?: e
        }
    }
}