load("//bzl:rules.bzl", "java_junit5_test", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "LevelDataTest",
    srcs = ["LevelDataTest.kt"],
    friends = ["//java/believe/level"],
    deps = [
        "//java/believe/command",
        "//java/believe/command/proto:command_sequence_java_proto",
        "//java/believe/level",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/io",
        "//third_party/truth",
    ],
)
