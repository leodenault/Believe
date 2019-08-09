package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "tile",
    srcs = [
        "CollidableTile.java",
        "CollidableTileCollisionHandler.java",
        "CollidableTileGenerator.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//java/believe/geometry",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/physics/collision",
        "//java/believe/physics/gravity",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/auto_value",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CollidableTileDaggerModule.java"],
    deps = [
        ":tile",
        "//java/believe/map/io",
        "//third_party/dagger",
    ],
)
