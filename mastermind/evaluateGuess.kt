package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {

    /* zip combines the two strings and then we count if the two characters match*/
    val rightPositions = secret.zip(guess).count { it.first == it.second}

    /* Common letters will be the minimum of the count of matching letters in the two strings*/
    val commonLetters = "ABCDEF".sumBy { ch ->
        Math.min(secret.count{ ch == it }, guess.count{ ch == it})
    }

    /* Wrong position signifies that the color is right but position is wrong, so by
    * common letters we have all correct colors and then subtract the rightPositions*/

    return Evaluation(rightPositions , commonLetters - rightPositions)
}
