load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "MutableValueTest",
    srcs = ["MutableValueTest.kt"],
    deps = [
        "//java/believe/datamodel:impl",
        "//third_party/truth",
    ],
)
