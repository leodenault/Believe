load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "EntityTypeTest",
    srcs = ["EntityTypeTest.kt"],
    friends = [],
    deps = [
        "//java/believe/map/data",
        "//third_party/truth",
    ],
)
