load("//bazel/bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "CollisionHandlerExecutorImplTest",
    srcs = ["CollisionHandlerExecutorImplTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/physics/collision",
        "//java/believe/physics/collision:impl",
        "//java/believe/physics/collision/testing",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)
