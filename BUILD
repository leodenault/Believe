load("//bzl:rules.bzl", "believe_binary", "pkg_all", "pkg_zip")

BELIEVE_MAIN_CLASS = "believe.app.game.Believe"

BELIEVE_DATA_FILES = [
    "//customFlowFiles:custom_flow_files",
    "//customSongs:custom_songs",
]

WINDOWS_X86_NATIVES = [
    "//third_party/lwjgl:windows_x86",
    "//third_party/openal:windows_x86",
]

WINDOWS_X64_NATIVES = [
    "//third_party/lwjgl:windows_x64",
    "//third_party/openal:windows_x64",
]

LINUX_X86_NATIVES = [
    "//third_party/lwjgl:linux_x86",
    "//third_party/openal:linux_x86",
    "//third_party/jinput:linux_x86",
]

LINUX_X64_NATIVES = [
    "//third_party/lwjgl:linux_x64",
    "//third_party/openal:linux_x64",
    "//third_party/jinput:linux_x64",
]

MAC_NATIVES = [
    "//third_party/lwjgl:mac",
    "//third_party/openal:mac",
    "//third_party/jinput:mac",
]

BELIEVE_RES = [
    "//data",
    "//levelFlowFiles:level_flow_files",
    "//res",
]

package_group(
    name = "believe_src_pkgs",
    packages = ["//src/..."],
)

package_group(
    name = "believe_test_pkgs",
    packages = ["//test/..."],
)

package_group(
    name = "believe_all_pkgs",
    includes = [
        ":believe_src_pkgs",
        ":believe_test_pkgs",
    ],
)

believe_binary(
    name = "Believe_windows_x86",
    data = BELIEVE_DATA_FILES + WINDOWS_X86_NATIVES,
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
    data = BELIEVE_DATA_FILES + WINDOWS_X64_NATIVES,
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
    data = BELIEVE_DATA_FILES + LINUX_X86_NATIVES,
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
    data = BELIEVE_DATA_FILES + LINUX_X64_NATIVES,
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
    data = BELIEVE_DATA_FILES + MAC_NATIVES,
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

believe_binary(
    name = "LevelEditor",
    data = LINUX_X64_NATIVES + ["//res/maps"],
    main_class = "believe.app.editor.LevelEditor",
    resources = [
        "//data",
        "//res/graphics",
        "//res/music",
        "//res/sfx",
    ],
    runtime_deps = ["//src/believe/app/editor"],
)

pkg_zip(
    name = "LevelEditor_pkg",
    deps = [":LevelEditor"],
)

believe_binary(
    name = "LevelEditor_windows",
    data = WINDOWS_X64_NATIVES + ["//res/maps"],
    main_class = "believe.app.editor.LevelEditor",
    resources = [
        "//data",
        "//res/graphics",
        "//res/music",
        "//res/sfx",
    ],
    runtime_deps = ["//src/believe/app/editor"],
)

pkg_zip(
    name = "LevelEditor_windows_pkg",
    deps = [":LevelEditor_windows"],
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
