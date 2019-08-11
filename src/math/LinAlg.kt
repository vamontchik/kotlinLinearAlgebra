package math

import ui.Grid
import kotlin.math.pow

typealias Matrix = Array<Array<Double>>
typealias Vector = Array<Double>

fun getEquationVerticalUp(startX: Int, startY: Int, grid: Grid) {
    println("Calling getEquationVerticalUp with loc: startX: $startX, startY: $startY")

    // populate pairs lists with "pairs" of points,
    // where .first == x and .second == y, so the
    // pairs will be (x, y) coordinates
    val points: MutableList<Double> = ArrayList()
    var currentY = startY
    while (currentY > 0) {
        val square = grid.findSquare(startX, currentY)
        val numberForSquare = square.labelCorner.toDouble()
        points.add(numberForSquare)
        currentY -= grid.squareLength
    }

    // set-up the matrices
    val A: Matrix = Array(points.size) {
        val doubleRep = it.toDouble()
        arrayOf(doubleRep.pow(2.0), doubleRep, 1.0)
    }
    val b: Vector = points.toTypedArray()

    // use linear least squares to find coefficients (x vector)
    // for an equation of the form ax^2 + bx + c

    val (U, E, Vt) = svd(A)

    // TODO: finish least squares calculation...
}

private fun getRow(matrix: Matrix, index: Int): Vector {
    if (index < 0 || index >= getHeight(matrix)) {
        throw RuntimeException("Index is invalid! index: $index, num rows: " + getHeight(matrix))
    }
    return matrix[index]
}

private fun getCol(matrix: Matrix, index: Int): Vector {
    if (index < 0 || index >= getWidth(matrix)) {
        throw RuntimeException("Index is invalid! index: $index, num columns: " + getWidth(matrix))
    }
    return matrix.map { it[index] }.toTypedArray()
}

/**
 * Width = the amount of values in one inner array,
 * since we are looking row-major
 */
private fun getWidth(matrix: Matrix): Int {
    return matrix[0].size
}

/**
 * Height = the amount of inner arrays, since
 * we are looking row-major
 */
private fun getHeight(matrix: Matrix): Int {
    return matrix.size
}

private fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    printMatrix(A)

    val At: Matrix = transpose(A)
    printMatrix(At)

    val product: Matrix = multiply(At, A)
    printMatrix(product)

    // val eigenvalues: Vector = eigenvalues(product)
    // println("eigenvalues: $eigenvalues")

    // TODO: finish svd calculation...

    return Triple(At, At, At) // dummy for compiler
}

private fun printMatrix(matrix: Matrix) {
    println("Matrix:")
    for (row in 0 until getHeight(matrix)) {
        println(getRow(matrix, row).contentDeepToString())
    }
    println()
}

private fun transpose(matrix: Matrix): Matrix {
    return Array(getWidth(matrix)) {
        getCol(matrix, it)
    }
}

//private fun multiply(first: Matrix, second: Matrix): Matrix {
//    if (getWidth(first) != getHeight(second)) {
//        throw RuntimeException(
//            "Dimension mismatch! first.width: " + getWidth(first) +
//            ", second.height: " + getHeight(second)
//        )
//    }
//    return Array(first.size) {
//        multiply(getRow(first, it), getCol(second, it))
//    }
//}

private fun dot(first: Vector, second: Vector): Double {
    if (first.size != second.size) {
        throw RuntimeException(
            "Dimension mismatch! first.size: " + first.size +
            " second.size: " + second.size
        )
    }
    return first.mapIndexed { index, value -> value * second[index] }.sum()
}

private fun multiply(first: Matrix, second: Matrix): Matrix {
    // A      = m x n
    // B      = n x p
    // result = m x p

    val result: Matrix = Array(getHeight(first)) {
        Array(getWidth(second)) { 0.0 }
    }

    for (col in 0 until getWidth(second)) {
        for (row in 0 until getHeight(first)) {
            result[row][col] = dot(getRow(first, row), getCol(second, col))
        }
    }

    return result
}

//private fun eigenvalues(matrix: Matrix): Vector {
//    val amount: Int =
//        if (getWidth(matrix) < getHeight(matrix)) {
//            getWidth(matrix)
//        } else {
//            getHeight(matrix)
//        }
//
//    val values: Vector = Array(amount) {
//        powerIteration(matrix, it)
//    }
//
//    // TODO: finish eigenvalues calculation...
//
//    return arrayOf() // dummy for compiler
//}
//
//private fun powerIteration(matrix: Matrix, index: Int): Double {
//    var b: Vector = Array(getHeight(matrix)) {
//        Random.nextDouble()
//    }
//
//    for (i in 0 until 50) {
//        b = multiply(matrix, b)
//    }
//
//    // TODO: finish power iteration calculation...
//
//    return -1.0 // dummy for compiler
//}