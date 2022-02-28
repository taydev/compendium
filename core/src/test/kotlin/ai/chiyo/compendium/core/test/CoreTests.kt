package ai.chiyo.compendium.core.test

import ai.chiyo.compendium.core.element.source.Source
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CoreTests {

    private var source = Source("Test Source")

    @BeforeEach
    fun init() {
        source = Source("Test Source")
    }

    @Test
    fun testSourceData() {
        println("Testing source data accuracy...")
        assertEquals(source.name, "Test Source")
        assertEquals(source.abbreviation, "TS")
    }

    @Test
    fun testSourceAbbreviationUpdate() {
        println("Testing source abbreviation update...")
        assertEquals(source.name, "Test Source")
        assertEquals(source.abbreviation, "TS")
        source.name = "New Test Source"
        assertEquals(source.name, "New Test Source")
        assertNotEquals(source.abbreviation, "NTS")
        source.updateAbbreviation()
        assertEquals(source.abbreviation, "NTS")
    }

}