package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    /* You can print these to check what each API is doing*/
    /*val sortedDriversIncome: List<Double> = trips
            .groupBy {it.driver}
            .map { (_, tripsByDriver) -> tripsByDriver.sumByDouble { it.cost }}
            .sortedDescending()

    println(trips.flatMap {it.passengers})
    println(trips.flatMap {it.passengers}.groupBy { it })
    println(trips.map {it.passengers}.get(0))*/


    if(trips!= null && trips.isEmpty())
        return allDrivers
    return allDrivers.filter { d ->trips.none {it.driver == d} }.toSet()
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    return allPassengers.
                filter { p -> trips.count { p in it.passengers} >= minTrips}.toSet()
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    /* filter -> will get trips taken by this driver
    * flatMap - > list of all passengers who took these trips
    * groupBy -> group passengers , where each group size will tell how many trips this passenger took
    * filterValues -> keep only those passengers who took more than one trip
    * keys : all the passengers who took more than one trip */

    return trips.filter { trip-> trip.driver == driver}
            .flatMap (Trip::passengers)
            .groupBy { passenger -> passenger }
            .filterValues { group -> group.size > 1 }.keys
}


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val (tripsWithDiscount, tripsWithoutDiscount) = trips.partition { trip -> trip.discount != null }
    return allPassengers.filter { p->
        tripsWithDiscount.count{p in it.passengers} > tripsWithoutDiscount.count{p in it.passengers}
    }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return trips.groupBy {it.duration/10 * 10..(it.duration/10 * 10) + 9}
            .toList()
            .maxBy {(_,group)->group.size}?.first
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    // find total income of all drivers
    // get 80%
    // total no of driver , 20 %
    if(trips.isEmpty())
        return false

    /* Total income using property reference*/
    val totalIncome = trips.sumByDouble (Trip::cost)

    /*Drivers income in sorted list*/
    var sortedDriverIncome :List<Double> =
            trips.groupBy { it.driver }
                    .map {(_, tripCost) -> tripCost.sumByDouble {it.cost }}
                    .sortedDescending()

    val numberOfDrivers = (0.2 * allDrivers.size).toInt()
    val incomeByTopDrivers = sortedDriverIncome.take(numberOfDrivers).sum()

    return incomeByTopDrivers >= 0.8 * totalIncome
}