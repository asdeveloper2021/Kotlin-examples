package nicestring

fun String.isNice1(): Boolean {
    val listString:List<String> = listOf("bu","ba","be")
    val listVowels:List<Char> = listOf('a','e','i','o','u')

    fun String.firstCondition(list:List<String>):Boolean {
        for( s in list){
            if(this.contains(s))
                return false
        }
        return true
    }

    fun String.secondCondition(list:List<Char>):Boolean {
        var count = 0
        val charArray = this.toCharArray()

        count = charArray.count { it in list}
        return count >= 3
    }

    fun String.thirdCondition():Boolean{
        val pairs = this.zipWithNext().count { it.first == it.second}
        return pairs > 0
    }

    return firstCondition(listString) && secondCondition(listVowels)
            || secondCondition(listVowels) && thirdCondition()
            || firstCondition(listString) && thirdCondition()


}