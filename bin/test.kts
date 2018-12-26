#!/usr/bin/env kscript
//INCLUDE includes.kts

val title = "DerBauer Tests"

execute("./gradlew", "clean", "test", "check", failNotification = Notification(
    title = title,
    message = "Failed to execute tests ⚠️"
))

displayNotification(Notification(
    title = title,
    message = "Executing tests succeeded ✅"
))
