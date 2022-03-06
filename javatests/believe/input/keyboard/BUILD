load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "KeyboardInputAdapterTest",
    srcs = ["KeyboardInputAdapterTest.kt"],
    associates = ["//java/believe/input/keyboard"],
    deps = [
        "//java/believe/input",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)
