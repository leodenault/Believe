load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "InputAdapterImplTest",
    srcs = ["InputAdapterImplTest.kt"],
    associates = ["//java/believe/input:impl"],
    deps = [
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)
