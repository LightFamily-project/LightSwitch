package com.lightswitch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class LightswitchApplication

fun main(args: Array<String>) {
    runApplication<LightswitchApplication>(*args)
}
