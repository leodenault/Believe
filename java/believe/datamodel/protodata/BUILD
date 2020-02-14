load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "protodata",
    srcs = [
        "BinaryProtoFile.kt",
    ],
    deps = [
        "//java/believe/datamodel",
        "//java/believe/io",
        "//java/believe/proto",
        "//java/believe/proto:impl",
        "//java/believe/util:kt",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)
