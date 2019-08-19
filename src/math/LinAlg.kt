package math

import ui.Grid
import kotlin.math.pow
import kotlin.math.sqrt
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

fun getRow(matrix: Matrix, index: Int): Vector {
//    if (index < 0 || index >= getHeight(matrix)) {
//        throw RuntimeException("Index is invalid! index: $index, num rows: " + getHeight(matrix))
//    }
    return matrix[index]
}

fun getCol(matrix: Matrix, index: Int): Vector {
//    if (index < 0 || index >= getWidth(matrix)) {
//        throw RuntimeException("Index is invalid! index: $index, num columns: " + getWidth(matrix))
//    }
    return matrix.map { it[index] }.toTypedArray()
}

/**
 * Width = the amount of values in one inner array,
 * since we are looking row-major
 */
fun getWidth(matrix: Matrix): Int {
    return matrix[0].size
}

/**
 * Height = the amount of inner arrays, since
 * we are looking row-major
 */
fun getHeight(matrix: Matrix): Int {
    return matrix.size
}

fun printMatrix(name: String, matrix: Matrix) {
    println("$name:")
    for (row in 0 until getHeight(matrix)) {
        println(getRow(matrix, row).contentDeepToString())
    }
}

fun transpose(matrix: Matrix): Matrix {
    return Array(getWidth(matrix)) {
        getCol(matrix, it)
    }
}

fun add(first: Matrix, second: Matrix): Matrix {
    val result: Matrix = zeroMatrix(getWidth(first), getHeight(first))

    for (row in 0 until getHeight(result)) {
        for (col in 0 until getWidth(result)) {
            result[row][col] = first[row][col] + second[row][col]
        }
    }

    return result
}

fun subtract(first: Matrix, second: Matrix): Matrix {
    val result: Matrix = zeroMatrix(getWidth(first), getHeight(first))

    for (row in 0 until getHeight(result)) {
        for (col in 0 until getWidth(result)) {
            result[row][col] = first[row][col] - second[row][col]
        }
    }

    return result
}

fun dot(first: Vector, second: Vector): Double {
//    if (first.size != second.size) {
//        throw RuntimeException(
//            "Dimension mismatch! first.size: " + first.size +
//            " second.size: " + second.size
//        )
//    }
    return first.mapIndexed { index, value -> value * second[index] }.sum()
}

