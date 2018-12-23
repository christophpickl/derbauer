import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    idea
}

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("http://dl.bintray.com/christophpickl/cpickl") }
}

// DEPENDENCIES ========================================================================================================

dependencies {
    // lang
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("no.tornado:tornadofx:1.7.17") {
        exclude(group = "org.jetbrain.kotlin", module = "kotlin-stdlib-jdk8")
    }

    // misc
    implementation("io.github.microutils:kotlin-logging:1.6.20")

    // TODO test: testng, assertj
}


tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

defaultTasks("tasks", "--all")

tasks.withType(Test::class.java).all {
    // TODO useTestNG()
}
