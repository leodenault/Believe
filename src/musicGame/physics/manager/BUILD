java_library(
    name = "manager",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/musicGame/character:character_pkg",
        "//src/musicGame/gamestate:__pkg__",
        "//src/musicGame/physics/collision:__pkg__",
        "//test/musicGame:believe_tests",
    ],
    deps = [
        "//src/musicGame/physics/collision",
        "//src/musicGame/physics/gravity",
    ],
)
