package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "command",
    srcs = [
        "Command.java",
        "CommandCollidable.java",
        "CommandCollisionHandler.java",
        "CommandGenerator.java",
    ],
    deps = [
        ":internal_qualifiers",
        ":package-info",
        "//java/believe/geometry",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/physics/collision",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CommandDaggerModule.java"],
    deps = [
        ":command",
        ":internal_qualifiers",
        ":package-info",
        "//java/believe/map/io",
        "//third_party/dagger",
    ],
)

java_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.java"],
    visibility = ["//visibility:private"],
    deps = [
        ":package-info",
        "//third_party/javax:annotations",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//javatests/believe/map/collidable/command:__pkg__"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)
