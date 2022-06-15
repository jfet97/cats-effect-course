package com.rockthejvm.part2effects

import cats.effect.{ExitCode, IO, IOApp}

import scala.io.StdIn

object IOApps {
  // def di program
  val program: IO[Unit] = for {
    line <- IO(StdIn.readLine())
    _ <- IO(println(s"You've just written: $line"))
  } yield ()
}

object TestApp {
  import IOApps._

  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global // thread pool, IO runtime
    program.unsafeRunSync()
  }
}

// IOApp è un trait di cats-effect
object FirstCEApp extends IOApp {
  import IOApps._

  override def run(args: List[String]) =
    // Replaces the result of this IO with the given value
    // because an IO[ExitCode] is needed
    program.as(ExitCode.Success)
}

object MySimpleApp extends IOApp.Simple {
  // A simplified version of IOApp for applications which ignore their process arguments and always produces ExitCode.
  // Success (unless terminated exceptionally or interrupted)
  import IOApps._

  override def run = program
}
