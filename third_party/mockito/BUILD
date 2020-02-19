licenses(["notice"])

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "mockito",
    exports = ["@maven//:org_mockito_mockito_core"],
)

alias(
    name = "mockito_kotlin",
    actual = "@maven//:com_nhaarman_mockitokotlin2_mockito_kotlin",
)
