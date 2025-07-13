package dev.lucianosantos.minicryptotracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CryptoEntity::class], version = 1)
abstract class CryptoDatabase: RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao

    companion object {
        const val DATABASE_NAME = "crypto_database.db"

        fun create(context: Context): CryptoDatabase {
            return Room.databaseBuilder(
                context,
                CryptoDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}