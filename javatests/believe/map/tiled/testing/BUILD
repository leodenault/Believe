load("//bazel/bzl:rules.bzl", "java_junit5_test", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "FakeElementTest",
    srcs = ["FakeElementTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PropertiesTruthTest",
    srcs = ["PropertiesTruthTest.kt"],
    deps = [
        "//java/believe/map/tiled:impl",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)
