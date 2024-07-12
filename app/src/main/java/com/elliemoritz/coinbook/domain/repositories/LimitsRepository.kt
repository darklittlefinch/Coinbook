package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Limit

interface LimitsRepository {
    fun getLimitsList(): LiveData<List<Limit>>
    fun getLimit(id: Int): Limit
    fun addLimit(operation: Limit)
    fun editLimit(operation: Limit)
    fun removeLimit(operation: Limit)
}