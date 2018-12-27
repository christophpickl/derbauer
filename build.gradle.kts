import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

object Versions {
    val kpotpourri = "2.2"
}

plugins {
    kotlin("jvm") version "1.3.11"
    idea
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("com.github.ben-manes.versions") version "0.20.0"
}

val artifactName = "DerBauer"
val localProjectVersion = file("src/main/version.txt").readText().trim()
version = localProjectVersion

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven { setUrl("http://dl.bintray.com/christophpickl/cpickl") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("io.github.microutils:kotlin-logging:1.6.22")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.github.christophpickl.kpotpourri:common4k:${Versions.kpotpourri}")
    implementation("com.github.christophpickl.kpotpourri:logback4k:${Versions.kpotpourri}")
    implementation("com.github.christophpickl.kpotpourri:swing4k:${Versions.kpotpourri}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")

    testImplementation("org.testng:testng:6.14.3")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
}


tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

defaultTasks("tasks", "--all")

tasks.withType(Test::class.java).all {
    useTestNG()
}

tasks.withType(Jar::class.java).all {
    manifest {
        attributes["Main-Class"] = "com.github.christophpickl.derbauer.DerBauer"
    }
}

tasks.withType(ShadowJar::class.java).all {
    baseName = artifactName
    classifier = ""
    version = localProjectVersion
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc")
                    .map { Regex("(?i).*[.-]$it[.\\d-]*") }
                    .any { it.matches(candidate.version) }
                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
    checkForGradleUpdate = true
}
