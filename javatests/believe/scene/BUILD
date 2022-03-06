load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "CameraTest",
    srcs = ["CameraTest.kt"],
    deps = [
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/react:impl",
        "//java/believe/scene",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "LevelMapTest",
    srcs = ["LevelMapTest.kt"],
    associates = ["//java/believe/scene:impl"],
    deps = [
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/gui/testing",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/physics/manager",
        "//third_party/mockito:mockito_kotlin",
    ],
)

kt_junit5_test(
    name = "MapBackgroundTest",
    srcs = ["MapBackgroundTest.kt"],
    associates = ["//java/believe/scene:impl"],
    deps = [
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/gui/testing",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//third_party/mockito:mockito_kotlin",
    ],
)
