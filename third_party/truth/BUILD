licenses(["notice"])

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "truth",
    exports = [
        "@google_guava//jar",
        "@google_truth//jar",
    ],
    runtime_deps = [
        "@diff_utils//jar",
    ],
)

java_library(
    name = "truth8",
    exports = [
        "@google_guava//jar",
        "@google_truth//jar",
        "@google_truth_java8//jar",
    ],
    runtime_deps = [
        "@diff_utils//jar",
    ],
)
