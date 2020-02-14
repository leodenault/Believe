licenses(["notice"])

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "junit",
    exports = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_junit_platform_junit_platform_commons",
        "@maven//:org_junit_platform_junit_platform_console",
    ],
    runtime_deps = [
        "@maven//:junit_junit",
        "@maven//:org_junit_jupiter_junit_jupiter_engine",
    ],
)
