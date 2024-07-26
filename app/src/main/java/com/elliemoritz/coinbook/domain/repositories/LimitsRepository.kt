package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.Limit
import kotlinx.coroutines.flow.Flow

interface LimitsRepository {
    fun getLimitsList(): Flow<List<Limit>>
    fun getLimit(id: Int): Flow<Limit>
    fun getLimitByCategoryId(categoryId: Int): Flow<Limit?>
    suspend fun addLimit(limit: Limit)
    suspend fun editLimit(limit: Limit)
    suspend fun removeLimit(limit: Limit)
    fun getLimitsCount(): Flow<Int>
}