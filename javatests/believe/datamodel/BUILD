load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "MutableValueTest",
    srcs = ["MutableValueTest.java"],
    deps = [
        "//java/believe/datamodel:impl",
        "//third_party/truth",
    ],
)
