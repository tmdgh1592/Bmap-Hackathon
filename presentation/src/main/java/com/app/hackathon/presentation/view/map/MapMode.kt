package com.app.hackathon.presentation.view.map

enum class MapMode {
    INACTIVE, ACTIVE, DIRECTION_ACTIVE;

    fun changeMode(): MapMode {
        return when {
            this == INACTIVE -> {
                ACTIVE
            }
            this == ACTIVE -> {
                DIRECTION_ACTIVE
            }
            else -> { // DIRECTION_ACTIVE
                INACTIVE
            }
        }
    }
}