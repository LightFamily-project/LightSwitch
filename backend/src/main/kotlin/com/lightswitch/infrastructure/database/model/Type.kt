package com.lightswitch.infrastructure.database.model

enum class Type {
    NUMBER,
    BOOLEAN,
    STRING;

    companion object {
        fun from(type: String): Type {
            return when (type.uppercase()) {
                "NUMBER" -> NUMBER
                "BOOLEAN" -> BOOLEAN
                "STRING" -> STRING
                else -> throw IllegalArgumentException("Unsupported type: $type")
            }
        }
    }
}
