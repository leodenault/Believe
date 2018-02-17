java_import(
    name = "lib",
    jars = glob(["*.jar"]),
    visibility = [
        "//:__pkg__",
        "//src/believe:believe_pkg",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "mockito",
    visibility = [
        "//test/believe:believe_tests",
    ],
    exports = ["@mockito_maven//jar"],
    runtime_deps = [
        "@bytebuddy//jar",
        "@bytebuddy_agent//jar",
        "@objenesis//jar",
    ],
)
