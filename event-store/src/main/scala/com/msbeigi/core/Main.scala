package com.msbeigi.core

import cats.effect.{ExitCode, IO, IOApp}
import com.msbeigi.core.config.Config
import com.msbeigi.core.elasticsearch.{ElasticClientAlgebra, EsAlgebra}
import com.msbeigi.core.server.Server
import fs2.Stream

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = (for {
    config <- Stream.eval(Config.config[IO].load)
    restHighLevelClient <- Stream.resource(ElasticClientAlgebra.impl[IO](config.esConfig).client)
    server <- Server.stream[IO](config, restHighLevelClient)
  } yield server).compile.drain.as(ExitCode.Success)
}
