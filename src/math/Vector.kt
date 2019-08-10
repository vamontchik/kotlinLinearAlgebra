package math

import java.lang.RuntimeException

data class Vector(private val vector: Array<Double>) {
    fun getElem(index: Int): Double {
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