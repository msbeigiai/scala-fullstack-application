ThisBuild / scalaVersion := "2.13.6"

val eventStore = (project in file ("./event-store"))
  .settings(moduleName := "event-store")
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.http4sDsl,
      Dependencies.http4sBlazeClient,
      Dependencies.http4sBlazeServer,
      Dependencies.catsEffect,
      Dependencies.ciris,
      Dependencies.circeGenericExtra,
      Dependencies.http4sCirce,
      Dependencies.kafka,
      Dependencies.elasticSearchClient
    ) ++ Dependencies.circe
  )

