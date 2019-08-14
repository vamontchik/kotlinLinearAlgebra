package math

import ui.Grid
import kotlin.math.pow
import kotlin.math.sqrt
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

private fun printMatrix(name: String, matrix: Matrix) {
    println("$name:")
    for (row in 0 until getHeight(matrix)) {
        println(getRow(matrix, row).contentDeepToString())
    }
}

private fun transpose(matrix: Matrix): Matrix {
    return Array(getWidth(matrix)) {
        getCol(matrix, it)
    }
}

private fun add(first: Matrix, second: Matrix): Matrix {
    val result: Matrix = zeroMatrix(getWidth(first), getHeight(first))

    for (row in 0 until getHeight(result)) {
        for (col in 0 until getWidth(result)) {
            result[row][col] = first[row][col] + second[row][col]
        }
    }

    return result
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

    val result: Matrix = zeroMatrix(getWidth(second), getHeight(first))

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
    // B      = 1 x n
    // Do: A * B^T, or each row multiplied by vector
    // result = m x 1
    return first.map { row -> dot(row, second) }.toTypedArray()
}

private fun multiply(first: Vector, second: Matrix): Vector {
    // A = 1 x m
    // B = m x n
    // Do: A * B, or vector multiplied by each column in matrix
    // result = 1 x n

    val result: Vector = Array(getWidth(second)) { 0.0 }
    for (i in 0 until getWidth(second)) {
        result[i] = dot(first, getCol(second, i))
    }
    return result
}

// since dot(vector, vector) can be used for a vector * vector
// multiplication where the result reduces to a single value,
// use this method for the other way (ie. when vector * vector
// would turn into a matrix)
private fun multiply(first: Vector, second: Vector): Matrix {
    // first: m x 1
    // second: n x 1
    // Do: first * second^T, or each value in each vector multiplied once together
    // result: m x n

    val result: Matrix = zeroMatrix(second.size, first.size)

    for (row in 0 until first.size) {
        for (col in 0 until second.size) {
            result[row][col] = first[row] * second[col]
        }
    }

    return result
}

private fun norm(v: Vector): Double {
    val acc: Double = v.reduce { acc, value -> acc.plus(value.pow(2)) }
    return sqrt(acc)
}

private fun eigen(matrix: Matrix): Pair<Vector, Matrix> {
     val amount: Int =
        if (getWidth(matrix) < getHeight(matrix)) {
            getWidth(matrix)
        } else {
            getHeight(matrix)
        }

    val eigenvalues: Vector = zeroVector(amount)
    val eigenvectors: Matrix = zeroMatrix(amount, amount)

    // TODO: add in Hotelling's deflation
    //       so that we can find second, third, ...
    //       eigenvectors / eigenvalues , not just
    //       the most dominant multiple times ^^
    //       link: http://www.robots.ox.ac.uk/~sjrob/Teaching/EngComp/ecl4.pdf
    //       link2: https://math.stackexchange.com/questions/768882/power-method-for-finding-all-eigenvectors
    var currMatrix: Matrix = matrix
    for (i in 0 until amount) {
        printMatrix("currMatrix", currMatrix)
        val eigenvector: Vector = powerIteration(currMatrix)
        val eigenvalue: Double = raleighQuotient(currMatrix, eigenvector)

        eigenvectors[i] = eigenvector
        eigenvalues[i] = eigenvalue

        currMatrix = deflation(currMatrix, eigenvector, eigenvalue)
    }

    return Pair(eigenvalues, eigenvectors)
}

private fun zeroVector(size: Int): Vector {
    return Array(size) { 0.0 }
}

private fun zeroMatrix(width: Int, height: Int): Matrix {
    return Array(height) {
        Array(width) { 0.0 }
    }
}

private fun deflation(currMatrix: Matrix, eigenvector: Vector, eigenvalue: Double): Matrix {
    val rightHandMatrix: Matrix = scalarMultiply(
        eigenvalue / norm(eigenvector),
        multiply(eigenvector, eigenvector)
    )

    return add(currMatrix, rightHandMatrix)
}

private fun scalarMultiply(scalar: Double, matrix: Matrix): Matrix {
    return matrix.map { it.map { it.times(scalar) }.toTypedArray() }.toTypedArray()
}

private fun scalarDivide(scalar: Double, v: Vector): Vector {
    return v.map { value -> value / scalar }.toTypedArray()
}

private fun raleighQuotient(matrix: Matrix, eigenvector: Vector): Double {
    val numerator: Double = dot(multiply(eigenvector, matrix), eigenvector)
    val denominator: Double = dot(eigenvector, eigenvector)
    return numerator / denominator
}

private fun powerIteration(matrix: Matrix): Vector {
    var b: Vector = Array(getHeight(matrix)) { Random.nextDouble() }
    var numerator: Vector
    var denominator: Double

    for (i in 0 until 50) {
        numerator = multiply(matrix, b)
        denominator = norm(numerator)
        b = scalarDivide(denominator, numerator)
    }

    return b
}

private fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    printMatrix("A", A)

    val At: Matrix = transpose(A)
    printMatrix("At", At)

    val product: Matrix = multiply(At, A)
    printMatrix("product", product)

    val (eigenvalues, eigenvectors) = eigen(product)
    println("eigenvalues: " + eigenvalues.contentDeepToString())
    printMatrix("eigenvectors", eigenvectors)

    // TODO: finish svd calculation...

    return Triple(A, A, A) // dummy for compiler
}
