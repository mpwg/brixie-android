package eu.mpwg.android.rebrickable.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a part category
 */
data class PartCategory(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("name")
    val name: String? = null
)

/**
 * Represents a part in a specific color
 */
data class PartColor(
    @SerializedName("color")
    val color: Color? = null,
    
    @SerializedName("elements")
    val elements: List<String>? = null,
    
    @SerializedName("num_sets")
    val numSets: Int? = null,
    
    @SerializedName("num_set_parts")
    val numSetParts: Int? = null,
    
    @SerializedName("part")
    val part: Part? = null,
    
    @SerializedName("year_from")
    val yearFrom: Int? = null,
    
    @SerializedName("year_to")
    val yearTo: Int? = null
)

/**
 * List of parts with pagination
 */
data class PartsList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<Part>
)

/**
 * List of part categories with pagination
 */
data class PartCategoriesList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<PartCategory>
)

/**
 * List of part colors with pagination
 */
data class PartColorsList(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<PartColor>
)