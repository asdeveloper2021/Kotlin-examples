package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers1(): Set<Driver> {
    val set:MutableSet<Driver> = mutableSetOf()
    for(d in allDrivers){
        if(!this.trips.any {it.driver.name == d.name})
            set.add(d)
    }
    return set
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers1(minTrips: Int): Set<Passenger> {

        val map:MutableMap<Passenger,Int> = mutableMapOf()
        val set:MutableSet<Passenger> = mutableSetOf()
        if(minTrips == 0)
            return allPassengers
        for(i in trips.indices){
            for(passenger in trips[i].passengers){
                if(map.containsKey(passenger))
                    map.put(passenger, map.get(passenger)!!.plus(1))
                else
                    map.put(passenger, 1)
            }
        }
        var new =  map.filter { it.value >= minTrips }
        set.addAll(new.keys)
        return set
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers1(driver: Driver): Set<Passenger> {
    println("${trips.size}")
    val fTrips = trips.filter { it.driver.equals(driver) }
    println("${fTrips.size}")

    val map:MutableMap<Passenger,Int> = mutableMapOf()
    val set:MutableSet<Passenger> = mutableSetOf()

    for(i in fTrips.indices){
        for(passenger in fTrips[i].passengers){
            if(map.containsKey(passenger))
                map.put(passenger, map.get(passenger)!!.plus(1))
            else
                map.put(passenger, 1)
        }
    }
    var new =  map.filter { it.value > 1 }
    set.addAll(new.keys)
    return set
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers1(): Set<Passenger> {
    val fTrips = trips.filter { it.discount != null && it.discount > 0}
    val map:MutableMap<Passenger,Int> = mutableMapOf()
    val set:MutableSet<Passenger> = mutableSetOf()

    for(i in fTrips.indices){
        for(passenger in fTrips[i].passengers){
            if(map.containsKey(passenger))
                map.put(passenger, map.get(passenger)!!.plus(1))
            else
                map.put(passenger, 1)
        }
    }
    var new =  map.filter{it.value > getTotalTrips(it.key)}
    val addAll = set.addAll(new.keys)
    return set
}

fun TaxiPark.getTotalTrips(key: Passenger): Int {
    var count = 0
    for(trip in trips){
        for(passenger in trip.passengers){
            if(passenger.equals(key)){
                count++
            }
        }
    }
    println("${count/2}")
    return count /2
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod1(): IntRange? {
     if(trips != null && trips.size == 0)
         return null

     val range = trips.map { IntRange((it.duration/10 * 10),(it.duration/10 * 10) + 9) }
     val map:MutableMap<IntRange,Int> = mutableMapOf()

     for(trip in trips){
         val currentDuration = trip.duration
         for(i in range.indices){
             if(range[i].contains(currentDuration)){
                 if(map.containsKey(range[i]))
                     map.put(range[i],map.get(range[i])!!.plus(1))
                     else
                         map.put(range[i],1)

             }
         }
     }
     //val keyTest = map.maxByOrNull { it.value }!!.key
     val keyTest = map.maxBy { it.value }!!.key
     println(keyTest)
     return keyTest
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple1(): Boolean {
    // find total income of all drivers
    // get 80%
    // total no of driver , 20 %
    if(trips.size == 0)
        return false

    //val totalCost = trips.sumOf { it.cost }
    val totalCost = trips.sumByDouble { it.cost }

    println("$totalCost")
    val threshhold = 0.8 * totalCost

    val totalNoOfDrivers = allDrivers.size
    val dThreshold = (0.2 * totalNoOfDrivers).toInt()
    println("$dThreshold")

    val map:MutableMap<Driver,Double> = mutableMapOf()

    for(driver in allDrivers){
        for(trip in trips){
            if(trip.driver.equals(driver)){
                if(map.containsKey(driver))
                    map.put(driver, map.get(driver)!!.plus(trip.cost))
                else
                    map.put(driver, trip.cost)
            }
        }
    }

    //val maximumEarning = map.maxOf { it.value }
    val maximumEarning = map.maxBy { it.value }!!.value

    println("$maximumEarning")
    /* 20 % of all drivers is 1, just checking with maximum income*/
    if(dThreshold == 1) {
        if (maximumEarning >= threshhold)
            return true
    }

    /* If 20 percent is more than one , getting income of all those
    * drivers*/
    var sortedList = map.values.toList()
    sortedList = sortedList.sortedDescending()

    var runningIncome = 0.0
    for(i in 1..dThreshold){
        runningIncome += sortedList.get(i-1)
    }

    return runningIncome >= threshhold

}