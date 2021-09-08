#!/usr/bin/python

from pathlib import Path
import subprocess
import sys

if len(sys.argv) < 2:
    print(
        "Please provide the base revision number from which to start linting.")
    exit(1)

out_file = "lint.out"
base_rev = sys.argv[1]
fix_errors = ""
if len(sys.argv) > 2 and sys.argv[2] == "-f":
    fix_errors = "-f"

subprocess.run(
    "git -c sequence.editor=: rebase -i -q {base_rev} --exec \"dev/lint.py {fix_errors} -o={out_file}\"".format(
        base_rev=base_rev,
        fix_errors=fix_errors,
        out_file=out_file
    ),
    shell=True
)

out_file_path = Path(out_file)
if out_file_path.is_file():
    out_file_path.unlink()
    print("There were lint errors. Please fix them.")
    exit(1)
