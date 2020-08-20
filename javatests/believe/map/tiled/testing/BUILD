load("//bazel/bzl:rules.bzl", "java_junit5_test", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "FakeElementTest",
    srcs = ["FakeElementTest.kt"],
    friends = ["//java/believe/map/tiled:tiled"],
    deps = [
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)
