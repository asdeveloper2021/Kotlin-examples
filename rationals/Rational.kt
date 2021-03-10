package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.math.BigInteger.ONE

/* inlix extension functions */

infix fun Int.divBy(d:Int) : Rational {
    return Rational.create(this.toBigInteger(),d.toBigInteger())
}

infix fun BigInteger.divBy(d:BigInteger) : Rational {
    return Rational.create(this,d)
}

infix fun Long.divBy(d:Long) : Rational {
    return Rational.create(this.toBigInteger(),d.toBigInteger())
}

/* Note the private modifier which will ensure data safety.
* Note the Comparable implementation*/
data class Rational
private constructor(val n:BigInteger, val d: BigInteger = ONE) : Comparable<Rational>{

    companion object {
        fun create(n: BigInteger, d: BigInteger) = normalize(n, d)
        private fun normalize(n: BigInteger, d: BigInteger): Rational {

            /* The task required we throw a IllegalStateException*/
            require(d != ZERO) {" Denominator cannot be 0"}
            /* This is to normalize the fraction. Dividing the numerator and denominator
            *  with the greatest common divisor 6/10 = 3/5*/
            val gcd = n.gcd(d)

            /*Carrying forward the - sign */
            val sign = d.signum().toBigInteger()
            return Rational(n / gcd * sign, d / gcd * sign)
        }
    }

    /* Not returning 2/1 instead returning 2*/
    override fun toString() = if(d == ONE)  "$n" else "$n/$d"


    /* operator implementations */
    operator fun unaryMinus() : Rational = create (-n,d)
    operator fun plus(other : Rational) : Rational = create ((n * other.d + d * other.n), (d * other.d))
    operator fun minus(other : Rational) : Rational = create ((n * other.d - d * other.n), (d * other.d))
    operator fun times(other: Rational) : Rational = create (n * other.n,d * other.d)
    operator fun div(other:Rational) : Rational = create (n * other.d,d * other.n)


    override fun compareTo(other: Rational): Int {
        return (n*other.d - d * other.n).signum()
    }
}

/* Note the fail() implementation in a local function and its return type Nothing*/
fun String.toRational(): Rational {

    fun fail(): Nothing = throw  IllegalArgumentException("Expecting rational in the" +
            " form of 'n/d' or 'n', was '$this'")
    if(!this.contains("/")) {
        val number = this.toBigIntegerOrNull() ?: fail()
        return Rational.create(number, ONE)
    }
    val (numText,denoText) = split("/")

    val num = numText.toBigIntegerOrNull() ?: fail()
    val den = denoText.toBigIntegerOrNull() ?: fail()

    return Rational.create(num, den)
}


fun main() {

    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)

}




