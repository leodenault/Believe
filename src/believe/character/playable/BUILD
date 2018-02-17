java_library(
    name = "playable",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/believe/gamestate:__pkg__",
        "//src/believe/map/gui:__pkg__",
        "//src/believe/map/io:__pkg__",
        "//src/believe/physics/collision:__pkg__",
    ],
    deps = [
        "//lib",
        "//src/believe/character",
        "//src/believe/character:faction",
        "//src/believe/core",
        "//src/believe/map/io:map_entity_generator",
        "//src/believe/physics/collision",
        "//src/believe/physics/collision:handlers",
        "//src/believe/physics/manager",
        "//src/believe/statemachine",
        "//src/believe/util",
    ],
)
