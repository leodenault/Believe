load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "tiled",
    srcs = [
        "TiledMap.kt",
    ],
    deps = [
        ":dagger_component",
        ":impl",
        "//java/believe/io",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = [
        "ElementParser.kt",
        "Layer.kt",
        "ObjectTemplateManager.kt",
        "PartialTiledObject.kt",
        "Properties.kt",
        "Tile.kt",
        "TileSet.kt",
        "TileSetGroup.kt",
        "TiledObject.kt",
        "TiledObjectGroup.kt",
    ],
    deps = [
        ":annotations",
        "//java/believe/core:core_kt",
        "//java/believe/datamodel",
        "//java/believe/io",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/kotlin",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "dagger_module",
    srcs = ["TiledDaggerModule.kt"],
    deps = [
        ":impl",
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_component",
    srcs = [
        "TileSetParserComponent.java",
        "TiledParserComponent.java",
    ],
    deps = [
        ":annotations",
        ":dagger_module",
        ":impl",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/io",
        "//java/believe/io:dagger_module",
        "//java/believe/io:impl",
        "//third_party/dagger",
        "//third_party/kotlin",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "annotations",
    srcs = ["Annotations.kt"],
    visibility = ["//visibility:private"],
    deps = ["//third_party/javax:annotations"],
)
