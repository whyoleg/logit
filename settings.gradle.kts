pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    val kotlinVersion: String by settings
    val versionUpdatesVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        id("com.github.ben-manes.versions") version versionUpdatesVersion
    }
}

include("logit-core")
