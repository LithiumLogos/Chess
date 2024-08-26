import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }

        commonTest.dependencies {
            implementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
            runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.lithiumlogos.chess.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.lithiumlogos.chess"
            packageVersion = "1.0.0"
        }
    }
}

// Ensure that tests run during the build process
tasks.named("build") {
    dependsOn("check")
}

tasks.withType<Test> {
    useJUnitPlatform()

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {
            // Optional: Print the name of the suite
            if (suite.parent == null) { // Root suite
                println("Running tests in: ${suite.displayName}")
            }
        }

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // Root suite
                println("Tests finished. ${result.testCount} tests run, ${result.successfulTestCount} succeeded, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped.")
            }
        }

        override fun beforeTest(descriptor: TestDescriptor) {
            // Optional: Print the name of each test before it runs
            println("Running test: ${descriptor.displayName}")
        }

        override fun afterTest(descriptor: TestDescriptor, result: TestResult) {
            // Optional: Print the result of each test after it runs
            println("Test ${descriptor.displayName} ${result.resultType}.")
        }
    })
}