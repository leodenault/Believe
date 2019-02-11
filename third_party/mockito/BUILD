licenses(["notice"])

java_library(
    name = "mockito",
    testonly = 1,
    visibility = ["//:believe_test_pkgs"],
    exports = ["@mockito_maven//jar"],
    runtime_deps = [
        "@bytebuddy_agent_maven//jar",
        "@bytebuddy_maven//jar",
        "@objenesis_maven//jar",
    ],
)
