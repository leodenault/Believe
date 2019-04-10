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
        "AppGameContainerSupplier.java",
        "AppUtil.java",
        "Application.java",
        "Launcher.java",
        "StateInstantiator.java",
    ],
    deps = [
        ":qualifiers",
        "//java/believe/app/flag_parsers",
        "//java/believe/app/flags",
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["ApplicationComponent.java"],
    deps = [
        ":app",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ApplicationModule.java"],
    deps = [
        ":app",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel/protodata",
        "//java/believe/gamestate",
        "//java/believe/physics/manager",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "qualifiers",
    srcs = [
        "ApplicationTitle.java",
        "FirstState.java",
        "GameStateInstantiators.java",
    ],
    deps = ["//third_party/javax:annotations"],
)
