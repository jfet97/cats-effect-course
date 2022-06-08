package com.rockthejvm.part2effects

import cats.effect.IO
import scala.io.StdIn

object IOIntroduction {

  // IO
  val ourFirstIO: IO[Int] = IO.pure(42) // the argumetn should not have side effect, it's evaluated eagerly
  val aDelayedIO: IO[Int] = IO.delay({
    println("I'm producing an integer")
    42
  })

  val aDelayedIO_v2: IO[Int] = IO { // apply = delay
    println("I'm producing an integer")
    42
  }

  val improvedMeaningOfLife = ourFirstIO.map(_ * 2)
  val printedMeaningOfLife = ourFirstIO.flatMap(mol => IO.delay(println(mol)))

  // mapN - combine IO effects as tuples
  import cats.syntax.apply._
  val combinedMeaningOfLife = (ourFirstIO, improvedMeaningOfLife).mapN(_ + _)

  def smallProgram(): IO[Unit] = for {
    line1 <- IO(StdIn.readLine())
    line2 <- IO(StdIn.readLine())
    _ <- IO.delay(println(line1 + line2))
  } yield ()

  def smallProgram2(): IO[Unit] =
    (IO(StdIn.readLine()), IO(StdIn.readLine())).mapN(_ + _).map(println)

  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global // IO runtime (platform)

    // do this at the end of the world

    // println(aDelayedIO.unsafeRunSync())
    println(smallProgram().unsafeRunSync())
    // println(combinedMeaningOfLife.unsafeRunSync())
  }
}
