package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "playable",
    srcs = [
        "DamageProjection.java",
        "EnemyCharacter.java",
        "InternalQualifiers.java",
        "PlayableCharacter.java",
        "PlayableCharacterMovementCommandCollisionHandler.java",
    ],
    deps = [
        ":package-info",
        "//java/believe/character",
        "//java/believe/character:faction",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/map/collidable/command",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/physics/collision",
        "//java/believe/physics/damage",
        "//java/believe/physics/gravity",
        "//java/believe/physics/manager",
        "//java/believe/statemachine",
        "//java/believe/util",
        "//third_party/auto_factory",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["PlayableDaggerModule.java"],
    deps = [
        ":package-info",
        ":playable",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/tile",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/physics/collision",
        "//java/believe/physics/damage",
        "//java/believe/statemachine",
        "//java/believe/util",
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
