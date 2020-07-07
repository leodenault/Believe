load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "GraphicsTest",
    srcs = ["GraphicsTest.kt"],
    friends = [],
    deps = [
        "//java/believe/core/display:display_v2",
        "//java/believe/geometry",
        "//java/believe/gui/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)
