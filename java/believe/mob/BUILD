load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(default_visibility = ["//:believe_all_pkgs"])

kt_jvm_library(
    name = "mob",
    srcs = [
        "StationaryEnemy.kt",
        "StationaryEnemyParser.kt",
    ],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["MobDaggerModule.java"],
    deps = [
        ":mob",
        "//java/believe/map/io",
        "//third_party/dagger",
    ],
)
