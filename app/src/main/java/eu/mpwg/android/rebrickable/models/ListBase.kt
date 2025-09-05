package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Base class for paginated lists
 */
data class ListBase(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous") 
    val previous: String?
)