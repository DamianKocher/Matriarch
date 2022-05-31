plugins {
    kotlin("jvm") version "1.6.21"
    id("maven-publish")
    id("java")
}

group = "com.damiankocher"
version = "1.0-SNAPSHOT"

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

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        groupId = project.properties["group"] as? String?
        artifactId = project.name
        version = project.properties["version"] as? String?

        from(components["java"])
    }
}