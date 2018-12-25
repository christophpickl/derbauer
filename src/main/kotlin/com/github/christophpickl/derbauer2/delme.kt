package com.github.christophpickl.derbauer2

import java.io.File

fun main(args: Array<String>) {

    println()
    println("GitHub release.")

    val jarFile = File("build/libs/DerBauer2-1.0.jar")

    execute("./bin/upload.sh", "a", "b")

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
    displayNotification("⚠️ ERROR ⚠️️", message)
    System.exit(1)
}

fun displayNotification(title: String, message: String) {
    execute("osascript", "-e", "display notification \"$message\" with title \"$title\"")
}
