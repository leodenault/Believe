load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "GameStateRunnerImplTest",
    srcs = ["GameStateRunnerImplTest.kt"],
    friends = [],
    deps = [
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//java/believe/gamestate/transition",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
    ],
)
