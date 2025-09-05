package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a MOC (My Own Creation)
 */
data class Moc(
    @SerializedName("designer_name")
    val designerName: String? = null,
    
    @SerializedName("designer_url")
    val designerUrl: String? = null,
    
    @SerializedName("moc_img_url")
    val mocImgUrl: String? = null,
    
    @SerializedName("moc_url")
    val mocUrl: String? = null,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("num_parts")
    val numParts: Int? = null,
    
    @SerializedName("set_num")
    val setNum: String? = null,
    
    @SerializedName("theme_id")
    val themeId: Int? = null,
    
    @SerializedName("year")
    val year: Int? = null
)

/**
 * List of MOCs with pagination
 */
data class MocList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<Moc>
)