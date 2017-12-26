package(
    default_visibility = [
        "//src/musicGame/character:__pkg__",
        "//src/musicGame/character/base:__pkg__",
        "//src/musicGame/map/collidable:__pkg__",
        "//src/musicGame/physics/manager:__pkg__",
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
        "//src/musicGame/character/base:faction",
        "//src/musicGame/geometry",
        "//src/musicGame/statemachine",
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
        "//src/musicGame/geometry",
        "//src/musicGame/map/collidable",
        "//src/musicGame/physics/gravity",
        "//src/musicGame/physics/manager",
    ],
)
