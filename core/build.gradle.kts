plugins {
    id("java")
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.serialization)
}

group = "com.github.ahmedwelhakim.schematocode"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}