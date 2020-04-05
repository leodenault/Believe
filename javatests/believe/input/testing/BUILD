load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "FakeInputAdapterTest",
    srcs = ["FakeInputAdapterTest.kt"],
    deps = [
        "//java/believe/input",
        "//java/believe/input/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)
