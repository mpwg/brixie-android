package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a LEGO theme
 */
data class Theme(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("parent_id")
    val parentId: Int? = null
)

/**
 * List of themes with pagination
 */
data class ThemesList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<Theme>
)