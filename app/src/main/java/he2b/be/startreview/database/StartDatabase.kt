package he2b.be.startreview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StartItem::class], version = 1, exportSchema = false)
abstract class StartDatabase : RoomDatabase() {
    abstract fun theDAO(): StartDAO

    companion object {
        private const val DATABASE_NAME = "review_db"
        private var sInstance: StartDatabase? = null
        fun getInstance(context: Context): StartDatabase {
            if (sInstance == null) {
                val dbBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    StartDatabase::class.java,
                    DATABASE_NAME
                )
                sInstance = dbBuilder.build()
            }
            return sInstance!!
        }
    }
}