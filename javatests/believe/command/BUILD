load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "CommandGeneratorTest",
    srcs = ["CommandGeneratorTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/core",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/util",
        "//third_party/truth:truth8",
    ],
)
