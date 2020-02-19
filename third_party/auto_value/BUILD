licenses(["notice"])

java_plugin(
    name = "auto_value_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["@maven//:com_google_auto_value_auto_value"],
)

java_library(
    name = "auto_value",
    exported_plugins = [":auto_value_plugin"],
    visibility = ["//:believe_all_pkgs"],
    exports = [
        "@maven//:com_google_auto_value_auto_value",
        "@maven//:com_google_auto_value_auto_value_annotations",
    ],
)
