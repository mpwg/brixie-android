package eu.mpwg.android.brixie.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "sets")
data class SetEntity(
    @PrimaryKey
    val setNum: String,
    val name: String?,
    val year: Int?,
    val themeId: Int?,
    val numParts: Int?,
    val setImgUrl: String?,
    val setUrl: String?,
    val lastModifiedDt: String?,
    val cachedAt: Date = Date(),
    val isFavorite: Boolean = false
)