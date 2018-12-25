fun confirmOrExit() {
    if (!confirm()) {
        println()
        println("Aborted by user.")
        System.exit(1)
    }
}

fun confirm(): Boolean {
    print("[y/n] ")
    val result = readLine() == "y"
    println()
    return result
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
