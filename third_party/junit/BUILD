licenses(["notice"])

java_library(
    name = "junit",
    testonly = 1,
    visibility = [
        "//:believe_all_pkgs",
    ],
    exports = [
        "@junit_jupiter_api//jar",
        "@junit_platform_commons//jar",
        "@junit_platform_console//jar",
    ],
    runtime_deps = [
        "@junit//jar",
        "@junit_jupiter_engine//jar",
        "@junit_platform_engine//jar",
        "@junit_platform_launcher//jar",
        "@opentest4j//jar",
    ],
)
