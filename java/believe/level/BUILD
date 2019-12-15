load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "level",
    srcs = [
        "LevelData.kt",
        "LevelManager.kt",
    ],
    deps = [
        "//java/believe/command",
        "//java/believe/level/proto:level_java_proto",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//third_party/javax:annotations",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["LevelManagerImpl.kt"],
    deps = [
        ":internal_qualifiers",
        ":level",
        "//java/believe/datamodel/protodata",
        "//java/believe/level/proto:level_java_proto",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.kt"],
    visibility = ["//visibility:private"],
    deps = ["//third_party/javax:annotations"],
)