fun multiply(first: Matrix, second: Matrix): Matrix {
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

fun multiply(first: Matrix, second: Vector): Vector {
    // A      = m x n
    // B      = 1 x n
    // Do: A * B^T, or each row multiplied by vector
    // result = m x 1
    return first.map { row -> dot(row, second) }.toTypedArray()
}

fun multiply(first: Vector, second: Matrix): Vector {
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
fun multiply(first: Vector, second: Vector): Matrix {
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

fun norm2(v: Vector): Double {
    val acc: Double = v.reduce { acc, value -> acc.plus(value.pow(2)) }
    return sqrt(acc)
}

// TODO: Fix this, since it doesn't produce the correct values...
fun eigen(matrix: Matrix): Pair<Vector, Matrix> {
     val amount: Int =
        if (getWidth(matrix) < getHeight(matrix)) {
            getWidth(matrix)
        } else {
            getHeight(matrix)
        }

    val eigenvalues: Vector = zeroVector(amount)
    val eigenvectors: Matrix = zeroMatrix(amount, amount)

    var currMatrix: Matrix = matrix
    var isFirstIteration = true
    for (i in 0 until amount) {
        printMatrix("currMatrix", currMatrix)

        eigenvectors[i] =
            if (isFirstIteration) {
                isFirstIteration = false
                powerIteration(currMatrix)
            } else {
                raleighQuotientEigenvector(currMatrix, eigenvalues[i - 1], eigenvectors[i - 1])
            }
        eigenvalues[i] = raleighQuotientEigenvalue(currMatrix, eigenvectors[i])

        currMatrix = deflation(currMatrix, eigenvectors[i], eigenvalues[i])
    }

    return Pair(eigenvalues, eigenvectors)
}

fun zeroVector(size: Int): Vector {
    return Array(size) { 0.0 }
}

fun zeroMatrix(width: Int, height: Int): Matrix {
    return Array(height) {
        Array(width) { 0.0 }
    }
}

fun deflation(currMatrix: Matrix, eigenvector: Vector, eigenvalue: Double): Matrix {
    val rightHandMatrix: Matrix = scalarMultiply(
        eigenvalue / norm2(eigenvector),
        multiply(eigenvector, eigenvector)
    )

    return subtract(currMatrix, rightHandMatrix)
}

fun scalarMultiply(scalar: Double, matrix: Matrix): Matrix {
    return matrix.map { it.map { it.times(scalar) }.toTypedArray() }.toTypedArray()
}

fun scalarDivide(scalar: Double, matrix: Matrix): Matrix {
    return matrix.map { it.map { it.div(scalar) }.toTypedArray() }.toTypedArray()
}

fun scalarDivide(scalar: Double, v: Vector): Vector {
    return v.map { value -> value / scalar }.toTypedArray()
}

fun identity(amount: Int): Matrix {
    val matrix: Matrix = zeroMatrix(amount, amount)
    for (i in 0 until amount) {
        matrix[i][i] = 1.0
    }
    return matrix
}

fun adjoint(matrix: Matrix): Matrix {
    val cofactorMatrix: Matrix = zeroMatrix(getWidth(matrix), getHeight(matrix))

    for (row in 0 until getWidth(matrix)) {
        for (col in 0 until getHeight(matrix)) {
            cofactorMatrix[row][col] = determinant(subMatrix(matrix, row, col))
        }
    }

    return transpose(cofactorMatrix)
}

fun subMatrix(matrix: Matrix, removeRow: Int, removeCol: Int): Matrix {
    val subMatrix: Matrix = zeroMatrix(getWidth(matrix) - 1, getHeight(matrix) - 1)

    var trueRow = 0
    var trueCol = 0
    for (row in 0 until getWidth(matrix)) {
        if (row == removeRow) continue
        for (col in 0 until getHeight(matrix)) {
            if (col == removeCol) continue
            subMatrix[trueRow][trueCol] = matrix[row][col]
            ++trueCol
        }
        trueCol = 0
        ++trueRow
    }

    return subMatrix
}

fun determinant(matrix: Matrix): Double {
//    if (getWidth(matrix) != getHeight(matrix)) {
//        throw RuntimeException("Non-square matrix cannot have a determinant!")
//    }

    // base case
    if (getWidth(matrix) == 2) {
        return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0]
    }

    val topRow: Vector = getRow(matrix, 0)
    var accumulator = 0.0
    var negativeSign = false
    for (i in 0 until getWidth(matrix)) {
        if (negativeSign) {
            accumulator -= topRow[i] * determinant(subMatrix(matrix, 0, i))
            negativeSign = false
        } else {
            accumulator += topRow[i] * determinant(subMatrix(matrix, 0, i))
            negativeSign = true
        }
    }
    return accumulator
}

fun inverse(matrix: Matrix): Matrix {
    return scalarDivide(determinant(matrix), adjoint(matrix))
}

fun raleighQuotientEigenvector(matrix: Matrix, eigenvalue: Double, eigenvector: Vector): Vector {
    val identity: Matrix = identity(getWidth(matrix))
    val scalarMultRes: Matrix = scalarMultiply(eigenvalue, identity)
    val inverse: Matrix = inverse(scalarMultRes)
    val numerator: Vector = multiply(inverse, eigenvector)
    val denominator: Double = norm2(numerator)
    println("For raleighQuotientEigenvector calc...")
    println("numerator: ${numerator.contentDeepToString()}")
    println("denominator: $denominator")
    return scalarDivide(denominator, numerator)
}

fun raleighQuotientEigenvalue(matrix: Matrix, eigenvector: Vector): Double {
    val numerator: Double = dot(eigenvector, multiply(matrix, eigenvector))
    val denominator: Double = dot(eigenvector, eigenvector)
    println("For raleighQuotientEigenvalue calc...")
    println("numerator: $numerator")
    println("denominator: $denominator")
    return numerator / denominator
}

fun powerIteration(matrix: Matrix): Vector {
    var b: Vector = Array(getHeight(matrix)) { Random.nextDouble() }
    var numerator: Vector
    var denominator: Double

    println("For powerIteration calc...")
    for (i in 0 until 50) {
        numerator = multiply(matrix, b)
        denominator = norm2(numerator)
        b = scalarDivide(denominator, numerator)

        println("b at $i: ${b.contentDeepToString()}")
    }

    return b
}

fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    printMatrix("A", A)

    val At: Matrix = transpose(A)
    printMatrix("At", At)

    val product: Matrix = multiply(At, A)
    printMatrix("product", product)

    val (eigenvalues, eigenvectors) = eigen(product)
    println("eigenvalues: ${eigenvalues.contentDeepToString()}")
    printMatrix("eigenvectors", eigenvectors)

    // TODO: finish svd calculation...

    return Triple(A, A, A) // dummy for compiler
}
