package com.msbeigi.core.service
import cats.effect.Async
import cats.effect.std.Console
import com.msbeigi.core.elasticsearch.EsAlgebra
import com.msbeigi.core.kafka.KafkaConsumerAlgebra
import fs2.Stream
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.RestHighLevelClient

trait IndexService[F[_]] {
  def persist: Stream[F, IndexResponse]
}

object IndexService {
  def impl[F[_] : Async : Console](kafkaConsumerAlgebra: KafkaConsumerAlgebra[F],
                         esAlgebra: EsAlgebra[F]): IndexService[F] = new IndexService[F] {
    override def persist: Stream[F, IndexResponse] = kafkaConsumerAlgebra
      .consume
      .evalMapChunk{ consumedEvents =>
        esAlgebra.store(consumedEvents.record.value)
      }
      .evalTapChunk { indexResponse =>
        Console[F].println(s"The index status is -> ${indexResponse.status()}")
      }
  }
}
