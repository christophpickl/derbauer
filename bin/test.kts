#!/usr/bin/env kscript
@file:DependsOn("com.github.christophpickl.kpotpourri:build4k:2.2")
@file:CompilerOpts("-jvm-target 1.8")

import com.github.christophpickl.kpotpourri.build.*

build4k {
    title = "DerBauer Tests"
    gradlew("clean", "test", "check", context = whenFail("Test build failed ⚠️"))
    displayNotification("Test build succeeded ✅")
}
