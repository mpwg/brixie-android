package eu.mpwg.android.rebrickable.api

import eu.mpwg.android.rebrickable.config.RebrickableApiConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Comprehensive integration tests for all LegoApi endpoints.
 * Tests actual API calls and response parsing.
 */
class LegoApiIntegrationTest {

    private lateinit var apiClient: RebrickableApiClient
    private lateinit var legoApi: LegoApi
    
    companion object {
        // API key can be set via environment variable or as constant
        private const val FALLBACK_API_KEY = "your-api-key-here"
        
        /**
         * Gets API key from environment variable first, falls back to constant
         */
        private fun getApiKey(): String {
            return System.getenv("REBRICKABLE_API_KEY") ?: FALLBACK_API_KEY
        }
    }

    @Before
    fun setUp() {
        val apiKey = getApiKey()
        assertNotEquals("API key should not be the placeholder", "your-api-key-here", apiKey)
        
        val config = RebrickableApiConfiguration.create(apiKey)
        apiClient = RebrickableApiClient.create(config)
        legoApi = apiClient.legoApi
    }

    @After
    fun tearDown() {
        // Reset configuration after each test
        try {
            RebrickableApiConfiguration.reset()
        } catch (e: Exception) {
            // Ignore reset errors in test cleanup
        }
    }

    // =====================================================
    // Colors API Tests
    // =====================================================

