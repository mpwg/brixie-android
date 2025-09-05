package eu.mpwg.android.brixie.data.repository

import eu.mpwg.android.brixie.data.local.dao.ThemeDao
import eu.mpwg.android.brixie.data.local.entities.ThemeEntity
import eu.mpwg.android.rebrickable.api.RebrickableApiClient
import eu.mpwg.android.rebrickable.models.Theme
import kotlinx.coroutines.flow.Flow

class ThemeRepository(
    private val apiClient: RebrickableApiClient,
    private val themeDao: ThemeDao
) {
    
    fun getAllThemes(): Flow<List<ThemeEntity>> = themeDao.getAllThemes()
    
    fun getRootThemes(): Flow<List<ThemeEntity>> = themeDao.getRootThemes()
    
    fun getChildThemes(parentId: Int): Flow<List<ThemeEntity>> = themeDao.getChildThemes(parentId)
    
    suspend fun getTheme(id: Int): ThemeEntity? = themeDao.getTheme(id)
    
    suspend fun refreshThemes(
        page: Int? = null,
        pageSize: Int? = null
    ): Result<Unit> {
        return try {
            val response = apiClient.legoApi.getThemesList(
                page = page,
                pageSize = pageSize
            )
            
            val themeEntities = response.results.map { it.toEntity() }
            themeDao.insertThemes(themeEntities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshThemeDetails(id: Int): Result<ThemeEntity?> {
        return try {
            val theme = apiClient.legoApi.getTheme(id)
            val themeEntity = theme.toEntity()
            themeDao.insertTheme(themeEntity)
            Result.success(themeEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun Theme.toEntity(): ThemeEntity {
        return ThemeEntity(
            id = id ?: 0,
            name = name,
            parentId = parentId
        )
    }
}