package eu.mpwg.android.rebrickable.api

import eu.mpwg.android.rebrickable.models.*
import retrofit2.http.*

/**
 * Retrofit interface for the Rebrickable Lego API
 */
interface LegoApi {
    
    /**
     * Get a list of all Colors.
     */
    @GET("/api/v3/lego/colors/")
    suspend fun getColorsList(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null
    ): ColorsList

    /**
     * Get details about a specific Color.
     */
    @GET("/api/v3/lego/colors/{id}/")
    suspend fun getColor(@Path("id") id: Int): Color

    /**
     * Get details about a specific Element ID.
     */
    @GET("/api/v3/lego/elements/{element_id}/")
    suspend fun getElement(@Path("element_id") elementId: String): Element

    /**
     * Get a list of Minifigs.
     */
    @GET("/api/v3/lego/minifigs/")
    suspend fun getMinifigsList(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null
    ): SetMinifigsList

    /**
     * Get a list of all Inventory Parts in this Minifig.
     */
    @GET("/api/v3/lego/minifigs/{set_num}/parts/")
    suspend fun getMinifigPartsList(
        @Path("set_num") setNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): SetPartsList

    /**
     * Get details for a specific Minifig.
     */
    @GET("/api/v3/lego/minifigs/{set_num}/")
    suspend fun getMinifig(@Path("set_num") setNum: String): ModelSet

    /**
     * Get a list of Sets a Minifig has appeared in.
     */
    @GET("/api/v3/lego/minifigs/{set_num}/sets/")
    suspend fun getMinifigSetsList(
        @Path("set_num") setNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): SetList

    /**
     * Get a list of all Part Categories.
     */
    @GET("/api/v3/lego/part_categories/")
    suspend fun getPartCategoriesList(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null
    ): PartCategoriesList

    /**
     * Get details about a specific Part Category.
     */
    @GET("/api/v3/lego/part_categories/{id}/")
    suspend fun getPartCategory(@Path("id") id: Int): PartCategory

    /**
     * Get a list of all Colors a Part has appeared in.
     */
    @GET("/api/v3/lego/parts/{part_num}/colors/")
    suspend fun getPartColorsList(
        @Path("part_num") partNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): PartColorsList

    /**
     * Get details about a specific Part/Color combination.
     */
    @GET("/api/v3/lego/parts/{part_num}/colors/{color_id}/")
    suspend fun getPartColor(
        @Path("part_num") partNum: String,
        @Path("color_id") colorId: Int
    ): PartColor

    /**
     * Get a list of all Sets the Part/Color combination has appeared in.
     */
    @GET("/api/v3/lego/parts/{part_num}/colors/{color_id}/sets/")
    suspend fun getPartColorSetsList(
        @Path("part_num") partNum: String,
        @Path("color_id") colorId: Int,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): SetList

    /**
     * Get a list of Parts.
     */
    @GET("/api/v3/lego/parts/")
    suspend fun getPartsList(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null,
        @Query("part_cat_id") partCatId: Int? = null
    ): PartsList

    /**
     * Get details about a specific Part.
     */
    @GET("/api/v3/lego/parts/{part_num}/")
    suspend fun getPart(@Path("part_num") partNum: String): eu.mpwg.android.rebrickable.models.Part

    /**
     * Get a list of MOCs which are Alternate Builds of a specific Set.
     */
    @GET("/api/v3/lego/sets/{set_num}/alternates/")
    suspend fun getSetAlternatesList(
        @Path("set_num") setNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): MocList

    /**
     * Get a list of Sets, optionally filtered by any of the below parameters.
     */
    @GET("/api/v3/lego/sets/")
    suspend fun getSetsList(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null,
        @Query("theme_id") themeId: Int? = null,
        @Query("min_year") minYear: Int? = null,
        @Query("max_year") maxYear: Int? = null,
        @Query("min_parts") minParts: Int? = null,
        @Query("max_parts") maxParts: Int? = null
    ): SetList

    /**
     * Get a list of all Inventory Minifigs in this Set.
     */
    @GET("/api/v3/lego/sets/{set_num}/minifigs/")
    suspend fun getSetMinifigsList(
        @Path("set_num") setNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): SetMinifigsList

    /**
     * Get a list of all Inventory Parts in this Set.
     */
    @GET("/api/v3/lego/sets/{set_num}/parts/")
    suspend fun getSetPartsList(
        @Path("set_num") setNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): SetPartsList

    /**
     * Get details for a specific Set.
     */
    @GET("/api/v3/lego/sets/{set_num}/")
    suspend fun getSet(@Path("set_num") setNum: String): ModelSet

    /**
     * Get a list of all Inventory Sets in this Set.
     */
    @GET("/api/v3/lego/sets/{set_num}/sets/")
    suspend fun getSetSetsList(
        @Path("set_num") setNum: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): SetList

    /**
     * Return all Themes.
     */
    @GET("/api/v3/lego/themes/")
    suspend fun getThemesList(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null
    ): ThemesList

    /**
     * Return details for a specific Theme.
     */
    @GET("/api/v3/lego/themes/{id}/")
    suspend fun getTheme(@Path("id") id: Int): Theme
}