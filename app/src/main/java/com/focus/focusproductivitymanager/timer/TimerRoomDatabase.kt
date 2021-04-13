package com.focus.focusproductivitymanager.timer

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@Database(entities = arrayOf(Timer::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TimerRoomDatabase : RoomDatabase() {

    abstract fun timerDao(): TimerDao

    private class TimerDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    Log.d("Focus", "here!!!")
                    populateDatabase(database.timerDao())
                }
            }
        }

        suspend fun populateDatabase(timerDao: TimerDao) {

            timerDao.deleteAll()
            var timer = Timer(
                0,
                "Timer",
                LocalDate.now(),
                LocalTime.of(17, 30),
                LocalTime.of(18, 0),
                "Here are some notes"
            )
            timerDao.insert(timer)

        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TimerRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TimerRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                    ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimerRoomDatabase::class.java,
                    "timer_database"
                ).addCallback(TimerDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


}
