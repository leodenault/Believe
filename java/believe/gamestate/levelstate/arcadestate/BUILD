package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "arcadestate",
    srcs = ["ArcadeState.java"],
    data = ["//java/believe/gamestate/levelstate/arcadestate/data:dialogue_textproto"],
    deps = [
        ":package-info",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/character:faction",
        "//java/believe/character/playable",
        "//java/believe/core/io",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/gamestate/levelstate",
        "//java/believe/gui",
        "//java/believe/levelFlow/component",
        "//java/believe/levelFlow/parsing",
        "//java/believe/levelFlow/parsing/exceptions",
        "//java/believe/map/data",
        "//java/believe/map/gui",
        "//java/believe/map/io",
        "//java/believe/physics/manager",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ArcadeStateDaggerModule.java"],
    deps = [
        ":package-info",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/map/collidable/command",
        "//third_party/dagger",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)
