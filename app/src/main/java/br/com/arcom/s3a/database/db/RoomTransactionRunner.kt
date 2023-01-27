package br.com.arcom.s3a.database.db

import androidx.room.withTransaction
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val db: AppDatabase
) : DatabaseTransactionRunner {
    override suspend operator fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction {
            block()
        }
    }
}