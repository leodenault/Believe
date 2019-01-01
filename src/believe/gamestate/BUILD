package(
    default_visibility = [
        "//src/believe/app:app_pkg",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "gamestate",
    srcs = glob(
        ["*.java"],
        exclude = ["package-info.java"],
    ),
    deps = [
        ":package-info",
        "//src/believe/character",
        "//src/believe/character:faction",
        "//src/believe/character/playable",
        "//src/believe/core",
        "//src/believe/core/io",
        "//src/believe/graphics_transitions",
        "//src/believe/gui",
        "//src/believe/levelFlow/component",
        "//src/believe/levelFlow/parsing",
        "//src/believe/levelFlow/parsing/exceptions",
        "//src/believe/map/gui",
        "//src/believe/physics/manager",
        "//src/believe/util",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//:believe_test_pkgs"],
    deps = [
        "//src/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)
