#!/usr/bin/python

import subprocess
import sys


class Flag:
    def __init__(self, default, processNewValue):
        self.value = default
        self._processNewValue = processNewValue

    def parseValue(self, value):
        self.value = self._processNewValue(value)


def _lint_java(diff_files, fix_errors):
    diff_invocation = "git --no-pager diff HEAD^ HEAD {diff_files}".format(
        diff_files=" ".join(diff_files)
    )
    gjf_invocation = "dev/bin/google-java-format-diff.py -p1"
    if fix_errors:
        gjf_invocation += " -i"
    gjf_invocation += " --google-java-format-jar dev/bin/google-java-format-1.11.0-all-deps.jar"
    p = subprocess.run(diff_invocation + " | " + gjf_invocation, shell=True,
                         capture_output=True)
    output = p.stdout.decode("unicode_escape")
    if output != "":
        print(output)
        return 1

    return 0


def _lint_kotlin(diff_files, fix_errors):
    args = ["dev/bin/ktlint"]
    if fix_errors:
        args.append("-F")

    return subprocess.run(
        args + diff_files, capture_output=fix_errors
    ).returncode


LINTERS_BY_FILE_EXTENSION = {
    ".java": _lint_java,
    ".kt": _lint_kotlin
}

ARG_PARSERS = {
    "-f": Flag(default=False, processNewValue=lambda value: True),
    "-o": Flag(default=None, processNewValue=lambda value: value)
}

for i in range(1, len(sys.argv)):
    tokens = sys.argv[i].split("=")
    key = tokens[0]
    value = ""
    num_tokens = len(tokens)
    if num_tokens > 2:
        print(
            "Argument '{}' should be single value or key-value pair separated"
            " by '='.".format(tokens))
        exit(1)
    elif num_tokens == 2:
        value = tokens[1]
    if key not in ARG_PARSERS:
        print("Unrecognized argument {}.".format(key))
        exit(1)
    ARG_PARSERS[key].parseValue(value)

fix_errors = ARG_PARSERS["-f"].value
output_file = ARG_PARSERS["-o"].value

diff_result = subprocess.run(
    ["git", "diff", "HEAD^", "HEAD", "--name-only"],
    capture_output=True, text=True
)
diff_files = diff_result.stdout.split("\n")

print()
subprocess.run(
    "git --no-pager log -1 --pretty=\"%C(yellow)%h %C(bold green)%D %C(white)%s%Creset\"",
    shell=True
)

for extension, lint in LINTERS_BY_FILE_EXTENSION.items():
    files_with_extension = [f for f in diff_files if f.endswith(extension)]
    if not files_with_extension:
        continue
    returncode = lint(files_with_extension, fix_errors=fix_errors)
    print("{} -> {}".format(extension, returncode))
    if returncode and output_file:
        with open(output_file, "w") as o:
            o.writelines("")
    if fix_errors:
        print("Fixing lint errors.")
        subprocess.run(
            ["git", "add"] + files_with_extension, stdout=subprocess.DEVNULL
        )
        subprocess.run(
            ["git", "commit", "--amend", "--no-edit"], stdout=subprocess.DEVNULL
        )

print()
