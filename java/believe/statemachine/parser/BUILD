load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "parser",
    srcs = [
        "ConcurrentStateMachineParser.kt",
        "StateMachineParser.kt",
        "SubStateMachineConfiguration.kt",
    ],
    deps = [
        "//java/believe/statemachine:impl",
        "//java/believe/statemachine:statemachine_v2",
        "//java/believe/statemachine/proto:state_machine_java_proto",
        "//third_party/slick",
    ],
)
