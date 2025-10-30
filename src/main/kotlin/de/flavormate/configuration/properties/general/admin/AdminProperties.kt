package de.flavormate.configuration.properties.general.admin

interface AdminProperties {
    fun displayName(): String
    fun username(): String
    fun password(): String
    fun email(): String
}