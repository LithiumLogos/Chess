plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.testLogger) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.dataframe) apply false
    alias(libs.plugins.ksp) apply false
}