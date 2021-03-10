package board

import board.Direction.*

open class Board(val sides:Int) : SquareBoard{

    private val rows:IntRange = 1..sides
    private val cols:IntRange = 1..sides

    /* Use of lateinit*/
    private lateinit var allCells:Collection<Cell>

    init {
        /* Initialization for this class. Please note we have to
        * create a list of cells only once. We dont want to keep
        * creating the collection of cells, so this will ensure that
        * the collection is created only once */
         println("Init called")
         fun createallCells(): Collection<Cell>{
             val rowsList = rows.toList()
             val colsList = cols.toList()
             val list: MutableList<Cell> = mutableListOf()
             for(i in rowsList) {
                 for(j in colsList){
                     list.add(Cell(i,j))
                 }
             }
             val list2:Collection<Cell> = list
             return list2
        }

        allCells = createallCells()
    }

    override val width: Int
        get() = sides

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        val size = getAllCells().filter { (row,col) -> row == i && col == j }.size
        if(size == 0)
            return null
        else
            return getCell(i,j)
    }

    override fun getCell(i: Int, j: Int): Cell {
        println(getAllCells().filter { (row,col) -> row == i && col == j }.elementAt(0))
        return getAllCells().filter { (row,col) -> row == i && col == j }.elementAt(0)
    }

    override fun getAllCells(): Collection<Cell> {
        return allCells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val start = jRange.first
        val end = jRange.last
        /* Note the use of = here when rows = cols*/
        if(start <= end)
            return getAllCells().filter { (row,col) -> row == i && col <= end }
        else {
            return getAllCells().
                    filter { (row, col) -> row == i && col <= start }
                    .sortedByDescending {it.j}
        }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val start = iRange.first
        val end = iRange.last
        if(start <= end)
            return getAllCells().filter { (row,col) -> col == j && row >= start && row <= end}
        else
            return getAllCells().filter { (row,col) -> col == j && row <= start && row >= end}.sortedByDescending { it.i }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
            return when(direction) {
                UP -> getCellOrNull(i-1,j)
                DOWN ->getCellOrNull(i+1,j)
                RIGHT ->getCellOrNull(i,j+1)
                LEFT ->getCellOrNull(i,j-1)
            }
    }
}

fun createSquareBoard(width: Int): SquareBoard {
    val board = Board(width)
    return board
}

/* Note the <T> here which indicates that T is a type generic argument.*/
/* Note the syntax to implement two interfaces */
class ValuesBoard<T>(override val width: Int) : Board(width), GameBoard<T> {

    val mapValues:MutableMap<Cell, T?> = mutableMapOf<Cell, T?>()

    init{
        for(cell in getAllCells())
            mapValues.put(cell,null)
    }
    override fun get(cell: Cell): T? {
        return mapValues.get(cell)
    }

    override fun set(cell: Cell, value: T?) {
        /* Kotlin syntax*/
        mapValues += cell to value
        /* Java syntax*/
        //mapValues.put(cell,value)
    }

    /* Note the use of invoke on predicate*/
    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        mapValues.filterValues { predicate.invoke(it) }.keys

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return mapValues.filterValues { predicate.invoke(it) }.keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return mapValues.values.any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return mapValues.values.all(predicate)
    }
}

/* <T> represents generic/type parameter.It means that we have to pass <T>
* parameter. It is like declaring the type parameter before using it in GameBoard<T> */
fun <T> createGameBoard(value: Int): GameBoard<T> {
    return ValuesBoard(value)
}

