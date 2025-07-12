plugins {
    id("java-library")
    kotlin("jvm") version "2.2.0"
}

group = "io.github.mucute.qwq.kolomitm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "opencollabRepositoryMavenSnapshots"
        url = uri("https://repo.opencollab.dev/maven-snapshots")
    }
    maven {
        name = "opencollabRepositoryMavenReleases"
        url = uri("https://repo.opencollab.dev/maven-releases")
    }
}

dependencies {
    implementation(libs.bedrock.codec)
    implementation(libs.common)
    implementation(libs.bedrock.connection)
    implementation(libs.transport.raknet)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.net.raphimc.minecraftauth)
    implementation(libs.jackson.databind)
    implementation(platform(libs.log4j.bom))
    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}