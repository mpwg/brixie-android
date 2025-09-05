package eu.mpwg.android.rebrickable.api

import eu.mpwg.android.rebrickable.config.RebrickableApiConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Unit tests for LegoApi endpoints that don't require Android dependencies.
 * This version creates API configuration without using Android Log.
 */
class LegoApiUnitTest {

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
        
        // Skip test if no valid API key is provided
        if (apiKey == "your-api-key-here") {
            println("Skipping API tests - no API key provided. Set REBRICKABLE_API_KEY environment variable.")
            return
        }
        
        // Create configuration directly without using shared singleton
        // to avoid Android Log dependencies
        try {
            val config = createTestApiConfiguration(apiKey)
            apiClient = RebrickableApiClient.create(config)
            legoApi = apiClient.legoApi
        } catch (e: Exception) {
            fail("Failed to setup API client: ${e.message}")
        }
    }
    
    /**
     * Creates API configuration for testing without Android dependencies
     */
    private fun createTestApiConfiguration(apiKey: String): RebrickableApiConfiguration {
        return RebrickableApiConfiguration.create(apiKey, "https://rebrickable.com")
    }

    // =====================================================
    // Simple API Tests
    // =====================================================

    @Test
    fun testGetColorsList() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            val response = legoApi.getColorsList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first color has required fields
            val firstColor = response.results.first()
            assertNotNull("Color ID should not be null", firstColor.id)
            assertNotNull("Color name should not be null", firstColor.name)
            assertNotNull("Color RGB should not be null", firstColor.rgb)
            
            println("✓ Colors List API test passed - found ${response.count} colors")
        } catch (e: Exception) {
            fail("Colors List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetSetsList() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            val response = legoApi.getSetsList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first set has required fields
            val firstSet = response.results.first()
            assertNotNull("Set number should not be null", firstSet.setNum)
            assertNotNull("Set name should not be null", firstSet.name)
            
            println("✓ Sets List API test passed - found ${response.count} sets")
        } catch (e: Exception) {
            fail("Sets List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetThemesList() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            val response = legoApi.getThemesList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first theme has required fields
            val firstTheme = response.results.first()
            assertNotNull("Theme ID should not be null", firstTheme.id)
            assertNotNull("Theme name should not be null", firstTheme.name)
            
            println("✓ Themes List API test passed - found ${response.count} themes")
        } catch (e: Exception) {
            fail("Themes List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetPartCategoriesList() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            val response = legoApi.getPartCategoriesList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first category has required fields
            val firstCategory = response.results.first()
            assertNotNull("Category ID should not be null", firstCategory.id)
            assertNotNull("Category name should not be null", firstCategory.name)
            
            println("✓ Part Categories List API test passed - found ${response.count} categories")
        } catch (e: Exception) {
            fail("Part Categories List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetColor() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            // Test with a known color ID (0 = Black)
            val response = legoApi.getColor(0)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Color ID should match", 0, response.id)
            assertNotNull("Color name should not be null", response.name)
            assertNotNull("Color RGB should not be null", response.rgb)
            
            println("✓ Get Color API test passed - color: ${response.name}")
        } catch (e: Exception) {
            fail("Get Color API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetTheme() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            // Test with a known theme ID (1 = Town)
            val response = legoApi.getTheme(1)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Theme ID should match", 1, response.id)
            assertNotNull("Theme name should not be null", response.name)
            
            println("✓ Get Theme API test passed - theme: ${response.name}")
        } catch (e: Exception) {
            fail("Get Theme API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetPart() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            // Test with a known part number
            val response = legoApi.getPart("3001")
            
            assertNotNull("Response should not be null", response)
            assertEquals("Part number should match", "3001", response.partNum)
            assertNotNull("Part name should not be null", response.name)
            assertNotNull("Part category ID should not be null", response.partCatId)
            
            println("✓ Get Part API test passed - part: ${response.name}")
        } catch (e: Exception) {
            fail("Get Part API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetSet() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            // Test with a known set number
            val response = legoApi.getSet("75192-1")
            
            assertNotNull("Response should not be null", response)
            assertEquals("Set number should match", "75192-1", response.setNum)
            assertNotNull("Set name should not be null", response.name)
            assertNotNull("Theme ID should not be null", response.themeId)
            
            println("✓ Get Set API test passed - set: ${response.name}")
        } catch (e: Exception) {
            fail("Get Set API test failed: ${e.message}")
        }
    }

    // =====================================================
    // Error Handling Tests
    // =====================================================

    @Test
    fun testInvalidSetNumber() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            legoApi.getSet("invalid-set-number")
            fail("Should have thrown an exception for invalid set number")
        } catch (e: Exception) {
            // Expected - invalid set number should cause an error
            assertTrue("Should be a network or HTTP error", 
                e.message?.contains("404") == true || 
                e.message?.contains("Not Found") == true ||
                e is IOException)
            println("✓ Invalid Set Number error handling test passed")
        }
    }

    @Test
    fun testPaginationColors() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            // Test first page
            val page1 = legoApi.getColorsList(page = 1, pageSize = 3)
            assertNotNull("Page 1 should not be null", page1)
            assertEquals("Page 1 should have 3 results", 3, page1.results.size)
            
            // Test second page if there are more results
            if (page1.next != null) {
                val page2 = legoApi.getColorsList(page = 2, pageSize = 3)
                assertNotNull("Page 2 should not be null", page2)
                assertTrue("Page 2 should have results", page2.results.isNotEmpty())
                
                // Ensure different results
                assertNotEquals("Pages should have different results", 
                    page1.results.first().id, page2.results.first().id)
            }
            
            println("✓ Pagination test passed")
        } catch (e: Exception) {
            fail("Pagination test failed: ${e.message}")
        }
    }

    @Test
    fun testComplexObjectParsing() = runBlocking {
        if (!this@LegoApiUnitTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            // Get set parts to test complex nested objects
            val parts = legoApi.getSetPartsList("75192-1", page = 1, pageSize = 3)
            
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
            
            println("✓ Complex Object Parsing test passed")
        } catch (e: Exception) {
            fail("Complex Object Parsing test failed: ${e.message}")
        }
    }
}