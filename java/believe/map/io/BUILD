package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "io",
    srcs = [
        "BackgroundSceneParser.java",
        "MapManager.java",
        "ObjectParser.java",
        "TileParser.java",
        "TiledMapLayerParser.java",
        "TiledMapObjectLayerParser.java",
        "TiledMapParser.java",
    ],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/tiled",
        "//third_party/slick",
    ],
)

java_library(
    name = "impl",
    srcs = [
        "BackgroundSceneParserImpl.java",
        "MapManagerImpl.java",
        "ObjectParser.java",
        "TiledMapLayerParserImpl.java",
        "TiledMapObjectLayerParserImpl.java",
        "TiledMapParserImpl.java",
    ],
    deps = [
        ":internal_qualifiers",
        ":io",
        "//java/believe/app/annotation",
        "//java/believe/datamodel/protodata",
        "//java/believe/gui",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
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
        ":impl",
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
