licenses(["notice"])

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "mockito",
    exports = ["@mockito_maven//jar"],
    runtime_deps = [
        "@bytebuddy_agent_maven//jar",
        "@bytebuddy_maven//jar",
        "@objenesis_maven//jar",
    ],
)

alias(
    name = "mockito_kotlin",
    actual = "@maven//:com_nhaarman_mockitokotlin2_mockito_kotlin",
)
