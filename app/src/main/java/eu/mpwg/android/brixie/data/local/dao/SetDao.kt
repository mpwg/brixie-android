package eu.mpwg.android.brixie.data.local.dao

import androidx.room.*
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {
    
    @Query("SELECT * FROM sets ORDER BY name ASC")
    fun getAllSets(): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets WHERE themeId = :themeId ORDER BY name ASC")
    fun getSetsByTheme(themeId: Int): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets WHERE setNum = :setNum")
    suspend fun getSet(setNum: String): SetEntity?

    @Query("SELECT * FROM sets WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchSets(query: String): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteSets(): Flow<List<SetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<SetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: SetEntity)

    @Update
    suspend fun updateSet(set: SetEntity)

    @Query("DELETE FROM sets")
    suspend fun deleteAllSets()

    @Query("UPDATE sets SET isFavorite = :isFavorite WHERE setNum = :setNum")
    suspend fun updateFavoriteStatus(setNum: String, isFavorite: Boolean)
}