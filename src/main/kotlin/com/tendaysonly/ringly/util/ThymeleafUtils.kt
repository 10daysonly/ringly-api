package com.tendaysonly.ringly.util

import org.thymeleaf.context.Context

fun Context.variable(key: String, value: Any?): Context {

    value?.let {

        this.setVariable(key, it)
    }

    return this
}