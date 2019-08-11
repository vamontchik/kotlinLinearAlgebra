package math

import ui.Grid
import java.lang.RuntimeException
import kotlin.math.pow
import kotlin.random.Random

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
    if (index < 0 || index >= getWidth(matrix)) {
        throw RuntimeException("Index is invalid! index: $index, matrix size: " + getWidth(matrix))
    }
    return matrix[index]
}

private fun getCol(matrix: Matrix, index: Int): Vector {
    if (index < 0 || index >= getHeight(matrix)) {
        throw RuntimeException("Index is invalid! index: $index, matrix size: " + getHeight(matrix))
    }
    return matrix.map { it[index] }.toTypedArray()
}

private fun getWidth(matrix: Matrix): Int {
    return matrix.size
}

private fun getHeight(matrix: Matrix): Int {
    return matrix[0].size
}

private fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    println("A: $A")

    val At: Matrix = transpose(A)
    println("At: $At")

    val product: Matrix = dot(At, A)
    println("product: $product")

    val eigenvalues: Vector = eigenvalues(product)
    println("eigenvalues: $eigenvalues")

    // TODO: finish svd calculation...

    return Triple(At, At, At) // dummy for compiler
}

private fun transpose(matrix: Matrix): Matrix {
    return Array(getHeight(matrix)) {
        getCol(matrix, it)
    }
}

private fun dot(first: Matrix, second: Matrix): Matrix {
    if (first.size != second[0].size) {
        throw RuntimeException(
            "Dimension mismatch! first.width: " + first.size +
            ", second.height: " + second[0].size
        )
    }
    return Array(first.size) {
        multiply(getRow(first, it), getCol(second, it))
    }
}

private fun multiply(first: Vector, second: Vector): Vector {
    if (first.size != second.size) {
        throw RuntimeException("Dimension mismatch! first.size: " + first.size + " second.size: " + second.size)
    }
    return Array(first.size) { first[it] * second[it] }
}

private fun eigenvalues(matrix: Matrix): Vector {
    val amount: Int =
        if (getWidth(matrix) < getHeight(matrix)) {
            getWidth(matrix)
        } else {
            getHeight(matrix)
        }

    val values: Vector = Array(amount) {
        powerIteration(matrix, it)
    }

    // TODO: finish eigenvalues calculation...

    return arrayOf() // dummy for compiler
}

private fun powerIteration(matrix: Matrix, index: Int): Double {
    val initial: Vector = Array(getHeight(matrix)) {
        Random.nextDouble()
    }

    // TODO: finish power iteration calculation...

    return -1.0 // dummy for compiler
}