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
        "//src/believe/character/playable",
        "//src/believe/map/collidable",
        "//third_party/slick",
    ],
)

java_library(
    name = "map_entity_generator",
    srcs = ["MapEntityGenerator.java"],
    deps = ["//third_party/slick"],
)
