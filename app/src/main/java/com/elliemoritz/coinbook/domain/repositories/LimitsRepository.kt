package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Limit

interface LimitsRepository {
    fun getLimitsList(): LiveData<List<Limit>>
    fun getLimit(id: Int): Limit
    fun addLimit(limit: Limit)
    fun editLimit(limit: Limit)
    fun removeLimit(limit: Limit)
}