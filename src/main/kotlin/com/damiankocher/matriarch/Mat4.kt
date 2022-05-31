package com.damiankocher.matriarch

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import kotlin.math.cos
import kotlin.math.sin

data class Mat4(
    val m00: Double, val m01: Double, val m02: Double, val m03: Double,
    val m10: Double, val m11: Double, val m12: Double, val m13: Double,
    val m20: Double, val m21: Double, val m22: Double, val m23: Double,
    val m30: Double, val m31: Double, val m32: Double, val m33: Double
) {

    fun translate(x: Double, y: Double, z: Double): Mat4 {
        return this * translation(x, y, z)
    }

    fun rotate(pitch: Double, yaw: Double, roll: Double): Mat4 {
        return this * rotateX(pitch) * rotateY(yaw) * rotateZ(roll)
    }

    fun rotateX(rot: Double): Mat4 {
        val rotationMatrix = Mat4(
            1.0, 0.0, 0.0, 0.0,
            0.0, cos(rot), -sin(rot), 0.0,
            0.0, sin(rot), cos(rot), 0.0,
            0.0, 0.0, 0.0, 1.0
        )

        return this * rotationMatrix
    }

    fun rotateY(rot: Double): Mat4 {
        val rotationMatrix = Mat4(
            cos(rot), 0.0, sin(rot), 0.0,
            0.0, 1.0, 0.0, 0.0,
            -sin(rot), 0.0, cos(rot), 0.0,
            0.0, 0.0, 0.0, 1.0
        )

        return this * rotationMatrix
    }

    fun rotateZ(rot: Double): Mat4 {
        val rotationMatrix = Mat4(
            cos(rot), -sin(rot), 0.0, 0.0,
            sin(rot), cos(rot), 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        )

        return this * rotationMatrix
    }

    fun scale(scale: Point) {
        scale(scale, 1.0)
    }

    fun scale(scale: Point, w: Double) {
        scale(scale.x(), scale.y(), scale.z(), w)
    }

    fun scale(x: Double, y: Double, z: Double): Mat4 {
        return scale(x, y, z, 1.0)
    }

    fun scale(x: Double, y: Double, z: Double, w: Double): Mat4 {
        return Mat4(
            m00 * x, m01, m02, m03,
            m10, m11 * y, m12, m13,
            m20, m21, m22 * z, m23,
            m30, m31, m32, m33 * w
        )
    }

    // http://www.opengl-tutorial.org/assets/faq_quaternions/index.html#Q9
    operator fun plus(right: Mat4): Mat4 {
        return Mat4(
            m00 + right.m00, m01 + right.m01, m02 + right.m02, m03 + right.m03,
            m10 + right.m10, m11 + right.m11, m12 + right.m12, m13 + right.m13,
            m20 + right.m20, m21 + right.m21, m22 + right.m22, m23 + right.m23,
            m30 + right.m30, m31 + right.m31, m32 + right.m32, m33 + right.m33,
        )
    }

    // http://www.opengl-tutorial.org/assets/faq_quaternions/index.html#Q10
    operator fun minus(right: Mat4): Mat4 {
        return Mat4(
            m00 - right.m00, m01 - right.m01, m02 - right.m02, m03 - right.m03,
            m10 - right.m10, m11 - right.m11, m12 - right.m12, m13 - right.m13,
            m20 - right.m20, m21 - right.m21, m22 - right.m22, m23 - right.m23,
            m30 - right.m30, m31 - right.m31, m32 - right.m32, m33 - right.m33,
        )
    }

    // http://www.opengl-tutorial.org/assets/faq_quaternions/index.html#Q11
    operator fun times(right: Mat4): Mat4 {
        val nm00 = (m00 * right.m00) + (m01 * right.m10) + (m02 * right.m20) + (m03 * right.m30)
        val nm01 = (m00 * right.m01) + (m01 * right.m11) + (m02 * right.m21) + (m03 * right.m31)
        val nm02 = (m00 * right.m02) + (m01 * right.m12) + (m02 * right.m22) + (m03 * right.m32)
        val nm03 = (m00 * right.m03) + (m01 * right.m13) + (m02 * right.m23) + (m03 * right.m33)

        val nm10 = (m10 * right.m00) + (m11 * right.m10) + (m12 * right.m20) + (m13 * right.m30)
        val nm11 = (m10 * right.m01) + (m11 * right.m11) + (m12 * right.m21) + (m13 * right.m31)
        val nm12 = (m10 * right.m02) + (m11 * right.m12) + (m12 * right.m22) + (m13 * right.m32)
        val nm13 = (m10 * right.m03) + (m11 * right.m13) + (m12 * right.m23) + (m13 * right.m33)

        val nm20 = (m20 * right.m00) + (m21 * right.m10) + (m22 * right.m20) + (m23 * right.m30)
        val nm21 = (m20 * right.m01) + (m21 * right.m11) + (m22 * right.m21) + (m23 * right.m31)
        val nm22 = (m20 * right.m02) + (m21 * right.m12) + (m22 * right.m22) + (m23 * right.m32)
        val nm23 = (m20 * right.m03) + (m21 * right.m13) + (m22 * right.m23) + (m23 * right.m33)

        val nm30 = (m30 * right.m00) + (m31 * right.m10) + (m32 * right.m20) + (m33 * right.m30)
        val nm31 = (m30 * right.m01) + (m31 * right.m11) + (m32 * right.m21) + (m33 * right.m31)
        val nm32 = (m30 * right.m02) + (m31 * right.m12) + (m32 * right.m22) + (m33 * right.m32)
        val nm33 = (m30 * right.m03) + (m31 * right.m13) + (m32 * right.m23) + (m33 * right.m33)

        return Mat4(
            nm00, nm01, nm02, nm03,
            nm10, nm11, nm12, nm13,
            nm20, nm21, nm22, nm23,
            nm30, nm31, nm32, nm33
        )
    }

    // http://www.opengl-tutorial.org/assets/faq_quaternions/index.html#Q13
    operator fun times(right: Pos): Pos {
        // Since we're multiplying a position W is 1

        val x = m00 * right.x() + m01 * right.y() + m02 * right.z() + m03
        val y = m01 * right.x() + m11 * right.y() + m12 * right.z() + m13
        val z = m02 * right.x() + m21 * right.y() + m22 * right.z() + m23

        return Pos(x, y, z, right.yaw(), right.pitch)
    }

    // http://www.opengl-tutorial.org/assets/faq_quaternions/index.html#Q13
    operator fun times(right: Vec): Vec {
        // Since we're multiplying a direction W is 0

        val x = m00 * right.x() + m01 * right.y() + m02 * right.z()
        val y = m10 * right.x() + m11 * right.y() + m12 * right.z()
        val z = m20 * right.x() + m21 * right.y() + m22 * right.z()

        return Vec(x, y, z)
    }

    fun m00(m00: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m01(m01: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m02(m02: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m03(m03: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m10(m10: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m11(m11: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m12(m12: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m13(m13: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m20(m20: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m21(m21: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m22(m22: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m23(m23: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m30(m30: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m31(m31: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m32(m32: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun m33(m33: Double): Mat4 {
        return Mat4(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    companion object {

        // http://www.opengl-tutorial.org/assets/faq_quaternions/index.html#Q6
        val IDENTITY = Mat4(
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        )

        fun transform(positionRotation: Pos, scaleX: Double, scaleY: Double, scaleZ: Double) {

        }

        fun transform(
            posX: Double, posY: Double, posZ: Double, rotX: Double, rotY: Double, rotZ: Double, scaleX: Double, scaleY: Double, scaleZ: Double): Mat4 {
            return IDENTITY
                .translate(posX, posY, posZ)
                .rotate(rotX, rotY, rotZ)
                .scale(scaleX, scaleY, scaleZ)
        }

        fun translation(x: Double, y: Double, z: Double): Mat4 {
            return Mat4(
                1.0, 0.0, 0.0, x,
                0.0, 1.0, 0.0, y,
                0.0, 0.0, 1.0, z,
                0.0, 0.0, 0.0, 1.0
            )
        }
    }
}