licenses(["notice"])

java_plugin(
    name = "auto_factory_plugin",
    generates_api = True,
    processor_class = "com.google.auto.factory.processor.AutoFactoryProcessor",
    deps = [
        "@com_google_auto_auto_common//jar",
        "@com_google_errorprone_javac_shaded//jar",
        "@com_google_googlejavaformat_google_java_format//jar",
        "@com_google_guava_guava//jar",
        "@com_squareup_javapoet//jar",
        "@javax_inject_javax_inject//jar",
        "@maven//:com_google_auto_factory_auto_factory",
    ],
)

java_library(
    name = "auto_factory",
    exported_plugins = [":auto_factory_plugin"],
    visibility = ["//:believe_all_pkgs"],
    exports = [
        "//third_party/javax:annotations",
        "@maven//:com_google_auto_factory_auto_factory",
    ],
)
