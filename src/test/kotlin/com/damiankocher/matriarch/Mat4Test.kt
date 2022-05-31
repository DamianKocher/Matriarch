package com.damiankocher.matriarch

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import org.joml.Matrix4d
import org.joml.Vector3d
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val PRECISION = 1.0 / 1024.0

internal class Mat4Test {

    @Test
    fun scale() {
        for (scaleX in -2.0..2.0 step 0.1) {
            for (scaleY in -2.0..2.0 step 0.1) {
                for (scaleZ in -2.0..2.0 step 0.1) {
                    val jomlMatrix = Matrix4d().scale(scaleX, scaleY, scaleZ)
                    val mat4 = Mat4.IDENTITY.scale(scaleX, scaleY, scaleZ)

                    assertSame(jomlMatrix, mat4)
                }
            }
        }
    }

    @Test
    fun translate() {
        for (x in -20.0..20.0 step 0.5) {
            for (y in -20.0..20.0 step 0.5) {
                for (z in -20.0..20.0 step 0.5) {
                    val jomlMatrix = Matrix4d().translate(x, y, z).translate(x, y, z)
                    val mat4 = Mat4.IDENTITY.translate(x, y, z).translate(x, y, z)

                    assertSame(jomlMatrix, mat4)
                }
            }
        }

    }

    @Test
    fun rotate() {
        for (pitch in -360.0..720.0 step 10.0) {
            for (yaw in -360.0..720.0 step 10.0) {
                for (roll in -360.0..720.0 step 10.0) {
                    val pitchRad = Math.toRadians(pitch)
                    val yawRad = Math.toRadians(yaw)
                    val rollRad = Math.toRadians(roll)

                    val jomlMatrix = Matrix4d().rotateXYZ(pitchRad, yawRad, rollRad)
                    val mat4 = Mat4.IDENTITY.rotate(pitchRad, yawRad, rollRad)

                    assertSame(jomlMatrix, mat4)
                }
            }
        }
    }

    @Test
    fun mulVector() {
        val jomlMatrix = Matrix4d().translate(20.0, 10.0, -20.0).rotateX(1.0)
        val mat4 = Mat4.IDENTITY.translate(20.0, 10.0, -20.0).rotateX(1.0)

        assertSame(jomlMatrix.transformPosition(Vector3d(1.0, 1.0, 1.0)), mat4 * Pos(1.0, 1.0, 1.0))
        assertSame(jomlMatrix.transformPosition(Vector3d(1.0, 5.0, 1.0)), mat4 * Pos(1.0, 5.0, 1.0))
    }

    @Test
    fun `translate - rotate - negate - transform position`() {


        val jomlMatrix = Matrix4d().translate(20.0, 10.0, -20.0).rotateX(1.0)
        val mat4 = Mat4.IDENTITY.translate(20.0, 10.0, -20.0).rotateX(1.0)

        assertSame(jomlMatrix.transformPosition(Vector3d(1.0, 1.0, 1.0)), mat4 * Pos(1.0, 1.0, 1.0))
        assertSame(jomlMatrix.transformPosition(Vector3d(1.0, 5.0, 1.0)), mat4 * Pos(1.0, 5.0, 1.0))
    }

    private fun assertSame(matrix4d: Matrix4d, mat4: Mat4) {
        assertEquals(matrix4d.m00(), mat4.m00, PRECISION, "m00 is the wrong value!")
        assertEquals(matrix4d.m10(), mat4.m01, PRECISION, "m01 is the wrong value!")
        assertEquals(matrix4d.m20(), mat4.m02, PRECISION, "m02 is the wrong value!")
        assertEquals(matrix4d.m30(), mat4.m03, PRECISION, "m03 is the wrong value!")

        assertEquals(matrix4d.m01(), mat4.m10, PRECISION, "m10 is the wrong value!")
        assertEquals(matrix4d.m11(), mat4.m11, PRECISION, "m11 is the wrong value!")
        assertEquals(matrix4d.m21(), mat4.m12, PRECISION, "m12 is the wrong value!")
        assertEquals(matrix4d.m32(), mat4.m23, PRECISION, "m23 is the wrong value!")

        assertEquals(matrix4d.m02(), mat4.m20, PRECISION, "m20 is the wrong value!")
        assertEquals(matrix4d.m12(), mat4.m21, PRECISION, "m21 is the wrong value!")
        assertEquals(matrix4d.m22(), mat4.m22, PRECISION, "m22 is the wrong value!")
        assertEquals(matrix4d.m32(), mat4.m23, PRECISION, "m23 is the wrong value!")

        assertEquals(matrix4d.m03(), mat4.m30, PRECISION, "m30 is the wrong value!")
        assertEquals(matrix4d.m13(), mat4.m31, PRECISION, "m31 is the wrong value!")
        assertEquals(matrix4d.m23(), mat4.m32, PRECISION, "m32 is the wrong value!")
        assertEquals(matrix4d.m33(), mat4.m33, PRECISION, "m33 is the wrong value!")
    }

    // I understand this is not ideal, but it's good enough for tests
    // https://stackoverflow.com/questions/44315977/ranges-in-kotlin-using-data-type-double
    private infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
        require(start.isFinite())
        require(endInclusive.isFinite())
        require(step > 0.0) { "Step must be positive, was: $step." }
        val sequence = generateSequence(start) { previous ->
            if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
            val next = previous + step
            if (next > endInclusive) null else next
        }
        return sequence.asIterable()
    }

    private fun assertSame(vector3d: Vector3d, point: Point) {
        assertEquals(vector3d.x(), point.x(), "x() is the wrong value!")
        assertEquals(vector3d.y(), point.y(), "x() is the wrong value!")
        assertEquals(vector3d.z(), point.z(), "x() is the wrong value!")
    }
}