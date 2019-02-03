licenses(["notice"])

java_plugin(
    name = "auto_value_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["@google_auto_value//jar"],
)

java_library(
    name = "auto_value",
    exported_plugins = [":auto_value_plugin"],
    visibility = ["//:believe_all_pkgs"],
    exports = [
        "@google_auto_value//jar",
        "@google_auto_value_annotations//jar",
    ],
)
