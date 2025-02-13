package com.lightswitch.infrastructure.database.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JsonConverterTest {
    private val converter = JsonConverter()
    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `convertToDatabaseColumn should serialize object to JSON string`() {
        val data = mapOf("key" to "value", "number" to 123)
        val jsonString = converter.convertToDatabaseColumn(data)

        val expectedJson = objectMapper.writeValueAsString(data)
        assertThat(expectedJson).isEqualTo(jsonString)
    }

    @Test
    fun `convertToEntityAttribute should deserialize JSON string to object`() {
        val jsonString = """{"key":"value","number":123}"""
        val result = converter.convertToEntityAttribute(jsonString)

        assertThat(result).isNotNull()
        assertThat(result is Map<*, *>).isTrue()
        assertThat("value").isEqualTo((result as Map<*, *>)["key"])
        assertThat(123).isEqualTo(result["number"])
    }

    @Test
    fun `convertToDatabaseColumn should handle null values`() {
        val result = converter.convertToDatabaseColumn(null)
        assertThat(result).isEqualTo("null")
    }

    @Test
    fun `convertToEntityAttribute should handle null values`() {
        val result = converter.convertToEntityAttribute(null)
        assertThat(result).isNull()
    }

    @Test
    fun `convertToEntityAttribute should throw exception for invalid JSON`() {
        val invalidJson = "{key:value}"

        assertThrows<Exception> {
            converter.convertToEntityAttribute(invalidJson)
        }
    }

    @Test
    fun `convertToDatabaseColumn should serialize single item correctly`() {
        val data = "aaa"
        val jsonString = converter.convertToDatabaseColumn(data)

        val expectedJson = objectMapper.writeValueAsString(data)
        assertThat(jsonString).isEqualTo(expectedJson)
    }

    @Test
    fun `convertToEntityAttribute should deserialize single item correctly`() {
        val jsonString = "\"aaa\""
        val result = converter.convertToEntityAttribute(jsonString)

        assertThat(result).isNotNull
        assertThat(result is String).isTrue()
        assertThat(result).isEqualTo("aaa")
    }
}
