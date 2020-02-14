package(
    default_visibility = ["//:believe_all_pkgs"],
)

package_group(
    name = "character_pkg",
    packages = ["//java/believe/character/..."],
)

java_library(
    name = "character",
    srcs = ["Character.java"],
    deps = [
        ":faction",
        "//java/believe/character/proto:playable_character_movement_command_java_proto",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/gui",
        "//java/believe/map/collidable/tile",
        "//java/believe/physics/collision",
        "//java/believe/physics/damage",
        "//java/believe/physics/manager",
        "//java/believe/statemachine",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "faction",
    srcs = ["Faction.java"],
)
