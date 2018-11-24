package(
    default_visibility = [
        "//src/believe/character:character_pkg",
        "//src/believe/map/collidable:__pkg__",
        "//src/believe/map/gui:__pkg__",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "properties",
    srcs = ["MapProperties.java"],
    deps = [
        ":map_entity_generator",
        "//lib/slick",
        "//src/believe/character/playable",
        "//src/believe/map/collidable",
    ],
)

java_library(
    name = "map_entity_generator",
    srcs = ["MapEntityGenerator.java"],
    deps = ["//lib/slick"],
)
