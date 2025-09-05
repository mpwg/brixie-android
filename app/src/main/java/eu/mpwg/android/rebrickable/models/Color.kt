package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * List of colors with pagination
 */
data class ColorsList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<Color>
)