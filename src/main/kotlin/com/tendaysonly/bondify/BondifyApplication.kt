package com.tendaysonly.bondify

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BondifyApplication

fun main(args: Array<String>) {
	runApplication<BondifyApplication>(*args)
}
