load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "KeyboardInputAdapterTest",
    srcs = ["KeyboardInputAdapterTest.kt"],
    deps = [
        "//java/believe/input",
        "//java/believe/input/keyboard",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)
