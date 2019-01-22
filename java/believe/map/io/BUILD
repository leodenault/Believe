package(
    default_visibility = [
        "//java/believe/character:character_pkg",
        "//java/believe/map/collidable:__pkg__",
        "//java/believe/map/gui:__pkg__",
        "//javatests/believe:believe_tests",
    ],
)

java_library(
    name = "properties",
    srcs = ["MapProperties.java"],
    deps = [
        ":map_entity_generator",
        "//java/believe/character/playable",
        "//java/believe/map/collidable",
        "//third_party/slick",
    ],
)

java_library(
    name = "map_entity_generator",
    srcs = ["MapEntityGenerator.java"],
    deps = ["//third_party/slick"],
)
