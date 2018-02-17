package(
    default_visibility = [
        "//src/musicGame/character:character_pkg",
        "//src/musicGame/map/gui:__pkg__",
        "//src/musicGame/physics/collision:__pkg__",
    ],
)

package_group(
    name = "character_pkg",
    packages = ["//src/musicGame/character/..."],
)

java_library(
    name = "character",
    srcs = ["Character.java"],
    deps = [
        ":faction",
        "//lib",
        "//src/musicGame/core/display",
        "//src/musicGame/geometry",
        "//src/musicGame/gui/base",
        "//src/musicGame/physics/collision",
        "//src/musicGame/physics/collision:handlers",
        "//src/musicGame/statemachine",
        "//src/musicGame/util",
    ],
)

java_library(
    name = "faction",
    srcs = ["Faction.java"],
)
