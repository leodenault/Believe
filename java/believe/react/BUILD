package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "react",
    srcs = [
        "Observable.java",
        "Observer.java",
    ],
)

java_library(
    name = "impl",
    srcs = [
        "ObservableValue.java",
        "ValueObserver.java",
    ],
    deps = [":react"],
)
