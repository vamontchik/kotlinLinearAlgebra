package math

import java.lang.RuntimeException

// TODO: change from primary constructor to two constructors,
//       where one takes in an Array<Array<Double>> and saves it,
//       and the other takes in the an int size and (Int) -> T function,
//       which are used with Array's constructor
data class Vector(private val vector: Array<Double>) {
    val size: Int = vector.size

    fun get(index: Int): Double {
        if (index < 0 || index >= vector.size) {
            throw RuntimeException("Index is invalid! index: $index, vector.size: " + vector.size)
        }
        return vector[index]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector

        if (!vector.contentEquals(other.vector)) return false

        return true
    }

    override fun hashCode(): Int {
        return vector.contentHashCode()
    }
}