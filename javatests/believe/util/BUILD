load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "UtilTest",
    srcs = ["UtilTest.kt"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/util",
        "//third_party/hamcrest",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)
