package com.elliemoritz.coinbook.presentation.util

interface OnEditingListener {
    fun onFinished()
    fun onEmptyFields()
    fun onIncorrectNumber()
}