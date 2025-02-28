package com.lightswitch.infrastructure.database.model

enum class SdkType {
    JAVA,
    PYTHON,
    ;

    companion object {
        fun from(type: String): SdkType {
            return when (type.uppercase()) {
                "JAVA" -> JAVA
                "PYTHON" -> PYTHON
                else -> throw IllegalArgumentException("Unsupported type: $type")
            }
        }
    }
}
