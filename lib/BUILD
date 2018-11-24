java_import(
    name = "lib",
    jars = glob(["*.jar"]),
    visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "mockito",
    visibility = ["//:believe_test_pkgs"],
    exports = ["@mockito_maven//jar"],
    runtime_deps = [
        "@bytebuddy_agent_maven//jar",
        "@bytebuddy_maven//jar",
        "@objenesis_maven//jar",
    ],
)

java_library(
    name = "javax_annotations",
    visibility = ["//src/believe:believe_pkg"],
    exports = ["@javax_annotations_maven//jar"],
)

java_library(
    name = "hamcrest",
    visibility = ["//:believe_test_pkgs"],
    exports = ["@hamcrest_maven//jar"],
)

java_library(
    name = "junit",
    visibility = ["//:believe_test_pkgs"],
    exports = ["@junit_maven//jar"],
)
