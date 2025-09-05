package eu.mpwg.android.rebrickable.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Working unit tests for LegoApi endpoints that bypass Android dependencies.
 * Creates API client directly without using Android Log.
 */
class LegoApiWorkingTest {

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
        
        // Create API client directly without Android dependencies
        try {
            legoApi = createLegoApiClient(apiKey)
            println("✓ API client created successfully with key: ${apiKey.take(8)}...")
        } catch (e: Exception) {
            fail("Failed to setup API client: ${e.message}")
        }
    }
    
    /**
     * Creates LegoApi client directly without Android configuration
     */
    private fun createLegoApiClient(apiKey: String): LegoApi {
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("authorization", "key $apiKey")
            chain.proceed(requestBuilder.build())
        }
        
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rebrickable.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        return retrofit.create(LegoApi::class.java)
    }

    // =====================================================
    // Core API Tests
    // =====================================================

    @Test
    fun testGetColorsList() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Colors List API...")
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
            
            println("✅ Colors List API test PASSED - found ${response.count} colors")
            println("   First color: ${firstColor.name} (ID: ${firstColor.id}, RGB: ${firstColor.rgb})")
        } catch (e: Exception) {
            println("❌ Colors List API test FAILED: ${e.message}")
            fail("Colors List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetColor() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Get Color API...")
            // Test with a known color ID (0 = Black)
            val response = legoApi.getColor(0)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Color ID should match", 0, response.id)
            assertNotNull("Color name should not be null", response.name)
            assertNotNull("Color RGB should not be null", response.rgb)
            
            println("✅ Get Color API test PASSED - color: ${response.name} (RGB: ${response.rgb})")
        } catch (e: Exception) {
            println("❌ Get Color API test FAILED: ${e.message}")
            fail("Get Color API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetSetsList() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Sets List API...")
            val response = legoApi.getSetsList(page = 1, pageSize = 3)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first set has required fields
            val firstSet = response.results.first()
            assertNotNull("Set number should not be null", firstSet.setNum)
            assertNotNull("Set name should not be null", firstSet.name)
            
            println("✅ Sets List API test PASSED - found ${response.count} sets")
            println("   First set: ${firstSet.name} (${firstSet.setNum})")
        } catch (e: Exception) {
            println("❌ Sets List API test FAILED: ${e.message}")
            fail("Sets List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetSet() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Get Set API...")
            // Test with a known set number - use a more common set
            val response = legoApi.getSet("10030-1")
            
            assertNotNull("Response should not be null", response)
            assertEquals("Set number should match", "10030-1", response.setNum)
            assertNotNull("Set name should not be null", response.name)
            assertNotNull("Theme ID should not be null", response.themeId)
            
            println("✅ Get Set API test PASSED - set: ${response.name}")
            println("   Set details: ${response.numParts} parts, theme ID: ${response.themeId}")
        } catch (e: Exception) {
            println("❌ Get Set API test FAILED: ${e.message}")
            // Try with a different set if the first one fails
            try {
                println("Trying with different set...")
                val response2 = legoApi.getSet("6080-1")
                assertNotNull("Response should not be null", response2)
                println("✅ Get Set API test PASSED with alternative set: ${response2.name}")
            } catch (e2: Exception) {
                fail("Get Set API test failed with both test sets: ${e.message} and ${e2.message}")
            }
        }
    }

    @Test
    fun testGetThemesList() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Themes List API...")
            val response = legoApi.getThemesList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first theme has required fields
            val firstTheme = response.results.first()
            assertNotNull("Theme ID should not be null", firstTheme.id)
            assertNotNull("Theme name should not be null", firstTheme.name)
            
            println("✅ Themes List API test PASSED - found ${response.count} themes")
            println("   First theme: ${firstTheme.name} (ID: ${firstTheme.id})")
        } catch (e: Exception) {
            println("❌ Themes List API test FAILED: ${e.message}")
            fail("Themes List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetTheme() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Get Theme API...")
            // Test with a known theme ID (1 = Technic)
            val response = legoApi.getTheme(1)
            
            assertNotNull("Response should not be null", response)
            assertEquals("Theme ID should match", 1, response.id)
            assertNotNull("Theme name should not be null", response.name)
            
            println("✅ Get Theme API test PASSED - theme: ${response.name}")
        } catch (e: Exception) {
            println("❌ Get Theme API test FAILED: ${e.message}")
            fail("Get Theme API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetPartsList() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Parts List API...")
            val response = legoApi.getPartsList(page = 1, pageSize = 3)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first part has required fields
            val firstPart = response.results.first()
            assertNotNull("Part number should not be null", firstPart.partNum)
            assertNotNull("Part name should not be null", firstPart.name)
            
            println("✅ Parts List API test PASSED - found ${response.count} parts")
            println("   First part: ${firstPart.name} (${firstPart.partNum})")
        } catch (e: Exception) {
            println("❌ Parts List API test FAILED: ${e.message}")
            fail("Parts List API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetPart() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Get Part API...")
            // Test with a known part number
            val response = legoApi.getPart("3001")
            
            assertNotNull("Response should not be null", response)
            assertEquals("Part number should match", "3001", response.partNum)
            assertNotNull("Part name should not be null", response.name)
            assertNotNull("Part category ID should not be null", response.partCatId)
            
            println("✅ Get Part API test PASSED - part: ${response.name}")
            println("   Category ID: ${response.partCatId}")
        } catch (e: Exception) {
            println("❌ Get Part API test FAILED: ${e.message}")
            fail("Get Part API test failed: ${e.message}")
        }
    }

    @Test
    fun testGetPartCategoriesList() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Part Categories List API...")
            val response = legoApi.getPartCategoriesList(page = 1, pageSize = 5)
            
            assertNotNull("Response should not be null", response)
            assertTrue("Count should be positive", response.count > 0)
            assertNotNull("Results should not be null", response.results)
            assertTrue("Should have results", response.results.isNotEmpty())
            
            // Verify first category has required fields
            val firstCategory = response.results.first()
            assertNotNull("Category ID should not be null", firstCategory.id)
            assertNotNull("Category name should not be null", firstCategory.name)
            
            println("✅ Part Categories List API test PASSED - found ${response.count} categories")
            println("   First category: ${firstCategory.name} (ID: ${firstCategory.id})")
        } catch (e: Exception) {
            println("❌ Part Categories List API test FAILED: ${e.message}")
            fail("Part Categories List API test failed: ${e.message}")
        }
    }

    // =====================================================
    // Complex Object Parsing Test
    // =====================================================

    @Test
    fun testComplexObjectParsing() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Complex Object Parsing with Set Parts...")
            // Get set parts to test complex nested objects - use a simpler set
            val parts = legoApi.getSetPartsList("6080-1", page = 1, pageSize = 2)
            
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
            
            println("✅ Complex Object Parsing test PASSED")
            println("   Part: ${firstPart.part?.name} (${firstPart.part?.partNum})")
            println("   Color: ${firstPart.color?.name} (RGB: ${firstPart.color?.rgb})")
            println("   Quantity: ${firstPart.quantity}")
        } catch (e: Exception) {
            println("❌ Complex Object Parsing test FAILED: ${e.message}")
            fail("Complex Object Parsing test failed: ${e.message}")
        }
    }

    // =====================================================
    // Pagination Test
    // =====================================================

    @Test
    fun testPagination() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Pagination...")
            // Test first page
            val page1 = legoApi.getColorsList(page = 1, pageSize = 2)
            assertNotNull("Page 1 should not be null", page1)
            assertEquals("Page 1 should have 2 results", 2, page1.results.size)
            
            // Test second page if there are more results
            if (page1.next != null) {
                val page2 = legoApi.getColorsList(page = 2, pageSize = 2)
                assertNotNull("Page 2 should not be null", page2)
                assertTrue("Page 2 should have results", page2.results.isNotEmpty())
                
                // Ensure different results
                assertNotEquals("Pages should have different results", 
                    page1.results.first().id, page2.results.first().id)
                
                println("✅ Pagination test PASSED")
                println("   Page 1 first color: ${page1.results.first().name}")
                println("   Page 2 first color: ${page2.results.first().name}")
            } else {
                println("✅ Pagination test PASSED (no second page available)")
            }
        } catch (e: Exception) {
            println("❌ Pagination test FAILED: ${e.message}")
            fail("Pagination test failed: ${e.message}")
        }
    }

    // =====================================================
    // Error Handling Test
    // =====================================================

    @Test
    fun testErrorHandling() = runBlocking {
        if (!this@LegoApiWorkingTest::legoApi.isInitialized) {
            println("Skipping test - API not initialized")
            return@runBlocking
        }
        
        try {
            println("Testing Error Handling...")
            legoApi.getSet("invalid-set-number-12345")
            println("❌ Error Handling test FAILED: Should have thrown an exception for invalid set number")
            fail("Should have thrown an exception for invalid set number")
        } catch (e: Exception) {
            // Expected - invalid set number should cause an error
            val isExpectedError = e.message?.contains("404") == true || 
                                 e.message?.contains("Not Found") == true ||
                                 e is IOException
            
            assertTrue("Should be a network or HTTP error", isExpectedError)
            println("✅ Error Handling test PASSED - correctly handled invalid set: ${e.message}")
        }
    }
}