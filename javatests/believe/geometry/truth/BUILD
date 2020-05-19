load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "GeometryTruthTest",
    srcs = ["GeometryTruthTest.kt"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/geometry/truth",
        "//third_party/truth",
    ],
)
