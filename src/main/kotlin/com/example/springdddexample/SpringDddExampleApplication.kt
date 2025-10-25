package com.example.springdddexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example.springdddexample"])
class SpringDddExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringDddExampleApplication>(*args)
}
