package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JUnitCalculatorTest {

    @Test
    fun plusTest() {
        val calculator = Calculator(5)

        calculator.plus(3)

        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    fun minusTest() {
        val calculator = Calculator(5)

        calculator.minus(3)

        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun multiplyTest() {
        val calculator = Calculator(5)

        calculator.multiply(3)

        assertThat(calculator.number).isEqualTo(15)
    }

    @Test
    fun divideTest() {
        val calculator = Calculator(5)

        calculator.divide(2)

        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun divideExceptionTestUsingApply() {
        val calculator = Calculator(5)

        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.apply { // this = IllegalArgumentException
            assertThat(message).isEqualTo("0으로 나눌 수 없습니다")
        }
    }

    @Test
    fun divideExceptionTestNotUsingApply() {
        val calculator = Calculator(5)

        val message = assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.message
        assertThat(message).isEqualTo("0으로 나눌 수 없습니다")
    }
}