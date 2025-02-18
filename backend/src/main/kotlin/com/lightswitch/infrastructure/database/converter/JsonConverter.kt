package com.lightswitch.infrastructure.database.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonConverter : AttributeConverter<Any, String> {
    override fun convertToDatabaseColumn(attribute: Any?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): Any? {
        return dbData?.let { objectMapper.readValue(it, Any::class.java) }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
