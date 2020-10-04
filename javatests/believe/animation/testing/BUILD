load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "AnimationFakesTest",
    srcs = ["AnimationFakesTest.kt"],
    data = ["//third_party/lwjgl:linux_x64"],
    deps = [
        "//java/believe/animation/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TruthTest",
    srcs = ["TruthTest.kt"],
    deps = [
        "//java/believe/animation",
        "//java/believe/animation/proto:animation_java_proto",
        "//java/believe/animation/testing",
        "//java/believe/gui/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)
