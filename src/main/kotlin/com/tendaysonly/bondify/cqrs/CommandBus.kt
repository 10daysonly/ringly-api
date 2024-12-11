package com.tendaysonly.bondify.cqrs

interface CommandBus {
    
    fun <R> execute(command: Command<R>): R
}