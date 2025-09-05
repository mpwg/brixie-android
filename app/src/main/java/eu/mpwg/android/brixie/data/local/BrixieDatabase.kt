package eu.mpwg.android.brixie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.mpwg.android.brixie.data.local.converters.DateConverters
import eu.mpwg.android.brixie.data.local.dao.SetDao
import eu.mpwg.android.brixie.data.local.dao.ThemeDao
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import eu.mpwg.android.brixie.data.local.entities.ThemeEntity

@Database(
    entities = [
        SetEntity::class,
        ThemeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class BrixieDatabase : RoomDatabase() {
    abstract fun setDao(): SetDao
    abstract fun themeDao(): ThemeDao
}