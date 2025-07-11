package dev.lucianosantos.minicryptotracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {
    @Query("SELECT * FROM CryptoEntity")
    fun getAll(): Flow<List<CryptoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(crypto: List<CryptoEntity>)

    @Update
    suspend fun update(crypto: CryptoEntity)
}