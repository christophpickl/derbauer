package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.DEV_MODE
import com.github.christophpickl.derbauer.currentVersion
import com.github.christophpickl.kpotpourri.common.version.VersionChecker
import mu.KotlinLogging.logger
import java.net.URL

object DerBauerVersionChecker {

    private val log = logger {}

    private val versionFileUrl = "https://raw.githubusercontent.com/christophpickl/derbauer/" +
        "master/src/main/resources/derbauer/version.txt"

    private val downloadPattern = "https://github.com/christophpickl/derbauer/releases/download/{0}/DerBauer-{0}.jar"

    fun checkLatestVersion() {
        if (DEV_MODE) {
            log.debug { "Not checking for latest version while in DEV mode." }
            return
        }

        val runnable = Runnable {
            VersionChecker.checkAndShowDialog(
                currentVersion = currentVersion,
                urlOfLatestVersionFile = URL(versionFileUrl),
                downloadPattern = downloadPattern
            )
        }
        Thread(runnable, "VersionCheckThread").start()
    }
}
