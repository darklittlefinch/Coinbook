package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.CategoryDbModel

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories ORDER BY name")
    suspend fun getCategoriesList(): List<CategoryDbModel>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getCategory(id: Long): CategoryDbModel

    @Query("SELECT * FROM categories WHERE name = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): CategoryDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: CategoryDbModel): Long

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun removeCategory(id: Long)
}
