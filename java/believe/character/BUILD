load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

package_group(
    name = "character_pkg",
    packages = ["//java/believe/character/..."],
)

java_library(
    name = "character",
    srcs = ["Character.java"],
    deps = [
        ":faction",
        "//java/believe/character/proto:playable_character_movement_command_java_proto",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/gui",
        "//java/believe/map/collidable/tile",
        "//java/believe/physics/collision",
        "//java/believe/physics/damage",
        "//java/believe/physics/manager",
        "//java/believe/statemachine",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "character_v2",
    srcs = [
        "Animations.kt",
        "BidirectionalAnimation.kt",
        "CharacterKeyboardActionMap.kt",
        "CharacterMovementInputAction.kt",
        "CharacterStateMachine.kt",
        "CharacterV2.kt",
        "HorizontalMovementStateMachine.kt",
        "InputAction.kt",
        "MovementStateMachine.kt",
        "OrientationStateMachine.kt",
        "VerticalMovementHandler.kt",
    ],
    deps = [
        "//java/believe/character/proto:character_animations_java_proto",
        "//java/believe/core/display:display_v2",
        "//java/believe/datamodel",
        "//java/believe/input",
        "//java/believe/map/collidable/tile",
        "//java/believe/physics/collision",
        "//java/believe/physics/damage",
        "//java/believe/physics/manager",
        "//java/believe/scene",
        "//java/believe/statemachine:statemachine_v2",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CharacterDaggerModule.java"],
    deps = [
        ":character_v2",
        "//java/believe/input",
        "//java/believe/input/keyboard",
        "//third_party/dagger",
    ],
)

java_library(
    name = "faction",
    srcs = ["Faction.java"],
)
