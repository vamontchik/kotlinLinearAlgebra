package math

import ui.Grid
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
    // TODO: code the linear algebra stuff for this by hand...

    // Compute SVD
    val (U, E, Vt) = svd(A)
}

private fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    println("A: $A")

    val At: Matrix = transpose(A)
    println("At: $At")

    return Triple(At, At, At) // dummy for compiler
}

private fun transpose(matrix: Matrix): Matrix {
    val temp: MutableList<Array<Double>> = mutableListOf()
    for (i in 0 until matrix.getRow(0).size) {
        temp.add(matrix.getCol(i))
    }
    return Matrix(temp.toTypedArray())
}
