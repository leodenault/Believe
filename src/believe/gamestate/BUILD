package(
    default_visibility = [
        "//src/believe/app:app_pkg",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "gamestate",
    srcs = glob(["*.java"]),
    deps = [
        "//lib",
        "//src/believe/character",
        "//src/believe/character:faction",
        "//src/believe/character/playable",
        "//src/believe/core",
        "//src/believe/gui",
        "//src/believe/levelFlow/component",
        "//src/believe/levelFlow/parsing",
        "//src/believe/levelFlow/parsing/exceptions",
        "//src/believe/map/gui",
        "//src/believe/physics/manager",
        "//src/believe/util",
    ],
)
