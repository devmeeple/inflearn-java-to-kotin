package com.group.libraryapp.calculator

class Calculator(
    var number: Int,
) {
    fun plus(operand: Int) {
        this.number += operand
    }

    fun minus(operand: Int) {
        this.number -= operand
    }

    fun multiply(operand: Int) {
        this.number *= operand
    }

    fun divide(operand: Int) {
        // TODO: 2025-03-25 if, require, when
        when (operand) {
            0 -> throw IllegalArgumentException("0으로 나눌 수 없습니다")
            else -> this.number /= operand
        }
    }
}