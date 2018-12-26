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

fun execute(vararg commands: String, failNotification: Notification? = null, suppressOutput: Boolean = false) {
    val fullCommand = commands.joinToString(" ")
    if (!suppressOutput) println(">> $fullCommand")

    val builder = ProcessBuilder().apply {
        command(commands.toList())
        redirectOutput(ProcessBuilder.Redirect.INHERIT)
        redirectError(ProcessBuilder.Redirect.INHERIT)
    }
//    if (true) { return }
    val process = builder.start()
    val returnCode = process.waitFor()
    if (returnCode != 0) {
        fail(failNotification ?: Notification(
            title = "Command failed",
            message = "Executing '$fullCommand' returned: $returnCode"
        ))
    }
}

fun fail(notification: Notification) {
    System.err.println("[ERROR] ${notification.title} - ${notification.message}")
    displayNotification(notification)
    System.exit(1)
}

fun displayNotification(notification: Notification) {
    execute("osascript", "-e", "display notification \"${notification.message}\" with title \"${notification.title}\"", suppressOutput = true)
}

data class Notification(
    val title: String,
    val message: String
)
