load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = [
        "//:__pkg__",
        "//javatests/believe:believe_tests",
    ],
)

java_library(
    name = "game",
    srcs = ["Believe.java"],
    deps = [
        ":dagger_module",
        ":package-info",
        "//java/believe/app",
        "//java/believe/app:dagger_module",
        "//java/believe/app:util",
        "//java/believe/app/game:dagger_component",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["BelieveComponent.java"],
    visibility = ["//visibility:private"],
    deps = [
        ":dagger_module",
        ":package-info",
        "//java/believe/action",
        "//java/believe/app",
        "//java/believe/app:dagger_component",
        "//java/believe/app:dagger_module",
        "//java/believe/app:game_container_supplier",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/audio",
        "//java/believe/character/playable",
        "//java/believe/character/playable:dagger_module",
        "//java/believe/character/playable:package-info",
        "//java/believe/command",
        "//java/believe/command:dagger_module",
        "//java/believe/command:impl",
        "//java/believe/command/proto:command_java_proto",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue",
        "//java/believe/dialogue:dagger_module",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/gamestate",
        "//java/believe/gamestate:impl",
        "//java/believe/gamestate:package-info",
        "//java/believe/gamestate/levelstate/arcadestate",
        "//java/believe/gamestate/levelstate/platformingstate",
        "//java/believe/gamestate/levelstate/platformingstate:package-info",
        "//java/believe/gamestate/temporarystate:impl",
        "//java/believe/gamestate/temporarystate:package-info",
        "//java/believe/gui",
        "//java/believe/gui:dagger_module",
        "//java/believe/io",
        "//java/believe/io:impl",
        "//java/believe/level",
        "//java/believe/level:dagger_module",
        "//java/believe/level:impl",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:dagger_module",
        "//java/believe/map/collidable/tile",
        "//java/believe/map/gui",
        "//java/believe/map/io",
        "//java/believe/map/io:dagger_module",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/command:dagger_module",
        "//java/believe/map/tiled/command:impl",
        "//java/believe/physics/collision",
        "//java/believe/physics/collision:impl",
        "//java/believe/physics/damage",
        "//java/believe/physics/manager",
        "//java/believe/proto",
        "//java/believe/proto:dagger_module",
        "//java/believe/proto:impl",
        "//java/believe/react",
        "//java/believe/react:impl",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["BelieveGameModule.java"],
    visibility = ["//:believe_all_pkgs"],
    deps = [
        ":package-info",
        "//java/believe/action",
        "//java/believe/app",
        "//java/believe/app:dagger_module",
        "//java/believe/app:qualifiers",
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/gamestate:impl",
        "//java/believe/gamestate/levelstate/arcadestate",
        "//java/believe/gamestate/levelstate/platformingstate",
        "//java/believe/gamestate/temporarystate:impl",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "game_v2",
    srcs = ["BelieveV2.kt"],
    deps = [
        "//java/believe/app:dagger_component_v2",
    ],
)

java_library(
    name = "dagger_module_v2",
    srcs = ["BelieveDaggerModuleV2.java"],
    deps = [
        "//java/believe/app:app_kt",
        "//java/believe/app:qualifiers_v2",
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
