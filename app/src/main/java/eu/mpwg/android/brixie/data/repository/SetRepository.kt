package eu.mpwg.android.brixie.data.repository

import eu.mpwg.android.brixie.data.local.dao.SetDao
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import eu.mpwg.android.rebrickable.api.RebrickableApiClient
import eu.mpwg.android.rebrickable.models.ModelSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SetRepository(
    private val apiClient: RebrickableApiClient,
    private val setDao: SetDao
) {
    
    fun getAllSets(): Flow<List<SetEntity>> = setDao.getAllSets()
    
    fun getSetsByTheme(themeId: Int): Flow<List<SetEntity>> = setDao.getSetsByTheme(themeId)
    
    suspend fun getSet(setNum: String): SetEntity? = setDao.getSet(setNum)
    
    fun searchSets(query: String): Flow<List<SetEntity>> = setDao.searchSets(query)
    
    fun getFavoriteSets(): Flow<List<SetEntity>> = setDao.getFavoriteSets()
    
    suspend fun refreshSets(
        page: Int? = null,
        pageSize: Int? = null,
        themeId: Int? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minParts: Int? = null,
        maxParts: Int? = null
    ): Result<Unit> {
        return try {
            val response = apiClient.legoApi.getSetsList(
                page = page,
                pageSize = pageSize,
                themeId = themeId,
                minYear = minYear,
                maxYear = maxYear,
                minParts = minParts,
                maxParts = maxParts
            )
            
            val setEntities = response.results.map { it.toEntity() }
            setDao.insertSets(setEntities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshSetDetails(setNum: String): Result<SetEntity?> {
        return try {
            val set = apiClient.legoApi.getSet(setNum)
            val setEntity = set.toEntity()
            setDao.insertSet(setEntity)
            Result.success(setEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun toggleFavorite(setNum: String) {
        val currentSet = setDao.getSet(setNum)
        currentSet?.let { set ->
            setDao.updateFavoriteStatus(setNum, !set.isFavorite)
        }
    }
    
    private fun ModelSet.toEntity(): SetEntity {
        return SetEntity(
            setNum = setNum ?: "",
            name = name,
            year = year,
            themeId = themeId,
            numParts = numParts,
            setImgUrl = setImgUrl,
            setUrl = setUrl,
            lastModifiedDt = lastModifiedDt
        )
    }
}