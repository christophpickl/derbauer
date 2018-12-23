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
    implementation(kotlin("stdlib", "1.3.11"))
    implementation(kotlin("reflect", "1.3.11"))

    implementation("no.tornado:tornadofx:1.7.17") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    }
    implementation("com.google.inject:guice:4.2.2")
    implementation("io.github.microutils:kotlin-logging:1.6.20")
    implementation("ch.qos.logback:logback-classic:1.2.3")
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
