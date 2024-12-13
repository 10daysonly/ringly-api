package com.tendaysonly.ringly.cqrs

interface QueryBus {

    fun <R> execute(query: Query<R>): R
}