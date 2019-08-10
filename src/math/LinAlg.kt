package math

import ui.Grid
import java.lang.RuntimeException
import kotlin.collections.ArrayList
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
    val A: Array<Array<Double>> = Array(points.size) {
        val doubleRep = it.toDouble()
        arrayOf(doubleRep.pow(2.0), doubleRep, 1.0)
    }
    val b: Array<Double> = points.toTypedArray()

    // use linear least squares to find coefficients (x vector)
    // for an equation of the form ax^2 + bx + c
    // TODO: code the linear algebra stuff for this by hand...

    // Compute SVD
    val (U, E, Vt) = svd(A)
}

private fun svd(A: Array<Array<Double>>):
        Triple<Array<Array<Double>>, Array<Array<Double>>, Array<Array<Double>>> {
    println("A: " + A.contentDeepToString())

    val At: Array<Array<Double>> = transpose(A)
    println("At: " + At.contentDeepToString())

    return Triple(At, At, At) // dummy for compiler
}

private fun transpose(matrix: Array<Array<Double>>): Array<Array<Double>> {
    val matrixT: MutableList<Array<Double>> = mutableListOf()
    for (i in 0 until matrix[0].size) {
        matrixT.add(getCol(i, matrix))
    }
    return matrixT.toTypedArray()
}

private fun getRow(index: Int, matrix: Array<Array<Double>>): Array<Double> {
    if (index < 0 || index >= matrix.size) {
        throw RuntimeException("Index is invalid! index: $index, matrix size: " + matrix.size)
    }
    return matrix[index]
}

private fun getCol(index: Int, matrix: Array<Array<Double>>): Array<Double> {
    if (index < 0 || index >= matrix[0].size) {
        throw RuntimeException("Index is invalid! index: $index, matrix size: " + matrix[0].size)
    }
    return matrix.map { it[index] }.toTypedArray()
}
