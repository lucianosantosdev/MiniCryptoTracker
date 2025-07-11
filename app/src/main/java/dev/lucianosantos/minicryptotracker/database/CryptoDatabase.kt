package dev.lucianosantos.minicryptotracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CryptoEntity::class], version = 1)
abstract class CryptoDatabase: RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
}