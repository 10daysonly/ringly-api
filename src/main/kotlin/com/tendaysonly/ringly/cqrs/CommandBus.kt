package com.tendaysonly.ringly.cqrs

interface CommandBus {

    fun <R> execute(command: Command<R>): R
}