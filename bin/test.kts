#!/usr/bin/env kscript

@file:DependsOn("com.github.christophpickl.kpotpourri:build4k:SNAPSHOT")
@file:DependsOn("com.github.christophpickl.kpotpourri:common4k:SNAPSHOT")
@file:DependsOn("com.github.christophpickl.kpotpourri:logback4k:SNAPSHOT")

import com.github.christophpickl.kpotpourri.build.build4k

build4k {
    title = "DerBauer Tests"

    gradlew("clean", "test", "check", context = whenFail("Test build failed ⚠️"))
    displayNotification("Test build succeeded ✅")
}
