package com.tendaysonly.bondify.cqrs

interface QueryBus {

    fun <R> execute(query: Query<R>): R
}