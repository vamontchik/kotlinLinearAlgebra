package example

import kotlin.math.floor
import kotlin.math.sqrt

fun spiralPath(grid: Grid) {
    val values = Values(/*x =*/ grid.width / 2,
        /*y =*/ grid.height / 2,
        /*leftToWalk =*/ 0,
        /*totalLength = */ 0,
        /*timeToIncrease =*/ false,
        /*count =*/ 0,
        /*direction =*/ Direction.UP
    )

    for (i in 0 until grid.listOfSquares.size) {
        setNumberAndBold(values.x, values.y, values.count.toString(), values.count, grid)
        updateValues(values, grid)
    }
}

private fun setNumberAndBold(x: Int, y: Int, setTo: String, count: Int, grid: Grid) {
    val currSquare = grid.findSquare(x, y)
    currSquare.labelCorner = setTo
    currSquare.isBold = isPerfectSquare(count)
}

private fun updateValues(values: Values, grid: Grid) {
    // update x, y positions
    when (values.direction) {
        Direction.UP -> values.y -= grid.squareLength
        Direction.LEFT -> values.x -= grid.squareLength
        Direction.DOWN -> values.y += grid.squareLength
        Direction.RIGHT -> values.x += grid.squareLength
    }

    // update number to draw on grid
    ++values.count

    // update next direction we should move, or
    // else update how many steps left to walk
    if (values.leftToWalk == 0) {
        when (values.direction) {
            Direction.UP -> values.direction = Direction.LEFT
            Direction.LEFT -> values.direction = Direction.DOWN
            Direction.DOWN -> values.direction = Direction.RIGHT
            Direction.RIGHT -> values.direction = Direction.UP
        }

        // only change total length every two rotations
        if (values.timeToIncrease) {
            ++values.totalLength
            values.timeToIncrease = false
        } else {
            values.timeToIncrease = true
        }

        // update the next amount of steps needed to be taken
        values.leftToWalk = values.totalLength
    } else {
        --values.leftToWalk
    }
}

private fun isPerfectSquare(i: Int): Boolean {
    val squareRootVal: Double = sqrt(i.toDouble())
    val compareVal: Double = floor(squareRootVal)
    val tolerance = 0.0001
    return ((squareRootVal - compareVal) < tolerance)
}