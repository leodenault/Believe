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
        "AppUtil.java",
        "Launcher.java",
        "StateInstantiator.java",
    ],
    deps = [
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["ApplicationComponent.java"],
    deps = [
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ApplicationModule.java"],
    deps = [
        "//java/believe/app/flag_parsers",
        "//java/believe/app/flags",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/datamodel",
        "//java/believe/datamodel/protodata",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)
