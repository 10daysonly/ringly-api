package com.tendaysonly.bondify.cqrs

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler()
