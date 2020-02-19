load("//bazel/bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "MapBackgroundTest",
    srcs = ["MapBackgroundTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/gui/testing",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/gui",
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/slick",
    ],
)
