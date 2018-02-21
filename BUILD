load("//bzl:rules.bzl", "pkg_all", "believe_binary", "pkg_zip")

BELIEVE_MAIN_CLASS = "believe.app.game.Believe"

BELIEVE_DATA_FILES = [
    "//customFlowFiles:custom_flow_files",
    "//customSongs:custom_songs",
]

BELIEVE_RES = [
    "//data",
    "//levelFlowFiles:level_flow_files",
    "//res",
]

believe_binary(
    name = "Believe_windows_x86",
    data = BELIEVE_DATA_FILES + ["//lib/native:windows_x86"],
    jar_name = "Believe.jar",
    main_class = BELIEVE_MAIN_CLASS,
    resources = BELIEVE_RES,
    runtime_deps = ["//src/believe/app/game"],
)

pkg_zip(
    name = "Believe_windows_x86_pkg",
    deps = [":Believe_windows_x86"],
)

believe_binary(
    name = "Believe_windows_x64",
    data = BELIEVE_DATA_FILES + ["//lib/native:windows_x64"],
    jar_name = "Believe.jar",
    main_class = BELIEVE_MAIN_CLASS,
    resources = BELIEVE_RES,
    runtime_deps = ["//src/believe/app/game"],
)

pkg_zip(
    name = "Believe_windows_x64_pkg",
    deps = [":Believe_windows_x64"],
)

believe_binary(
    name = "Believe_linux_x86",
    data = BELIEVE_DATA_FILES + ["//lib/native:linux_x86"],
    jar_name = "Believe.jar",
    main_class = BELIEVE_MAIN_CLASS,
    resources = BELIEVE_RES,
    runtime_deps = ["//src/believe/app/game"],
)

pkg_zip(
    name = "Believe_linux_x86_pkg",
    deps = [":Believe_linux_x86"],
)

believe_binary(
    name = "Believe_linux_x64",
    data = BELIEVE_DATA_FILES + ["//lib/native:linux_x64"],
    jar_name = "Believe.jar",
    main_class = BELIEVE_MAIN_CLASS,
    resources = BELIEVE_RES,
    runtime_deps = ["//src/believe/app/game"],
)

pkg_zip(
    name = "Believe_linux_x64_pkg",
    deps = [":Believe_linux_x64"],
)

believe_binary(
    name = "Believe_mac",
    data = BELIEVE_DATA_FILES + ["//lib/native:mac"],
    jar_name = "Believe.jar",
    main_class = BELIEVE_MAIN_CLASS,
    resources = BELIEVE_RES,
    runtime_deps = ["//src/believe/app/game"],
)

pkg_zip(
    name = "Believe_mac_pkg",
    deps = [":Believe_mac"],
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
