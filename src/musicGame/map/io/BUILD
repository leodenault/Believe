package(
    default_visibility = [
        "//src/musicGame/character:character_pkg",
        "//src/musicGame/map/collidable:__pkg__",
        "//src/musicGame/map/gui:__pkg__",
        "//test/musicGame:believe_tests",
    ],
)

java_library(
    name = "properties",
    srcs = ["MapProperties.java"],
    deps = [
        ":map_entity_generator",
        "//lib",
        "//src/musicGame/character/playable",
        "//src/musicGame/map/collidable",
    ],
)

java_library(
    name = "map_entity_generator",
    srcs = ["MapEntityGenerator.java"],
    deps = ["//lib"],
)
