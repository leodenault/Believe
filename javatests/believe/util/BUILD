load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_test(
    name = "UtilTest",
    srcs = ["UtilTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/util",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/slick",
    ],
)
