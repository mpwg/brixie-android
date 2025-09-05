package eu.mpwg.android.brixie.data.local.dao

import androidx.room.*
import eu.mpwg.android.brixie.data.local.entities.ThemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    
    @Query("SELECT * FROM themes ORDER BY name ASC")
    fun getAllThemes(): Flow<List<ThemeEntity>>

    @Query("SELECT * FROM themes WHERE id = :id")
    suspend fun getTheme(id: Int): ThemeEntity?

    @Query("SELECT * FROM themes WHERE parentId IS NULL ORDER BY name ASC")
    fun getRootThemes(): Flow<List<ThemeEntity>>

    @Query("SELECT * FROM themes WHERE parentId = :parentId ORDER BY name ASC")
    fun getChildThemes(parentId: Int): Flow<List<ThemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemes(themes: List<ThemeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: ThemeEntity)

    @Query("DELETE FROM themes")
    suspend fun deleteAllThemes()
}