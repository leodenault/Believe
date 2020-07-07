load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "animation",
    srcs = [
        "SpriteSheetDataManagerFactory.kt",
        "SpriteSheetDataParser.kt",
    ],
    deps = [
        ":internal_qualifiers",
        "//java/believe/animation/proto:animation_java_proto",
        "//java/believe/datamodel",
        "//java/believe/datamodel/protodata",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["AnimationDaggerModule.java"],
    data = ["//java/believe/animation/data"],
    deps = [
        ":animation",
        ":internal_qualifiers",
        "//java/believe/datamodel",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.kt"],
    visibility = ["//visibility:private"],
    deps = ["//third_party/javax:annotations"],
)
