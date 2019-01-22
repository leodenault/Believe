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
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/gui",
        "//java/believe/physics/collision",
        "//java/believe/physics/collision:handlers",
        "//java/believe/statemachine",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "faction",
    srcs = ["Faction.java"],
)
