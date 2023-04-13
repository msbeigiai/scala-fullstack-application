package com.msbeigi.core.config

import cats.effect.Async
import ciris.{ConfigValue, env}
import com.msbeigi.core.domain._
import cats.implicits._

final case class ServerConfig(
                             port: Port,
                             host: Host
                             )

object ServerConfig {
  def serverConfig[F[_] : Async]: ConfigValue[F, ServerConfig] = (
    env("PORT").as[Int].default(8080),
    env("HOST").default("localhost")
  ).parMapN((port, host) => ServerConfig(Port(port), Host(host)))
}
