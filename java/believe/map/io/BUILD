package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "io",
    srcs = [
        "MapManager.java",
        "MapManagerImpl.java",
        "TileParser.java",
        "TiledMapLayerParser.java",
        "TiledMapLayerParserImpl.java",
        "TiledMapParser.java",
    ],
    deps = [
        ":internal_qualifiers",
        "//java/believe/app/annotation",
        "//java/believe/map/data",
        "//java/believe/map/tiled",
        "//java/believe/physics/manager",
        "//java/believe/xml",
        "//third_party/auto_value",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["MapParsingDaggerModule.java"],
    deps = [
        ":internal_qualifiers",
        ":io",
        "//third_party/dagger",
    ],
)

java_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.java"],
    visibility = ["//visibility:private"],
    deps = ["//third_party/dagger"],
)
