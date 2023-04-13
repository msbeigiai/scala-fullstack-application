package com.msbeigi.core.config

import cats.effect.Async
import ciris.{ConfigValue, env}
import cats.implicits._
import com.msbeigi.core.domain.{BootstrapServer, GroupId, Topic}

case class KafkaConfig(bootstrapServer: BootstrapServer, groupId: GroupId, topic: Topic)

object KafkaConfig {
  def kafkaConfig[F[_] : Async]: ConfigValue[F, KafkaConfig] = (
    env("BOOTSTRAP_SERVER").as[String].default("localhost:9092"),
    env("GROUP_ID").as[String].default("consumerGroup"),
    env("KAFKA_TOPIC").as[String].default("testTopic")
  ).parMapN((port, groupId, topic) => KafkaConfig(BootstrapServer(port), GroupId(groupId), Topic(topic)))
}
