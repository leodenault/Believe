load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "ResourceManagerImplTest",
    srcs = ["ResourceManagerImplTest.kt"],
    associates = ["//java/believe/io:impl"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/temporaryfolder",
        "//third_party/slick",
    ],
)
