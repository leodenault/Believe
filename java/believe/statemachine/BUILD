load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "statemachine",
    srcs = [
        "ConcurrentStateMachine.java",
        "EntityStateMachine.java",
        "State.java",
        "Transition.java",
    ],
)

kt_jvm_library(
    name = "statemachine_v2",
    srcs = [
        "ConcurrentStateMachineV2.kt",
        "MutableStateV2.kt",
        "StateMachineV2.kt",
        "StateV2.kt",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["ConcurrentStateMachineV2Impl.kt"],
    deps = [":statemachine_v2"],
)
