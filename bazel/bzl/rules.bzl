load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_test")

_NO_ASSOCIATES = ["no_associates"]

def _find_junit5_test_class(srcs, test_class, file_suffix):
    if len(srcs) != 1 or test_class != "":
        return test_class

    package = native.package_name()
    package_start_index = package.find("/") + 1

    # Remove java or javatests top-level directory reference from package path.
    package = package[package_start_index:]
    package = package.replace("/", ".")

    inferred_test_class = package + "." + srcs[0]
    inferred_test_class = inferred_test_class[:-len(file_suffix)]
    return inferred_test_class

def java_junit5_test(name = "", srcs = [], data = [], test_class = "", deps = []):
    computed_test_class = _find_junit5_test_class(srcs, test_class, ".java")

    native.java_test(
        name = name,
        srcs = srcs,
        args = [
            "--select-class",
            computed_test_class,
        ],
        data = data,
        main_class = "org.junit.platform.console.ConsoleLauncher",
        use_testrunner = False,
        deps = deps + ["//third_party/junit"],
    )

def kt_junit5_test(
        name = "",
        srcs = [],
        data = [],
        associates = [],
        test_class = "",
        deps = []):
    computed_test_class = _find_junit5_test_class(srcs, test_class, ".kt")

    kt_jvm_test(
        name = name,
        srcs = srcs,
        associates = associates,
        args = [
            "--select-class",
            computed_test_class,
        ],
        data = data,
        main_class = "org.junit.platform.console.ConsoleLauncher",
        deps = deps + ["//third_party/junit"],
    )

def textproto(name = "", srcs = [], java_outer_class_name = "", proto_message = "", deps = []):
    """Defines a set of protos written as textprotos and converts them to binary at build time.
    """

    outs = [(val[:val.rfind(".")] + ".pb") for val in srcs]
    proto_class = java_outer_class_name + "\\$$" + proto_message
    src_args = " ".join(["$(location " + src + ")" for src in srcs])
    binary_name = name + "_proto_file_serializer"

    native.java_binary(
        name = binary_name,
        main_class = "believe.tools.ProtoFileSerializerRunner",
        runtime_deps = ["//java/believe/tools:runner"] + deps,
    )

    cmd = "$(location :{binary_name}) {proto_class} $(@D) {src_args}".format(
        binary_name = binary_name,
        proto_class = proto_class,
        src_args = src_args,
    )

    native.genrule(
        name = name,
        srcs = srcs,
        outs = outs,
        cmd = cmd,
        tools = [":" + binary_name],
    )

def believe_binary(
        base_name = "",
        native_configs = {},
        data = [],
        main_class = "",
        resources = [],
        runtime_deps = []):
    for config_name, config_values in native_configs.items():
        fix_openal_rule_name = "fix_openal_" + config_name
        java_binary_rule_name = "Believe_" + config_name
        packaging_rule_name = "Believe_pkg_" + config_name

        native.genrule(
            name = fix_openal_rule_name,
            srcs = ["//third_party/openal:" + config_name],
            outs = [config_values["openal_file_name"]],
            cmd = "cp $(location //third_party/openal:" + config_name + ") $@",
        )

        native.java_binary(
            name = java_binary_rule_name,
            data = data + config_values["native_targets"] + [":" + fix_openal_rule_name],
            main_class = main_class,
            resources = resources,
            runtime_deps = runtime_deps,
        )

        native.genrule(
            name = packaging_rule_name,
            outs = [packaging_rule_name + ".zip"],
            cmd = """
            mkdir outs
            cp $(location :{binary_rule_name})_deploy.jar outs/Believe.jar
            rsync \
              -r -D \
              --links \
              --exclude=/external \
              --exclude=/java \
              --exclude=*.jar \
              $(location :{binary_rule_name}).runfiles/__main__/ \
              outs
            cd outs
            rm $(rootpath :{binary_rule_name})
            zip -qr contents.zip *
            cd ..
            cp outs/contents.zip $@
            rm -rf outs
            """.format(binary_rule_name = java_binary_rule_name),
            tools = [
                ":" + java_binary_rule_name,
                ":" + java_binary_rule_name + "_deploy.jar",
            ],
        )
