load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "AudioTest",
    srcs = ["AudioTest.kt"],
    associates = ["//java/believe/audio"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)
