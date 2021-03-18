package io.vaan.asare

import cats.effect.IOApp
import cats.effect.{ ExitCode, IO }
import org.http4s.server.blaze.BlazeServerBuilder
import scala.concurrent.ExecutionContext
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import io.vaan.asare.config.Config
import io.vaan.asare.modules._

// TODO: generate API docs (tapir, rho)
object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      logger    <- Slf4jLogger.create[IO]
      algrebras <- Algebras.make[IO]()
      programs  <- Programs.make[IO](algrebras)
      api       <- HttpApi.make[IO](algrebras, programs)
      _ <-
        BlazeServerBuilder[IO](ExecutionContext.global)
          .bindHttp(
            port = Config.httpPort,
            host = Config.httpHost
          )
          .withHttpApp(api.httpApp)
          .serve
          .compile
          .drain
    } yield ExitCode.Success
}
