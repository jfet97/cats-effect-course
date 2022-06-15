package com.rockthejvm.utils

import cats.effect.IO

extension [A](io: IO[A])
  // add debug method to all IOs
  def debug: IO[A] = for {
    a <- io
    t = Thread.currentThread().getName
    _ = println(s"[$t] $a")
  } yield a
