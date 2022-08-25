package com.app.hackathon.data.db.dao

import androidx.room.*
import com.app.hackathon.data.db.entity.SearchHistory
import io.reactivex.rxjava3.core.Single

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM SEARCH_HISTORY")
    fun getAll(): Single<List<SearchHistory>>

    // 삽입한 데이터의 PrimaryKey 반환 (따라서 Long)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: SearchHistory): Long

    // 삭제한 데이터 레코드 개수(Int) 반환
    @Delete
    fun deleteItem(item: SearchHistory): Int
}