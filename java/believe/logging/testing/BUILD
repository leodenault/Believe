package(
    default_testonly = 1,
    default_visibility = [
        "//:believe_test_pkgs",
        "//java/believe/logging/truth:__subpackages__",
    ],
)

java_library(
    name = "testing",
    srcs = [
        "LogMessage.java",
        "VerifiableLogSystem.java",
        "VerifiableLogSystemExtension.java",
        "VerifiesLoggingCalls.java",
    ],
    deps = [
        ":package_info",
        "//third_party/auto_value",
        "//third_party/javax:annotations",
        "//third_party/junit",
        "//third_party/slick",
    ],
)

java_library(
    name = "package_info",
    srcs = ["package-info.java"],
    visibility = ["//javatests/believe/logging/testing:__pkg__"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)
