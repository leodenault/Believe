load("//bzl:rules.bzl", "kt_junit5_test")

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
        "//java/believe/command/proto:command_java_proto",
        "//java/believe/level",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/io",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "LevelManagerImplTest",
    srcs = ["LevelManagerImplTest.kt"],
    friends = ["//java/believe/level:impl"],
    deps = [
        "//java/believe/command/proto:command_java_proto",
        "//java/believe/datamodel",
        "//java/believe/datamodel/protodata/testing",
        "//java/believe/level:impl",
        "//java/believe/level/proto:level_java_proto",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/data",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)
