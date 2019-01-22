package(
    default_visibility = [
        "//java/believe/character:character_pkg",
        "//java/believe/map/collidable:__pkg__",
        "//java/believe/physics/manager:__pkg__",
        "//javatests/believe:believe_tests",
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
        "//java/believe/character:faction",
        "//java/believe/geometry",
        "//java/believe/statemachine",
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
        "//java/believe/character:faction",
        "//java/believe/geometry",
        "//java/believe/map/collidable",
        "//java/believe/physics/gravity",
        "//java/believe/physics/manager",
    ],
)
