package com.kyc3.timestampap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TimestampApApplication

fun main(args: Array<String>) {
  runApplication<TimestampApApplication>(*args)
}
