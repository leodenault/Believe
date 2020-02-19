load("//bazel/bzl:rules.bzl", "java_junit5_test", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "KotlinHelpersTest",
    srcs = ["KotlinHelpersTest.kt"],
    friends = ["//java/believe/util:kt"],
    deps = [
        "//java/believe/util:kt",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
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
