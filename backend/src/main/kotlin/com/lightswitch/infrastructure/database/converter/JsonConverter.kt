package com.lightswitch.infrastructure.database.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonConverter : AttributeConverter<Any, String> {
    override fun convertToDatabaseColumn(attribute: Any?): String {
        return try {
            objectMapper.writeValueAsString(attribute)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert object to JSON: ${e.message}", e)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): Any? {
        return try {
            dbData?.let { objectMapper.readValue(it, Any::class.java) }
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert JSON to object: ${e.message}", e)
        }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
