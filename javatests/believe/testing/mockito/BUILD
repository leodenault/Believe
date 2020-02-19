load("//bazel/bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "MockitoExtensionTest",
    srcs = ["MockitoExtensionTest.java"],
    deps = [
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)
