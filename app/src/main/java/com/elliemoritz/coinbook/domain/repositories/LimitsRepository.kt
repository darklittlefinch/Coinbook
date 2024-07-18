package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Limit

interface LimitsRepository {
    fun getLimitsList(): LiveData<List<Limit>>
    suspend fun getLimit(id: Int): Limit
    suspend fun addLimit(limit: Limit)
    suspend fun editLimit(limit: Limit)
    suspend fun removeLimit(limit: Limit)
    suspend fun getLimitsCount(): Int
}