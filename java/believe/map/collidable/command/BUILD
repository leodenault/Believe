package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "command",
    srcs = [
        "CollidableCommand.java",
        "CollidableCommandCollisionHandler.java",
        "CollidableCommandGenerator.java",
    ],
    deps = [
        ":internal_qualifiers",
        ":package-info",
        "//java/believe/command",
        "//java/believe/command/proto:command_sequence_java_proto",
        "//java/believe/geometry",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/physics/collision",
        "//java/believe/physics/manager",
        "//third_party/auto_factory",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CollidableCommandDaggerModule.java"],
    deps = [
        ":command",
        ":internal_qualifiers",
        ":package-info",
        "//java/believe/map/io",
        "//java/believe/physics/collision",
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
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)
