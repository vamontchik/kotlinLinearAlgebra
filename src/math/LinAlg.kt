package math

import ui.Grid
import java.lang.RuntimeException
import kotlin.math.pow

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
    val fillForA: Array<Array<Double>> = Array(points.size) {
        val doubleRep = it.toDouble()
        arrayOf(doubleRep.pow(2.0), doubleRep, 1.0)
    }
    val A = Matrix(fillForA)
    val b = Vector(points.toTypedArray())

    // use linear least squares to find coefficients (x vector)
    // for an equation of the form ax^2 + bx + c

    val (U, E, Vt) = svd(A)

    // TODO: finish least squares calculation...
}

private fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    println("A: $A")

    val At: Matrix = transpose(A)
    println("At: $At")

    val product: Matrix = multiply(At, A)
    println("product: $product")

    // TODO: finish svd calculation...

    return Triple(At, At, At) // dummy for compiler
}

private fun transpose(matrix: Matrix): Matrix {
    val temp: Array<Array<Double>> = Array(matrix.height) {
        matrix.getCol(it)
    }
    return Matrix(temp)
}

private fun multiply(first: Matrix, second: Matrix): Matrix {
    if (first.width != second.height) {
        throw RuntimeException("Dimension mismatch! first.width: " + first.width + " second.height: " + second.height)
    }
    val temp: Array<Array<Double>> = Array(first.width) {
        multiply(first.getRow(it), second.getCol(it))
    }
    return Matrix(temp)
}

private fun multiply(first: Array<Double>, second: Array<Double>): Array<Double> {
    if (first.size != second.size) {
        throw RuntimeException("Dimension mismatch! first.size: " + first.size + " second.size: " + second.size)
    }
    return Array(first.size) { first[it] * second[it] }
}