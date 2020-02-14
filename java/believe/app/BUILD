load("//bzl:rules.bzl", "textproto")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

package_group(
    name = "app_pkg",
    packages = ["//java/believe/app/..."],
)

java_library(
    name = "app",
    srcs = [
        "Application.java",
    ],
    deps = [
        ":game_container_supplier",
        ":qualifiers",
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "game_container_supplier",
    srcs = ["AppGameContainerSupplier.java"],
    deps = [
        "//java/believe/app/flag_parsers",
        "//java/believe/app/flags",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "util",
    srcs = ["AppUtil.java"],
    deps = [
        ":dagger_component",
        ":game_container_supplier",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["ApplicationComponent.java"],
    deps = [
        ":app",
        ":game_container_supplier",
        "//java/believe/audio",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ApplicationModule.java"],
    deps = [
        ":app",
        ":game_container_supplier",
        ":qualifiers",
        "//java/believe/action",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/character/playable:dagger_module",
        "//java/believe/command:dagger_module",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue:dagger_module",
        "//java/believe/gamestate:impl",
        "//java/believe/gamestate/levelstate/platformingstate",
        "//java/believe/gamestate/levelstate/platformingstate:qualifiers",
        "//java/believe/gui:dagger_module",
        "//java/believe/io:dagger_module",
        "//java/believe/level:dagger_module",
        "//java/believe/map/collidable/command:dagger_module",
        "//java/believe/map/collidable/tile:dagger_module",
        "//java/believe/map/gui",
        "//java/believe/map/io:dagger_module",
        "//java/believe/map/tiled/command:dagger_module",
        "//java/believe/physics/collision:dagger_module",
        "//java/believe/physics/manager",
        "//java/believe/proto:dagger_module",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "qualifiers",
    srcs = [
        "ApplicationTitle.java",
        "FirstState.java",
        "GameStates.java",
    ],
    deps = ["//third_party/javax:annotations"],
)
