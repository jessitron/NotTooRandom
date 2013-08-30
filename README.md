NotTooRandom
============

Provides an evenly distributed series of Doubles between 0 and 1, for the purpose of testing code that bases its behavior on random numbers.

When code doesn't behave the same way every time, it's tough to unit
test it. Sometimes we want a random element in the code. For instance,
maybe we want an error report now and then when a certain problem
occurs, but every time would be too much. How can we test that something
occurs 10% of the time?

Code that uses random number generation to determine behavior will never
be perfectly predictable. We can run a method many times and expect to
get close to the desired percentage, but not always. When tests fail
occasionally for legitimate reasons, then you get flickers in the
continuous integration build, and then people start ignoring build
failures. "Oh, that one just fails sometimes. No big deal." When you
hear "The build is red sometimes, no big deal," that's a red flag right
there. Let's avoid that.

Code that is random in production should not be random in tests. One way
to avoid this is to pass in a seed for random number generation. This
gets you the same random number generation each run, but it's cryptic
("Why should I get exactly 36% response from this test?") and brittle to
refactoring. If the order of the checks in the tested code changes, the
test results for that seed will change.

If you know how many times you'll call it
---

Here's one way to make the tests nice.

Step 1. Pass in a source of random numbers, defaulting to really-random.
(In Scala this is easiest with by-name parameters; in another language,
pass a function.)

A simple class whose method returns true 40% of the time:

    class Sometimes(rand: => Double = Random.nextDouble) {
     def shouldI : Boolean = rand < 0.4
    }

Step 2. In the test, create a sequence that contains numbers evenly
distributed from 0 to 1, in a random order:

    val notTooRandom: Iterator[Double] = NotTooRandom.evenDistribution(100)

Step 3. Use its next() function
to produce the random number. In Scala, notTooRandom.next() is evaluated
every time the rand parameter is accessed.

    val sometimes = new Sometimes(notTooRandom.next())

Step 4. Be sure the test evaluates the random generator until the
sequence is exhausted. Check that the random behavior occurred the
expected number of times.

    def results = Stream.continually(sometimes.shouldI).take(100)
    results.count(t => t) should equal(40)

This achieves randomness in the order of the triggers, while
guaranteeing the aggregate frequency that we want to test. I like that
it expresses what I want to test ("it returns true 40% of the time")
without specifying which forty of the hundred calls returned true.

If you don't know how many times you'll call it
---

You can also get a sequence that's pretty evenly distributed for an
unknown length.

    val forever: Iterator[Double] = NotTooRandom.evenDistribution

This gives you an iterator that divides the interval between 0 and 1
into smaller and smaller pieces as you draw from it. For any power of
two plus one, it will be perfectly even.

By default, evenDistribution shuffles the progressively smaller
sections, so that it'll work reasonably well for in-between lengths.
If you'd rather get the same sequence every time, tell it not to:

    val theSameForever: Iterator[Double] = NotTooRandom.evenDistribution(NOT_SHUFFLED)

boring history
===

This came from a blog post:
http://blog.jessitron.com/2013/08/a-trick-for-deterministic-testing-of.html
