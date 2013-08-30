package jessitron

import scala.util.Random.shuffle

/**
 * NotTooRandom:
 *   provides sources of Doubles that are evenly distributed.
 *   This is useful for testing code that uses randoms.
 */
object NotTooRandom {

   def fixedLength(howMany: Int): Iterator[Double] = {
      val n = howMany - 1
      shuffle(1.0 +: Range(0,n).map(_.toDouble/n)).iterator
   }

   def evenDistribution(implicit arrange: ListArranger = SHUFFLED): Iterator[Double] = {
     val stream = Stream.cons(0.0,
                  Stream.from(0) map powerOfTwo flatMap (perPowerOfTwo andThen arrange)
                  )
     stream.iterator
   }

   def powerOfTwo(n: Int): Int = Math.pow(2,n).toInt
   def hasBeenInHereBefore(n: Int) = n % 2 == 0

   def perPowerOfTwo: Int => Seq[Double] = (n: Int) =>
      Range(1,n + 1).filterNot(hasBeenInHereBefore).map(_.toDouble / n).toList


   type ListArranger = Seq[Double] => Seq[Double]

   val SHUFFLED: ListArranger = shuffle

   val NOT_SHUFFLED: ListArranger = (a) => a

}


