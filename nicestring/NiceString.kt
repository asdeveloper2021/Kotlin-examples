package nicestring

fun String.isNice(): Boolean {
    val listString:Set<String> = setOf("bu","ba","be")
    val listVowels:Set<Char> = setOf('a','e','i','o','u')

    val noMatchingSubstring =
        listString.none { this.contains(it)}

    val noVowels =
        count { it in listVowels} >= 3

    val noNeighborRepeat =
        zipWithNext().any { it.first == it.second}

    return listOf(noMatchingSubstring,noVowels,noNeighborRepeat).
                count { it } >= 2


}