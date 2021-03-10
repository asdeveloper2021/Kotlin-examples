package mastermind

data class Evaluation1(val rightPosition: Int, val wrongPosition: Int)

fun String.findCount1(c :Char):Int {
    return this.filter {it == c}.count()
}

fun String.getIndexes1(c: Char): List<Int> {
    val list = mutableListOf<Int>()
    for(i in this.indices){
        if(this[i] == c)
            list.add(i)
    }
    return list
}


fun evaluateGuess1(secret: String, guess: String): Evaluation {
    val visited: MutableSet<Char> = mutableSetOf()
    var indexS = 0
    var right = 0
    var wrong = 0

    //DBFF
    //FFDD

    while(indexS < secret.length){
        val currentCharacter = secret[indexS]
        if(visited.contains(currentCharacter)){
            indexS++
            continue
        }
        val contains = guess.contains(currentCharacter)

        if(contains) {
            val countInS = secret.findCount(currentCharacter)
            val countInG = guess.findCount(currentCharacter)

            visited.add(currentCharacter)

            if(countInG == countInS) {
                val list = secret.getIndexes(currentCharacter)
                for(i in list){
                    if(guess[i] == currentCharacter)
                        right++
                    else
                        wrong++
                }
            } else if(countInG < countInS) {
                val list = guess.getIndexes(currentCharacter)
                for(i in list){
                    if(secret[i] == currentCharacter)
                        right++
                    else
                        wrong++
                }
            } else if(countInG > countInS) {
                val list = secret.getIndexes(currentCharacter)
                for(i in list){
                    if(guess[i] == currentCharacter)
                        right++
                    else
                        wrong++
                }

            }
        }
        indexS++
    }
    return Evaluation(right,wrong)
}

//{

// }
