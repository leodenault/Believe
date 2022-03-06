load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "GameTest",
    srcs = ["GameTest.kt"],
    associates = ["//java/believe/app:app_kt"],
    deps = [
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)
