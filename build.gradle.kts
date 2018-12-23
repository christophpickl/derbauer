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


    // misc
    implementation("io.github.microutils:kotlin-logging:1.6.20")
    implementation("com.github.christophpickl.kpotpourri:logback4k:1.12")

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
