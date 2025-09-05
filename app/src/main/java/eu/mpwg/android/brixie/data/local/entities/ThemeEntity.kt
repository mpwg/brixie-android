package eu.mpwg.android.brixie.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "themes")
data class ThemeEntity(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val parentId: Int?,
    val cachedAt: Date = Date()
)