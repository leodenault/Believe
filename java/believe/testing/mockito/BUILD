package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "mockito",
    srcs = [
        "InstantiateMocksIn.java",
        "MockitoExtension.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
        "//third_party/junit",
        "//third_party/mockito",
    ],
)
