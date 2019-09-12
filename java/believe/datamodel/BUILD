package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "datamodel",
    srcs = [
        "DataCommitter.java",
        "MutableData.java",
        "MutableDataCommitter.java",
    ],
)

java_library(
    name = "impl",
    srcs = ["MutableValue.java"],
    deps = [":datamodel"],
)
