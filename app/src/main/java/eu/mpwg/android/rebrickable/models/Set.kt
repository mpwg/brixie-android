package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Base class for sets
 */
data class SetBase(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("num_parts")
    val numParts: Int? = null,
    
    @SerializedName("set_img_url")
    val setImgUrl: String? = null,
    
    @SerializedName("set_num")
    val setNum: String? = null,
    
    @SerializedName("set_url")
    val setUrl: String? = null,
    
    @SerializedName("year")
    val year: Int? = null
)

/**
 * Detailed set model
 */
data class ModelSet(
    @SerializedName("last_modified_dt")
    val lastModifiedDt: String? = null,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("num_parts")
    val numParts: Int? = null,
    
    @SerializedName("set_img_url")
    val setImgUrl: String? = null,
    
    @SerializedName("set_num")
    val setNum: String? = null,
    
    @SerializedName("set_url")
    val setUrl: String? = null,
    
    @SerializedName("theme_id")
    val themeId: Int? = null,
    
    @SerializedName("year")
    val year: Int? = null
)

/**
 * Represents a part within a set
 */
data class SetPart(
    @SerializedName("color")
    val color: Color? = null,
    
    @SerializedName("element_id")
    val elementId: String? = null,
    
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("inv_part_id")
    val invPartId: Int? = null,
    
    @SerializedName("is_spare")
    val isSpare: Boolean? = null,
    
    @SerializedName("num_sets")
    val numSets: Int? = null,
    
    @SerializedName("part")
    val part: Part? = null,
    
    @SerializedName("quantity")
    val quantity: Int? = null,
    
    @SerializedName("set_num")
    val setNum: String? = null
)

/**
 * Represents a minifig within a part
 */
data class SetPartMinifig(
    @SerializedName("element_id")
    val elementId: String? = null,
    
    @SerializedName("fig")
    val fig: ModelSet? = null,
    
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("inv_part_id")
    val invPartId: Int? = null,
    
    @SerializedName("quantity")
    val quantity: Int? = null,
    
    @SerializedName("set_num")
    val setNum: String? = null
)

/**
 * List of sets with pagination
 */
data class SetList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<ModelSet>
)

/**
 * List of set parts with pagination
 */
data class SetPartsList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<SetPart>
)

/**
 * List of set minifigs with pagination
 */
data class SetMinifigsList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<SetPartMinifig>
)