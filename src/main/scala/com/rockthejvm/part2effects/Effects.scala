package com.rockthejvm.part2effects

import scala.concurrent.Future

object Effects {

  // pure functional programming: no side effects, compute expressions to obtan values
  // referential transparency: replace an expression with its value without changing the meaning of the program

  val printSomething: Unit = println("hi")
  val printSomething_v2 = printSomething // vs println("hi")
  // => the meaning of the program is different after the substitution
  // cannot replace an expression with its value if the expression has side effects

  // side effects are inevitable for useful programs

  // an effect is a data type
  // 1. the type signature has to describe the kind of computation that the effect will produce (when interpreted)
  // 2. the type signature has to descrive the value that will be calculated
  // 3. construction of the effect has to be separated from its execution

  // example: Option = possibly absent value, describes a pure calculation that may not return anything
  val anOption: Option[Int] = Option(42)

  // not an example: Future
  // describes an async computation, it computes a value of type A if it's successful
  // side effect are needed to run a future: a thread has to be scheduled/allocated
  import scala.concurrent.ExecutionContext.Implicits.global
  val aFuture: Future[Int] = Future(42) // thread scheduled right here, right now
  // => execution is not separated from construction :(

  // example: MyIO data type from the Monads lesson - it IS an effect type
  // - describes any computation that might produce side effects without errors
  // - calculates a value of type A, if it's successful
  // - side effects are required for the evaluation of () => A
  // - YES, the creation of MyIO does NOT produce the side effects on construction
  case class MyIO[A](unsafeRun: () => A) {
    def map[B](f: A => B): MyIO[B] =
      MyIO(() => f(unsafeRun()))

    def flatMap[B](f: A => MyIO[B]): MyIO[B] =
      MyIO(() => f(unsafeRun()).unsafeRun())
  }

  val anIO: MyIO[Int] = MyIO(() => {
    println("I'm writing something...")
    42
  })

  def main(args: Array[String]): Unit = {
    anIO.unsafeRun()
  }
}
