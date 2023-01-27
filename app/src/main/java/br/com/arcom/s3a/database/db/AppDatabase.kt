package br.com.arcom.s3a.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.arcom.s3a.database.converter.AppTypeConverters
import br.com.arcom.s3a.database.dao.CronogramaDao
import br.com.arcom.s3a.database.entity.CronogramaEntity
import br.com.arcom.s3a.util.DATABASE_NAME

@Database(
    entities = [
        CronogramaEntity::class,
    ],
    version = 1, exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cronogramaDao(): CronogramaDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}