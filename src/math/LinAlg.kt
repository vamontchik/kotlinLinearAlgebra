package math

import ui.Grid
import kotlin.math.pow
import kotlin.random.Random

private typealias Matrix = Array<Array<Double>>
private typealias Vector = Array<Double>

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

private fun printMatrix(matrix: Matrix) {
    println("Matrix:")
    for (row in 0 until getHeight(matrix)) {
        println(getRow(matrix, row).contentDeepToString())
    }
}

private fun transpose(matrix: Matrix): Matrix {
    return Array(getWidth(matrix)) {
        getCol(matrix, it)
    }
}

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

// the only meaningful way to multiply a vector by
// a matrix is to multiply the vector by each column or
// row of the matrix. Whether or not it is by each column
// or by each row is determined by which goes first:
//
// matrix * vector == each row of matrix * vector
// vector * matrix == vector * each col of matrix

private fun multiply(first: Matrix, second: Vector): Vector {
    // A      = m x n
    // B      = n x 1
    // result = m x 1

    val result: Vector = Array(first.size) { 0.0 }

    for (i in 0 until getHeight(first)) {
        result[i] = dot(getRow(first, i), second)
    }

    return result
}

private fun multiply(first: Vector, second: Matrix): Vector {
    // A = m x 1
    // B = m x n
    // do: A^T * B
    // result = 1 x n, but it's really expressed as "n x 1,"
    //          since vectors here don't maintain orientation

    val result: Vector = Array(second[0].size) { 0.0 }
    for (i in 0 until getWidth(second)) {
        result[i] = dot(first, getCol(second, i))
    }
    return result
}

private fun scalarDivide(v: Vector, divisor: Double): Vector {
    for (i in 0 until v.size) {
        v[i] /= divisor
    }
    return v
}

private fun norm(v: Vector): Double {
    return v.reduce { acc, value -> acc.plus(value) }
}

private fun eigen(matrix: Matrix): Pair<Vector, Matrix> {
     val amount: Int =
        if (getWidth(matrix) < getHeight(matrix)) {
            getWidth(matrix)
        } else {
            getHeight(matrix)
        }

    val eigenvalues: Vector = Array(amount) { 0.0 }
    val eigenvectors: Matrix = Array(amount) {
        Array(amount) { 0.0 }
    }

    // TODO: add in shifting of the matrix
    //       so that we can get all the eigenvalues,
    //       not just the most dominant one multiple times...
    for (i in 0 until amount) {
        val eigenvector: Vector = powerIteration(matrix)
        val eigenvalue: Double = raleighQuotient(matrix, eigenvector)

        eigenvectors[i] = eigenvector
        eigenvalues[i] = eigenvalue
    }

    return Pair(eigenvalues, eigenvectors)
}

private fun raleighQuotient(matrix: Matrix, eigenvector: Vector): Double {
    val halfNumerator: Vector = multiply(eigenvector, matrix)
    val numerator: Double = dot(halfNumerator, eigenvector)
    val denominator: Double = dot(eigenvector, eigenvector)
    return numerator / denominator
}

private fun powerIteration(matrix: Matrix): Vector {
    var b: Vector = Array(getHeight(matrix)) {
        Random.nextDouble()
    }

    var numerator: Array<Double>
    var denominator: Double
    for (i in 0 until 50) {
        numerator = multiply(matrix, b)
        denominator = norm(numerator)
        b = scalarDivide(numerator, denominator)
    }

    return b
}

private fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    printMatrix(A)

    val At: Matrix = transpose(A)
    printMatrix(At)

    val product: Matrix = multiply(At, A)
    printMatrix(product)

    val (eigenvalues, eigenvectors) = eigen(product)
    println("eigenvalues: " + eigenvalues.contentDeepToString())
    printMatrix(eigenvectors)

    // TODO: finish svd calculation...

    return Triple(A, A, A) // dummy for compiler
}
