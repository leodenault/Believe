load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "ValueObserverTest",
    srcs = ["ValueObserverTest.java"],
    deps = [
        "//java/believe/react",
        "//java/believe/react:impl",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "ObservableValueTest",
    srcs = ["ObservableValueTest.java"],
    deps = [
        "//java/believe/react",
        "//java/believe/react:impl",
        "//third_party/javax:annotations",
        "//third_party/truth",
    ],
)
