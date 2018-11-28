package(
    default_visibility = ["//:believe_all_pkgs"],
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
        "//src/believe/core/display",
        "//src/believe/geometry",
        "//src/believe/gui",
        "//src/believe/physics/collision",
        "//src/believe/physics/collision:handlers",
        "//src/believe/statemachine",
        "//src/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "faction",
    srcs = ["Faction.java"],
)
