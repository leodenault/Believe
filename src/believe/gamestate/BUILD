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
        "//lib:javax_annotations",
        "//lib/slick",
        "//src/believe/character",
        "//src/believe/character:faction",
        "//src/believe/character/playable",
        "//src/believe/core",
        "//src/believe/graphics_transitions",
        "//src/believe/gui",
        "//src/believe/levelFlow/component",
        "//src/believe/levelFlow/parsing",
        "//src/believe/levelFlow/parsing/exceptions",
        "//src/believe/map/gui",
        "//src/believe/physics/manager",
        "//src/believe/util",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//:believe_test_pkgs"],
    deps = [
        "//lib:javax_annotations",
        "//src/believe/app/annotation",
    ],
)
