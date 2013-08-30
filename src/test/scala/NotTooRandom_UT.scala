package jessitron

import org.scalatest._

class NotTooRandom_UT extends FunSpec with ShouldMatchers {

  import NotTooRandom._

   describe("An even distribution of fixed length") {

     it("Uses the two extremes as the first two") {
       val two = fixedLength(2).toList

       two should contain(1.0)
       two should contain(0.0)
     }

     it("Divides up the space in between") {
        val fourths = fixedLength(5).toList

        fourths should contain(0.0)
        fourths should contain(0.25)
        fourths should contain(0.5)
        fourths should contain(0.75)
        fourths should contain(1.0)
     }

     it("Gives a good percentage") {
        val hundred = fixedLength(100)

        hundred.filter(_ <= .39).size should equal(39)
     }
   }

   describe("A mostly even distribution") {
      it("Gives me 0.0 and 1.0 first") {
         val two = evenDistribution.take(2).toList

         two should contain(0.0)
         two should contain(1.0)
      }

      it("Divides up the space perfectly for a power of two plus one") {
        val fourths = evenDistribution.take(5).toList

        fourths should contain(0.0)
        fourths should contain(0.25)
        fourths should contain(0.5)
        fourths should contain(0.75)
        fourths should contain(1.0)
      }

      it("Shuffles each sublist by default") {
        // we'll always get 0.0 and 1.0 and 0.5. The next two will
        // be 0.25 and 0.75 in either order
        def theFourthElement = evenDistribution.take(4).toList.last
        val manyFourthElements = Stream.continually(theFourthElement).take(100)

        manyFourthElements should not be (100 * 0.25)
      }

      it("Doesn't shuffle when I tell it not to") {
        val fourths = evenDistribution(NOT_SHUFFLED).take(7).toList

        fourths should be(Seq(0.0, 1.0, 0.5, 0.25, 0.75, 0.125, 0.375))
      }

   }

   describe("The shuffler shuffles") {
     it("moves stuff around eventually") {
        // a list of 1,0,0,0,0,0...
        val s = Stream.cons(1.0, Stream.continually(0.0)).take(100).toList

        val shuffledHeads = Stream.continually(SHUFFLED(s).head).take(100)

        shuffledHeads.sum should not be (100)

     }
   }

}
