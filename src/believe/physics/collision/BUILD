package(
    default_visibility = [
        "//src/believe/character:character_pkg",
        "//src/believe/map/collidable:__pkg__",
        "//src/believe/physics/manager:__pkg__",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "collision",
    srcs = [
        "Collidable.java",
        "CommandCollidable.java",
        "DamageBox.java",
        "DamageBoxCollidable.java",
    ],
    deps = [
        "//src/believe/character:faction",
        "//src/believe/geometry",
        "//src/believe/statemachine",
    ],
)

java_library(
    name = "handlers",
    srcs = [
        "CollisionHandler.java",
        "CommandCollisionHandler.java",
        "DamageHandler.java",
        "TileCollisionHandler.java",
    ],
    deps = [
        ":collision",
        "//src/believe/character:faction",
        "//src/believe/geometry",
        "//src/believe/map/collidable",
        "//src/believe/physics/gravity",
        "//src/believe/physics/manager",
    ],
)
