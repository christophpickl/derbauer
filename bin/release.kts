#!/usr/bin/env kscript

@file:DependsOn("com.github.christophpickl.kpotpourri:build4k:SNAPSHOT")
@file:DependsOn("com.github.christophpickl.kpotpourri:common4k:SNAPSHOT")
@file:DependsOn("com.github.christophpickl.kpotpourri:logback4k:SNAPSHOT")
@file:CompilerOpts("-jvm-target 1.8")

import java.io.File
import com.github.christophpickl.kpotpourri.build.build4k
import com.github.christophpickl.kpotpourri.build.Build4k
import com.github.christophpickl.kpotpourri.build.Version
import com.github.christophpickl.kpotpourri.build.Version2

build4k {
    title = "DerBauer Release"
    val versionFile = File("src/main/version.txt")
    val artifactId = "DerBauer"
    val currentVersion = readFromFile<Version2>(versionFile)
    val nextVersion = currentVersion.incrementPart2()

    verifyGitStatus()
    println()
    println("Current version: $currentVersion")
    println("Next version:    $nextVersion")
    println()
    if (!confirm("Do you really want to release this? (This is your final warning)")) {
        exit()
    }
    printHeader("Test Build")
    gradlew("clean", "test", "check")

    printHeader("Write next version")
    versionFile.writeText(nextVersion.toString())
    println("Written '$nextVersion' to file: ${versionFile.absolutePath}")

    buildFatJar(artifactId, nextVersion)
    gitTagPush(nextVersion)

    printHeader("GitHub upload")
    execute("./bin/upload.sh", listOf("v${nextVersion}", jarFile.absolutePath))

    displayNotification("Release build succeeded ✅")
    say("Hey you! The release build finished successfully.")
}

fun Build4k.verifyGitStatus() {
    git("status")
    println()
    if (!confirm("Is everything checked in?")) {
        exit()
    }
}

fun Build4k.buildFatJar(artifactId: String, version: Version): File {
    printHeader("Build FatJAR")
    gradlew("shadowJar")
    val jarFile = File("build/libs/$artifactId-$version.jar")
    require(jarFile.exists()) { "JAR file does not exist at: ${jarFile.absolutePath}" }
    println("Created Fat JAR at: ${jarFile.absolutePath}")
    return jarFile
}

fun Build4k.gitTagPush(nextVersion: Version) {
    printHeader("GIT tag&push")
    git("add", ".")
    git("commit", "-m", "[Auto-Release] Version: $nextVersion")
    git("tag", "v$nextVersion")
    git("push")
    git("push", "origin", "--tags")
}
