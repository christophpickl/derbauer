package com.github.christophpickl.derbauer2

import java.io.File

fun main(args: Array<String>) {

    val versionFile = File("version.txt")
    val currentVersion = versionFile.let {
        require(it.exists()) { "Version file does not exist at: ${it.absolutePath}" }
        Version.parse(it.readText().trim())
    }
    val nextVersion = currentVersion.incrementMinor()

    execute("git", "status")
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

    println()
    println("GIT commit, tag and push.")
    execute("git", "add", ".")
    execute("git", "commit", "-m", "[Auto-Release] Version: $nextVersion")
    execute("git", "tag", nextVersion.toString())
    execute("git", "push")
    execute("git", "push", "origin", "--tags")

    println()
    println("GitHub release.")


//
//    echo "Creating new release in GitHub ..."
//    safeEval "curl \
//        --header \"Authorization: token ${NAMASTE_GITHUB_TOKEN}\" \
//        --header \"Content-Type: application/json\" \
//        --request POST \
//        --data '{ \"tag_name\": \"${NEXT_vVERSION}\", \"target_commitish\": \"master\", \"name\": \"${NEXT_vVERSION}\", \"body\": \"Namaste Release\", \"draft\": false, \"prerelease\": false }' \
//        https://api.github.com/repos/christophpickl/namaste/releases"
//    UPLOAD_URL=`echo $LAST_RESULT | jq -r '.upload_url' | sed -e 's/{?name,label}//g'`
//    
//    echo ""
//    echo "Uploading asset to GitHub ..."
//    safeEval "curl \
//        --header \"Authorization: token ${NAMASTE_GITHUB_TOKEN}\" \
//        --header \"Content-Type: application/zip\" \
//        --request POST \
//        --data-binary @${TARGET_ZIP_FILE} \
//        $UPLOAD_URL?name=namaste-${NEXT_vVERSION}.zip"

}

fun confirmOrExit() {
    if (!confirm()) {
        println()
        println("Aborted by user.")
        System.exit(1)
    }
}

fun confirm(): Boolean {
    print("[y/n] ")
    return readLine() == "y"
}

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


fun execute(vararg commands: String) {
    val fullCommand = commands.joinToString(" ")
    println(">> $fullCommand")

    val builder = ProcessBuilder().apply {
        command(commands.toList())
        redirectOutput(ProcessBuilder.Redirect.INHERIT)
        redirectError(ProcessBuilder.Redirect.INHERIT)
    }
//    if (true) { return }
    val process = builder.start()
    val returnCode = process.waitFor()
    if (returnCode != 0) {
        fail("Failed to execute command: $fullCommand")
    }
}

fun fail(message: String) {
    System.err.println("⚠ ERROR ⚠ $message")
    displayNotification("⚠ ERROR ⚠", message)
    System.exit(1)
}

fun displayNotification(title: String, message: String) {
    execute("osascript", "-e", "display notification \"$message\" with title \"$title\"")
}
