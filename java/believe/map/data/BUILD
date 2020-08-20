package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "data",
    srcs = [
        "BackgroundSceneData.java",
        "EntityType.java",
        "GeneratedMapEntityData.java",
        "LayerData.java",
        "MapData.java",
        "ObjectLayerData.java",
        "TileData.java",
        "TiledMapData.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//java/believe/core",
        "//java/believe/core:core_kt",
        "//java/believe/core/display",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled:impl",
        "//java/believe/physics/manager",
        "//java/believe/scene",
        "//third_party/auto_value",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)
