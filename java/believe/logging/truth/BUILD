package(
    default_testonly = 1,
    default_visibility = ["//:believe_test_pkgs"],
)

java_library(
    name = "truth",
    srcs = [
        "LogMessageListSubject.java",
        "VerifiableLogSystemSubject.java",
    ],
    deps = [
        ":package_info",
        "//java/believe/logging/testing",
        "//third_party/junit",
        "//third_party/truth",
    ],
)

java_library(
    name = "package_info",
    srcs = ["package-info.java"],
    visibility = ["//javatests/believe/logging/truth:__pkg__"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)
