load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(default_visibility = ["//:believe_all_pkgs"])

kt_jvm_library(
    name = "mob",
    srcs = [
        "MobAnimation.kt",
        "MobAnimationDataManager.kt",
        "MobDataManager.kt",
        "StationaryEnemy.kt",
        "StationaryEnemyParser.kt",
    ],
    data = ["//java/believe/mob/data"],
    deps = [
        ":annotations",
        "//java/believe/character",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/mob/proto:mob_animation_data_java_proto",
        "//java/believe/physics/damage",
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["MobDaggerModule.java"],
    deps = [
        ":annotations",
        ":mob",
        "//java/believe/datamodel",
        "//java/believe/map/io",
        "//java/believe/mob/proto:mob_animation_data_java_proto",
        "//third_party/dagger",
    ],
)

kt_jvm_library(
    name = "annotations",
    srcs = ["Annotations.kt"],
    deps = ["//third_party/javax:annotations"],
)
