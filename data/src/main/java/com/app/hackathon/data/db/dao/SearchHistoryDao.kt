package com.app.hackathon.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.hackathon.data.db.entity.SearchHistoryEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM SEARCH_HISTORY")
    fun getAll(): Single<List<SearchHistoryEntity>>

    @Insert
    fun insertItem(item: SearchHistoryEntity)

    @Delete
    fun deleteItem(item: SearchHistoryEntity)
}