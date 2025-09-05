package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a LEGO part
 */
data class Part(
    @SerializedName("external_ids")
    val externalIds: PartExternalIds? = null,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("part_cat_id")
    val partCatId: Int? = null,
    
    @SerializedName("part_img_url")
    val partImgUrl: String? = null,
    
    @SerializedName("part_num")
    val partNum: String? = null,
    
    @SerializedName("part_url")
    val partUrl: String? = null
)

/**
 * External IDs for a part
 */
data class PartExternalIds(
    @SerializedName("BrickLink")
    val brickLink: List<String>? = null,
    
    @SerializedName("BrickOwl")
    val brickOwl: List<String>? = null,
    
    @SerializedName("LEGO")
    val lego: List<String>? = null,
    
    @SerializedName("LDraw")
    val lDraw: List<String>? = null
)

/**
 * Represents a LEGO color
 */
data class Color(
    @SerializedName("external_ids")
    val externalIds: ColorExternalIds? = null,
    
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("is_trans")
    val isTrans: Boolean? = null,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("rgb")
    val rgb: String? = null
)

/**
 * External IDs for a color
 */
data class ColorExternalIds(
    @SerializedName("BrickLink")
    val brickLink: ColorExternalProvider? = null,
    
    @SerializedName("BrickOwl")
    val brickOwl: ColorExternalProvider? = null,
    
    @SerializedName("LEGO")
    val lego: ColorExternalProvider? = null,
    
    @SerializedName("Peeron")
    val peeron: ColorExternalProvider? = null
)

/**
 * External color provider information
 */
data class ColorExternalProvider(
    @SerializedName("ext_ids")
    val extIds: List<Int>? = null,
    
    @SerializedName("ext_descrs")
    val extDescrs: List<List<String>>? = null
)