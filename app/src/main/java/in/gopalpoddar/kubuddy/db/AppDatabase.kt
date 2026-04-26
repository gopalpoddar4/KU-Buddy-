package `in`.gopalpoddar.kubuddy.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import `in`.gopalpoddar.kubuddy.db.dao.StudyMaterialDao
import `in`.gopalpoddar.kubuddy.db.entity.StudyMaterialEntity

@Database(entities = [StudyMaterialEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun studyMaterialDao(): StudyMaterialDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE?:synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kubuddy_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}