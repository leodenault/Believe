load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "TiledCommandParserImplTest",
    srcs = ["TiledCommandParserImplTest.kt"],
    friends = ["//java/believe/map/tiled/command:impl"],
    deps = [
        "//java/believe/command",
        "//java/believe/command/proto:command_java_proto",
        "//java/believe/core",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled/command:impl",
        "//java/believe/proto/testing",
        "//third_party/proto",
        "//third_party/truth",
    ],
)
