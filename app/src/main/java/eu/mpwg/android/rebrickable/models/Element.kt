package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a LEGO element
 */
data class Element(
    @SerializedName("color")
    val color: Color? = null,
    
    @SerializedName("design_id")
    val designId: String? = null,
    
    @SerializedName("element_id")
    val elementId: String? = null,
    
    @SerializedName("part")
    val part: Part? = null
)