package(
    default_visibility = [
        "//src/believe/character:character_pkg",
        "//src/believe/gamestate:__pkg__",
        "//src/believe/map/gui:__pkg__",
        "//src/believe/physics/collision:__pkg__",
    ],
)

package_group(
    name = "character_pkg",
    packages = ["//src/believe/character/..."],
)

java_library(
    name = "character",
    srcs = ["Character.java"],
    deps = [
        ":faction",
        "//lib",
        "//src/believe/core/display",
        "//src/believe/geometry",
        "//src/believe/gui",
        "//src/believe/physics/collision",
        "//src/believe/physics/collision:handlers",
        "//src/believe/statemachine",
        "//src/believe/util",
    ],
)

java_library(
    name = "faction",
    srcs = ["Faction.java"],
)
