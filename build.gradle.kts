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

    implementation("io.github.microutils:kotlin-logging:1.6.20")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.github.christophpickl.kpotpourri:logback4k:1.12")
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