    @Test
    fun testGetColorsList() = runBlocking {
        try {
            val response = legoApi.getColorsList(page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first color has required fields
            val firstColor = response.results.first()
            assertNotNull("Color ID should not be null", firstColor.id)
            assertNotNull("Color name should not be null", firstColor.name)
            assertNotNull("Color RGB should not be null", firstColor.rgb)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetColor() = runBlocking {
        try {
            // Test with a known color ID (0 = Black)
            val response = legoApi.getColor(0)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Color ID should match", 0, response.id)
            assertNotNull("Color name should not be null", response.name)
            assertNotNull("Color RGB should not be null", response.rgb)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Elements API Tests
    // =====================================================

    @Test
    fun testGetElement() = runBlocking {
        try {
            // Test with a known element ID
            val response = legoApi.getElement("300123")
            
            assertNotNull("Response should not be null", response)
            assertNotNull("Element ID should not be null", response.elementId)
            assertNotNull("Design ID should not be null", response.designId)
            assertNotNull("Color should not be null", response.color)
            assertNotNull("Part should not be null", response.part)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Minifigs API Tests
    // =====================================================

    @Test
    fun testGetMinifigsList() = runBlocking {
        try {
            val response = legoApi.getMinifigsList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetMinifig() = runBlocking {
        try {
            // Test with a known minifig set number
            val response = legoApi.getMinifig("fig-000001")
            
            assertNotNull("Response should not be null", response)
            assertNotNull("Set number should not be null", response.setNum)
            assertNotNull("Name should not be null", response.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetMinifigPartsList() = runBlocking {
        try {
            val response = legoApi.getMinifigPartsList("fig-000001", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetMinifigSetsList() = runBlocking {
        try {
            val response = legoApi.getMinifigSetsList("fig-000001", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Part Categories API Tests
    // =====================================================

    @Test
    fun testGetPartCategoriesList() = runBlocking {
        try {
            val response = legoApi.getPartCategoriesList(page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first category has required fields
            val firstCategory = response.results.first()
            assertNotNull("Category ID should not be null", firstCategory.id)
            assertNotNull("Category name should not be null", firstCategory.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetPartCategory() = runBlocking {
        try {
            // Test with a known category ID (1 = Baseplates)
            val response = legoApi.getPartCategory(1)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Category ID should match", 1, response.id)
            assertNotNull("Category name should not be null", response.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Parts API Tests
    // =====================================================

    @Test
    fun testGetPartsList() = runBlocking {
        try {
            val response = legoApi.getPartsList(page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first part has required fields
            val firstPart = response.results.first()
            assertNotNull("Part number should not be null", firstPart.partNum)
            assertNotNull("Part name should not be null", firstPart.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetPart() = runBlocking {
        try {
            // Test with a known part number
            val response = legoApi.getPart("3001")
            
            assertNotNull("Response should not be null", response)
            assertEquals("Part number should match", "3001", response.partNum)
            assertNotNull("Part name should not be null", response.name)
            assertNotNull("Part category ID should not be null", response.partCatId)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetPartColorsList() = runBlocking {
        try {
            val response = legoApi.getPartColorsList("3001", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetPartColor() = runBlocking {
        try {
            // Test with known part number and color (3001 in Red = color 4)
            val response = legoApi.getPartColor("3001", 4)
            
            assertNotNull("Response should not be null", response)
            assertNotNull("Part should not be null", response.part)
            assertNotNull("Color should not be null", response.color)
            assertEquals("Color ID should match", 4, response.color?.id)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetPartColorSetsList() = runBlocking {
        try {
            val response = legoApi.getPartColorSetsList("3001", 4, page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Sets API Tests
    // =====================================================

    @Test
    fun testGetSetsList() = runBlocking {
        try {
            val response = legoApi.getSetsList(page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first set has required fields
            val firstSet = response.results.first()
            assertNotNull("Set number should not be null", firstSet.setNum)
            assertNotNull("Set name should not be null", firstSet.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetSetsListWithFilters() = runBlocking {
        try {
            // Test with filters
            val response = legoApi.getSetsList(
                page = 1, 
                pageSize = 5,
                themeId = 1, // Town theme
                minYear = 2020,
                maxYear = 2023,
                minParts = 100,
                maxParts = 1000
            )
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetSet() = runBlocking {
        try {
            // Test with a known set number
            val response = legoApi.getSet("75192-1")
            
            assertNotNull("Response should not be null", response)
            assertEquals("Set number should match", "75192-1", response.setNum)
            assertNotNull("Set name should not be null", response.name)
            assertNotNull("Theme ID should not be null", response.themeId)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetSetPartsList() = runBlocking {
        try {
            val response = legoApi.getSetPartsList("75192-1", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetSetMinifigsList() = runBlocking {
        try {
            val response = legoApi.getSetMinifigsList("75192-1", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetSetSetsList() = runBlocking {
        try {
            val response = legoApi.getSetSetsList("75192-1", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetSetAlternatesList() = runBlocking {
        try {
            val response = legoApi.getSetAlternatesList("75192-1", page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be non-negative", response.count >= 0)
            assertNotNull("Results should not be null", response.results)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Themes API Tests
    // =====================================================

    @Test
    fun testGetThemesList() = runBlocking {
        try {
            val response = legoApi.getThemesList(page = 1, pageSize = 10)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first theme has required fields
            val firstTheme = response.results.first()
            assertNotNull("Theme ID should not be null", firstTheme.id)
            assertNotNull("Theme name should not be null", firstTheme.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testGetTheme() = runBlocking {
        try {
            // Test with a known theme ID (1 = Town)
            val response = legoApi.getTheme(1)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Theme ID should match", 1, response.id)
            assertNotNull("Theme name should not be null", response.name)
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Error Handling Tests
    // =====================================================

    @Test
    fun testInvalidSetNumber() = runBlocking {
        try {
            legoApi.getSet("invalid-set-number")
            fail("Should have thrown an exception for invalid set number")
        } catch (e: Exception) {
            // Expected - invalid set number should cause an error
            assertTrue("Should be a network or HTTP error", 
                e.message?.contains("404") == true || 
                e.message?.contains("Not Found") == true ||
                e is IOException)
        }
    }

    @Test
    fun testInvalidPartNumber() = runBlocking {
        try {
            legoApi.getPart("invalid-part-number")
            fail("Should have thrown an exception for invalid part number")
        } catch (e: Exception) {
            // Expected - invalid part number should cause an error
            assertTrue("Should be a network or HTTP error", 
                e.message?.contains("404") == true || 
                e.message?.contains("Not Found") == true ||
                e is IOException)
        }
    }

    @Test
    fun testInvalidColorId() = runBlocking {
        try {
            legoApi.getColor(99999) // Very high ID unlikely to exist
            fail("Should have thrown an exception for invalid color ID")
        } catch (e: Exception) {
            // Expected - invalid color ID should cause an error
            assertTrue("Should be a network or HTTP error", 
                e.message?.contains("404") == true || 
                e.message?.contains("Not Found") == true ||
                e is IOException)
        }
    }

    @Test
    fun testInvalidThemeId() = runBlocking {
        try {
            legoApi.getTheme(99999) // Very high ID unlikely to exist
            fail("Should have thrown an exception for invalid theme ID")
        } catch (e: Exception) {
            // Expected - invalid theme ID should cause an error
            assertTrue("Should be a network or HTTP error", 
                e.message?.contains("404") == true || 
                e.message?.contains("Not Found") == true ||
                e is IOException)
        }
    }

    // =====================================================
    // Pagination Tests
    // =====================================================

    @Test
    fun testPaginationColors() = runBlocking {
        try {
            // Test first page
            val page1 = legoApi.getColorsList(page = 1, pageSize = 5)
            assertNotNull("Page 1 should not be null", page1)
            assertEquals("Page 1 should have 5 results", 5, page1.results.size)
            
            // Test second page if there are more results
            if (page1.next != null) {
                val page2 = legoApi.getColorsList(page = 2, pageSize = 5)
                assertNotNull("Page 2 should not be null", page2)
                assertTrue("Page 2 should have results", page2.results.isNotEmpty())
                
                // Ensure different results
                assertNotEquals("Pages should have different results", 
                    page1.results.first().id, page2.results.first().id)
            }
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testOrderingParameters() = runBlocking {
        try {
            // Test ascending order by name
            val ascOrder = legoApi.getColorsList(page = 1, pageSize = 5, ordering = "name")
            assertNotNull("Ascending order should not be null", ascOrder)
            assertTrue("Should have results", ascOrder.results.isNotEmpty())
            
            // Test descending order by name
            val descOrder = legoApi.getColorsList(page = 1, pageSize = 5, ordering = "-name")
            assertNotNull("Descending order should not be null", descOrder)
            assertTrue("Should have results", descOrder.results.isNotEmpty())
            
            // Results should be different (unless there are only 5 or fewer colors total)
            if (ascOrder.count > 5) {
                assertNotEquals("Different ordering should yield different first results",
                    ascOrder.results.first().name, descOrder.results.first().name)
            }
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    // =====================================================
    // Data Parsing Verification Tests
    // =====================================================

    @Test
    fun testCompleteDataParsing() = runBlocking {
        try {
            // Get a set with comprehensive data
            val set = legoApi.getSet("75192-1")
            
            // Verify all fields are parsed correctly
            assertNotNull("Set number parsed", set.setNum)
            assertNotNull("Name parsed", set.name)
            assertNotNull("Year parsed", set.year)
            assertNotNull("Part count parsed", set.numParts)
            assertNotNull("Theme ID parsed", set.themeId)
            assertNotNull("Set URL parsed", set.setUrl)
            assertNotNull("Set image URL parsed", set.setImgUrl)
            assertNotNull("Last modified date parsed", set.lastModifiedDt)
            
            // Verify data types
            assertTrue("Year should be reasonable", set.year!! > 1950 && set.year!! < 2050)
            assertTrue("Part count should be positive", set.numParts!! > 0)
            assertTrue("Theme ID should be positive", set.themeId!! > 0)
            assertTrue("Set URL should be valid", set.setUrl!!.startsWith("http"))
            
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }

    @Test
    fun testComplexObjectParsing() = runBlocking {
        try {
            // Get set parts to test complex nested objects
            val parts = legoApi.getSetPartsList("75192-1", page = 1, pageSize = 5)
            
            assertNotNull("Parts list should not be null", parts)
            assertTrue("Should have parts", parts.results.isNotEmpty())
            
            val firstPart = parts.results.first()
            assertNotNull("Part object should not be null", firstPart.part)
            assertNotNull("Color object should not be null", firstPart.color)
            assertNotNull("Quantity should not be null", firstPart.quantity)
            
            // Verify nested object parsing
            assertNotNull("Part number should not be null", firstPart.part?.partNum)
            assertNotNull("Part name should not be null", firstPart.part?.name)
            assertNotNull("Color ID should not be null", firstPart.color?.id)
            assertNotNull("Color name should not be null", firstPart.color?.name)
            assertNotNull("RGB value should not be null", firstPart.color?.rgb)
            
        } catch (e: IOException) {
            fail("Network error: ${e.message}")
        }
    }
}