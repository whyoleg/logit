import org.jetbrains.kotlin.gradle.dsl.*

buildscript {
    repositories {
        mavenCentral()
    }

    val kotlinVersion: String by rootProject

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("com.github.ben-manes.versions")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.all {
                val suffixIndex = name.indexOfLast { it.isUpperCase() }
                val targetName = name.substring(0, suffixIndex)
                val (srcPath, resourcesPath) = when (val suffix = name.substring(suffixIndex).toLowerCase()) {
                    "main" -> "src" to "resources"
                    else   -> suffix to "${suffix}Resources"
                }
                kotlin.setSrcDirs(listOf("$targetName/$srcPath"))
                resources.setSrcDirs(listOf("$targetName/$resourcesPath"))

                languageSettings.apply {
                    progressiveMode = true
                    languageVersion = "1.4"
                    apiVersion = "1.4"

                    useExperimentalAnnotation("kotlin.RequiresOptIn")
                }
            }
        }
    }
}
