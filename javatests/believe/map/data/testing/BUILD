load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "ObjectFactorySubjectTest",
    srcs = ["ObjectFactorySubjectTest.kt"],
    deps = [
        "//java/believe/map/data/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)
