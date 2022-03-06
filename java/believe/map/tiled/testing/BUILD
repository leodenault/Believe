load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_testonly = True,
    default_visibility = ["//:believe_test_pkgs"],
)

kt_jvm_library(
    name = "testing",
    srcs = [
        "FakeElement.kt",
        "PropertiesTruth.kt",
        "TiledFakes.kt",
    ],
    deps = [
        "//java/believe/io",
        "//java/believe/io/testing",
        "//java/believe/map/tiled:impl",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)
