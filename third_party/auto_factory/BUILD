licenses(["notice"])

java_plugin(
    name = "auto_factory_plugin",
    processor_class = "com.google.auto.factory.processor.AutoFactoryProcessor",
    deps = ["@google_auto_factory//jar"],
)

java_library(
    name = "auto_factory",
    exported_plugins = [":auto_factory_plugin"],
    visibility = ["//:believe_all_pkgs"],
    exports = ["@google_auto_factory//jar"],
)
