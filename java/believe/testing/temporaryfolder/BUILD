package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "temporaryfolder",
    srcs = [
        "InstantiableTemporaryFolder.java",
        "TemporaryFolder.java",
        "TemporaryFolderExtension.java",
        "TemporaryFolderImpl.java",
        "UsesTemporaryFolder.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
        "//third_party/junit",
        "//third_party/slick",
    ],
)
