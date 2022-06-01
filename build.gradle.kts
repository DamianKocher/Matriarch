import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("maven-publish")
    id("java-library")
}

group = "com.damiankocher"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("com.github.minestom:minestom:58b6e90142")

    testImplementation(kotlin("test"))
    testImplementation("com.github.minestom:minestom:58b6e90142")
    testImplementation("org.joml:joml:1.10.4")
}

tasks.test {
    useJUnitPlatform()
}

val jvmTarget = JavaVersion.VERSION_17.toString()
val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions.jvmTarget = jvmTarget

publishing {
    publications.create<MavenPublication>("maven") {
        groupId = project.properties["group"] as? String?
        artifactId = project.name
        version = project.properties["version"] as? String?

        from(components["java"])
    }
}