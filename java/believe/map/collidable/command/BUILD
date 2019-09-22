package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "command",
    srcs = [
        "Command.java",
        "CommandCollidable.java",
        "CommandCollisionHandler.java",
        "CommandSupplier.java",
    ],
    deps = [
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
    name = "impl",
    srcs = [
        "CommandGenerator.java",
        "CommandSequenceSupplier.java",
    ],
    deps = [
        ":command",
        ":internal_qualifiers",
        ":package-info",
        "//java/believe/character/playable",
        "//java/believe/map/collidable/command/proto:command_sequence_java_proto",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CommandDaggerModule.java"],
    deps = [
        ":command",
        ":impl",
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
