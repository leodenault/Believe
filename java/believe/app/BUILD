load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")
load("//bazel/bzl:rules.bzl", "textproto")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

package_group(
    name = "app_pkg",
    packages = ["//java/believe/app/..."],
)

java_library(
    name = "app",
    srcs = [
        "Application.java",
    ],
    deps = [
        ":game_container_supplier",
        ":qualifiers",
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "app_kt",
    srcs = [
        "AppGameContainerFactory.kt",
        "ApplicationV2.kt",
        "EnvironmentConfigurator.kt",
        "Game.kt",
        "StateSelector.kt",
    ],
    deps = [
        ":qualifiers_v2",
        "//java/believe/app/flags",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "game_container_supplier",
    srcs = ["AppGameContainerSupplier.java"],
    deps = [
        "//java/believe/app/flag_parsers:impl",
        "//java/believe/app/flags",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "util",
    srcs = ["AppUtil.java"],
    deps = [
        ":dagger_component",
        ":game_container_supplier",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["ApplicationComponent.java"],
    deps = [
        ":app",
        ":game_container_supplier",
        "//java/believe/audio",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component_v2",
    srcs = ["ApplicationComponentV2.java"],
    deps = [
        ":app_kt",
        ":dagger_module_v2",
        ":qualifiers_v2",
        "//java/believe/animation",
        "//java/believe/animation:dagger_module",
        "//java/believe/app/flag_parsers:impl",
        "//java/believe/app/flag_parsers:qualifiers",
        "//java/believe/app/flags",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/audio",
        "//java/believe/character:character_v2",
        "//java/believe/character:dagger_module",
        "//java/believe/character/playable",
        "//java/believe/character/playable:dagger_module",
        "//java/believe/character/proto:character_animations_java_proto",
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
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//java/believe/gamestate:package-info",
        "//java/believe/gui",
        "//java/believe/gui:dagger_module",
        "//java/believe/gui:dagger_module_v2",
        "//java/believe/gui:gui_v2",
        "//java/believe/gui:package-info",
        "//java/believe/input",
        "//java/believe/input/keyboard",
        "//java/believe/io",
        "//java/believe/io:impl",
        "//java/believe/level",
        "//java/believe/level:dagger_module",
        "//java/believe/level:impl",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:dagger_module",
        "//java/believe/map/collidable/tile",
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
        "//java/believe/react:impl",
        "//java/believe/scene",
        "//third_party/dagger",
        "//third_party/kotlin",
        "//third_party/slick",
        "@com_google_protobuf//java/core",
    ],
)

java_library(
    name = "dagger_module_v2",
    srcs = ["ApplicationDaggerModuleV2.java"],
    deps = [
        ":app_kt",
        ":qualifiers_v2",
        "//java/believe/action",
        "//java/believe/animation:dagger_module",
        "//java/believe/app/flag_parsers:impl",
        "//java/believe/app/flags",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/character:dagger_module",
        "//java/believe/command:dagger_module",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue:dagger_module",
        "//java/believe/gamestate:dagger_module",
        "//java/believe/gamestate:impl",
        "//java/believe/gamestate/levelstate/platformingstate",
        "//java/believe/gamestate/levelstate/platformingstate:qualifiers",
        "//java/believe/gui:dagger_module",
        "//java/believe/gui:dagger_module_v2",
        "//java/believe/io:dagger_module",
        "//java/believe/level:dagger_module",
        "//java/believe/map/collidable/command:dagger_module",
        "//java/believe/map/collidable/tile:dagger_module",
        "//java/believe/map/gui",
        "//java/believe/map/io:dagger_module",
        "//java/believe/map/tiled/command:dagger_module",
        "//java/believe/physics/collision:dagger_module",
        "//java/believe/physics/manager",
        "//java/believe/proto:dagger_module",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ApplicationModule.java"],
    deps = [
        ":app",
        ":game_container_supplier",
        ":qualifiers",
        "//java/believe/action",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/character/playable:dagger_module",
        "//java/believe/command:dagger_module",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue:dagger_module",
        "//java/believe/gamestate:impl",
        "//java/believe/gamestate/levelstate/platformingstate",
        "//java/believe/gamestate/levelstate/platformingstate:qualifiers",
        "//java/believe/gui:dagger_module",
        "//java/believe/io:dagger_module",
        "//java/believe/level:dagger_module",
        "//java/believe/map/collidable/command:dagger_module",
        "//java/believe/map/collidable/tile:dagger_module",
        "//java/believe/map/gui",
        "//java/believe/map/io:dagger_module",
        "//java/believe/map/tiled/command:dagger_module",
        "//java/believe/physics/collision:dagger_module",
        "//java/believe/physics/manager",
        "//java/believe/proto:dagger_module",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "qualifiers",
    srcs = [
        "ApplicationTitle.java",
        "FirstState.java",
        "GameStates.java",
    ],
    deps = ["//third_party/javax:annotations"],
)

kt_jvm_library(
    name = "qualifiers_v2",
    srcs = [
        "ApplicationTitle.java",
        "FirstState.java",
        "GameStates.java",
        "NativeLibraryPaths.kt",
    ],
    deps = ["//third_party/javax:annotations"],
)
