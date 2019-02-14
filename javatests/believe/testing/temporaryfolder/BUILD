load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "TemporaryFolderExtensionTest",
    srcs = ["TemporaryFolderExtensionTest.java"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/mockito",
        "//java/believe/testing/temporaryfolder",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "TemporaryFolderManagerImplTest",
    srcs = ["TemporaryFolderManagerImplTest.java"],
    deps = [
        "//java/believe/testing/temporaryfolder",
        "//third_party/junit",
        "//third_party/truth",
    ],
)
