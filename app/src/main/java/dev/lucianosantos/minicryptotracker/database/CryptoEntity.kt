package dev.lucianosantos.minicryptotracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CryptoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val symbol: String,
    val name: String,
    val description: String,
    val image: String? = null,
    val price: Long? = null,
    val lastUpdated: Long = System.currentTimeMillis(),
)