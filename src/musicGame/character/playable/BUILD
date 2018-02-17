java_library(
    name = "playable",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/musicGame/gamestate:__pkg__",
        "//src/musicGame/map/gui:__pkg__",
        "//src/musicGame/map/io:__pkg__",
        "//src/musicGame/physics/collision:__pkg__",
    ],
    deps = [
        "//lib",
        "//src/musicGame/character",
        "//src/musicGame/character:faction",
        "//src/musicGame/core",
        "//src/musicGame/map/io:map_entity_generator",
        "//src/musicGame/physics/collision",
        "//src/musicGame/physics/collision:handlers",
        "//src/musicGame/physics/manager",
        "//src/musicGame/statemachine",
        "//src/musicGame/util",
    ],
)
