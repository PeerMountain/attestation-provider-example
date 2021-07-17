package com.kyc3.timestampap

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.net.URI

@SpringBootTest
abstract class AbstractIntegrationTest {

  companion object {

    val postgresImage = DockerImageName.parse("postgres:12")
    val postges = KPostgreSQLContainer(postgresImage)

    init {
      postges.start()

      URI(postges.jdbcUrl.replace("jdbc:", ""))
        .let {
          System.setProperty("datasource.host", it.host)
          System.setProperty("datasource.port", it.port.toString())
          System.setProperty("datasource.username", postges.username)
          System.setProperty("datasource.password", postges.password)
          System.setProperty("datasource.database", it.path.replace("/", ""))
        }

      System.setProperty("spring.datasource.url", postges.jdbcUrl)
      System.setProperty("spring.datasource.username", postges.username)
      System.setProperty("spring.datasource.password", postges.password)


    }
  }

  class KPostgreSQLContainer(dockerImageName: DockerImageName) :
    PostgreSQLContainer<KPostgreSQLContainer>(dockerImageName)
}