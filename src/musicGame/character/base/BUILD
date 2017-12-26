package(
    default_visibility = [
        "//src/musicGame/character:__pkg__",
        "//src/musicGame/map/gui:__pkg__",
        "//src/musicGame/physics/collision:__pkg__",
    ],
)

java_library(
    name = "base",
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
