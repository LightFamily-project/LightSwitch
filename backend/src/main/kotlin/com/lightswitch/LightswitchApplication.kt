package com.lightswitch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LightswitchApplication

fun main(args: Array<String>) {
    runApplication<LightswitchApplication>(*args)
}
