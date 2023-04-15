package com.msbeigi.core.server


import cats.effect.{Async, ExitCode}
import cats.effect.std.Console
import com.msbeigi.core.config.Config
import com.msbeigi.core.elasticsearch.EsAlgebra
import com.msbeigi.core.http.routes.EventsRoutes
import com.msbeigi.core.kafka.{KafkaConsumerAlgebra, KafkaProducerAlgebra}
import com.msbeigi.core.service.IndexService
import org.http4s.blaze.server.BlazeServerBuilder
import fs2.Stream
import org.elasticsearch.client.RestHighLevelClient
import org.http4s.Method
import org.http4s.server.middleware.{CORS, CORSConfig}

object Server {
  def stream[F[_]: Async: Console](
                                    config: Config,
                                    restHighLevelClient: RestHighLevelClient
                                  ): fs2.Stream[F, ExitCode] = for {
    _ <- Stream.eval(Console[F].println("Starting the server 🚀"))

    kafkaProducerAlgebra: KafkaProducerAlgebra[F] = KafkaProducerAlgebra
      .impl[F](config.kafkaConfig)
    kafkaConsumerAlgebra: KafkaConsumerAlgebra[F] = KafkaConsumerAlgebra
      .impl[F](config.kafkaConfig)
    esAlgebra: EsAlgebra[F]       = EsAlgebra.impl[F](config.esConfig, restHighLevelClient)
    indexService: IndexService[F] = IndexService.impl[F](kafkaConsumerAlgebra, esAlgebra)

    _ <- Stream.eval(esAlgebra.createIndex)

    corServiceV2 = CORS.policy
      .withAllowMethodsIn(Set(Method.POST))
      .withAllowCredentials(false)
      .withAllowOriginAll
      .apply(EventsRoutes[F](kafkaProducerAlgebra).router)

    /*corService = CORS(
      EventsRoutes[F](kafkaProducerAlgebra).router,
      CORSConfig.default
        .withAllowedOrigins(Set("http://localhost:3000"))
        .withAllowedMethods(Some(Set(Method.POST)))
    )*/

    sever <- BlazeServerBuilder[F]
      .bindHttp(
        config.serverConfig.port.value,
        config.serverConfig.host.value
      )
      .withHttpApp(corServiceV2.orNotFound)
      .serve
      .concurrently(indexService.persist)
  } yield sever
}