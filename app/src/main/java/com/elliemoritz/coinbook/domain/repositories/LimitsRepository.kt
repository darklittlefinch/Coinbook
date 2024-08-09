package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.Limit
import kotlinx.coroutines.flow.Flow

interface LimitsRepository {

    fun getLimitsList(): Flow<List<Limit>>

    fun getLimit(id: Long): Flow<Limit>
    fun getLimitByCategoryId(categoryId: Long): Flow<Limit?>
    suspend fun addLimit(limit: Limit)
    suspend fun editLimit(limit: Limit)
    suspend fun removeLimit(limit: Limit)

    fun getLimitsCount(): Flow<Long>
}