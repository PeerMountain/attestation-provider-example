import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.5.2"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.5.20"
  kotlin("plugin.spring") version "1.5.20"
}

group = "com.kyc3"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val testContainerVersion = "1.15.3"

repositories {
  mavenCentral()
}

sourceSets {
  main {
    java {
      srcDir("build/generated/source/java/main")
    }
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

  implementation("org.flywaydb:flyway-core")

  implementation("org.igniterealtime.smack:smack-tcp:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-core:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-im:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-extensions:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-java7:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-experimental:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-bosh:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-xmlparser-xpp3:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-xmlparser-stax:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-streammanagement:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-websocket:4.5.0-alpha1-SNAPSHOT")
  implementation("org.igniterealtime.smack:smack-websocket-okhttp:4.5.0-alpha1-SNAPSHOT")

  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  implementation("org.springframework:spring-jdbc")

  implementation("org.web3j:core:5.0.0")
  implementation("com.squareup.okhttp3:okhttp:4.9.1")

  implementation("org.apache.commons:commons-lang3:3.12.0")

  implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final")

  implementation("com.kyc3:oracle-definitions:6747f5e")
  implementation("org.ehcache:ehcache:3.1.3")

  runtimeOnly("io.r2dbc:r2dbc-postgresql")
  implementation("org.postgresql:postgresql:42.2.23")

  implementation("commons-io:commons-io:2.8.0")

  implementation("com.muquit.libsodiumjna:libsodium-jna:1.0.4") {
    exclude ("org.slf4j", "slf4j-log4j12")
  }

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")

  testImplementation("org.testcontainers:testcontainers:$testContainerVersion")
  testImplementation("org.testcontainers:postgresql:$testContainerVersion")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

task<Exec>("generateWeb3J") {
  commandLine("sh", "utils/generate.sh")
}
