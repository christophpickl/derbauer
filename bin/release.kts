#!/usr/bin/env kscript
//INCLUDE includes.kts

import java.io.File

val versionFile = File("src/main/version.txt")
val currentVersion = versionFile.let {
    require(it.exists()) { "Version file does not exist at: ${it.absolutePath}" }
    Version.parse(it.readText().trim())
}
val nextVersion = currentVersion.incrementMinor()

execute("git", "status")

println()
println("Is everything checked in?")
confirmOrExit()

println("Current version: $currentVersion")
println("Next version:    $nextVersion")
println()
println("Wanna release this?")
confirmOrExit()

println()
println("Test build.")
execute("./gradlew", "clean", "test", "check")

println()
println("Writing version to version.txt file.")
versionFile.writeText(nextVersion.toString())

println()
println("Building fat JAR.")
execute("./gradlew", "shadowJar")

val jarFile = File("build/libs/DerBauer2-${nextVersion}.jar")
require(jarFile.exists()) { "JAR file does not exist at: ${jarFile.absolutePath}" }
println("Created fat JAR at: ${jarFile.absolutePath}")

println()
println("GIT commit, tag and push.")
execute("git", "add", ".")
execute("git", "commit", "-m", "[Auto-Release] Version: $nextVersion")
execute("git", "tag", "v$nextVersion")
execute("git", "push")
execute("git", "push", "origin", "--tags")

println()
println("GitHub release.")
execute("./bin/upload.sh", "v${nextVersion}", jarFile.absolutePath)

data class Version(val major: Int, val minor: Int) {
    companion object {
        fun parse(string: String): Version {
            val parts = string.split(".")
            return Version(parts[0].toInt(), parts[1].toInt())
        }
    }

    fun incrementMinor() = Version(major = major, minor = minor + 1)
    override fun toString() = "$major.$minor"
}

