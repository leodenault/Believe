load("//bzl:rules.bzl", "pkg_for_platform", "pkg_all")
load("//bzl:enums.bzl", "ARCHITECTURE", "OS")

pkg_for_platform(
    name = "Believe",
    architecture = ARCHITECTURE.X86,
    os = OS.WINDOWS,
)

pkg_for_platform(
    name = "Believe",
    architecture = ARCHITECTURE.X64,
    os = OS.WINDOWS,
)

pkg_for_platform(
    name = "Believe",
    architecture = ARCHITECTURE.X86,
    os = OS.LINUX,
)

pkg_for_platform(
    name = "Believe",
    architecture = ARCHITECTURE.X64,
    os = OS.LINUX,
)

pkg_for_platform(
    name = "Believe",
    os = OS.MAC,
)

alias(
    name = "Believe",
    actual = ":Believe_linux_x64",
)

pkg_all(
    name = "Believe_all",
    deps = [
        ":Believe_linux_x64_pkg",
        ":Believe_linux_x86_pkg",
        ":Believe_mac_pkg",
        ":Believe_windows_x64_pkg",
        ":Believe_windows_x86_pkg",
    ],
)
