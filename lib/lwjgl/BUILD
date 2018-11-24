package(
    default_visibility = ["//:__pkg__"],
)

java_import(
    name = "lwjgl",
    jars = ["lwjgl.jar"],
    visibility = [
        "//:believe_all_pkgs",
        "//lib/slick:__pkg__",
    ],
)

filegroup(
    name = "windows_x86",
    srcs = ["lwjgl.dll"],
)

filegroup(
    name = "windows_x64",
    srcs = ["lwjgl64.dll"],
)

filegroup(
    name = "linux_x86",
    srcs = ["liblwjgl.so"],
)

filegroup(
    name = "linux_x64",
    srcs = ["liblwjgl64.so"],
)

filegroup(
    name = "mac",
    srcs = ["liblwjgl.dylib"],
)
