"""Fetch pinned build tools through inherited TLS/proxy configuration."""
import hashlib
import json
import pathlib
import urllib.request

root = pathlib.Path(__file__).resolve().parent
for name, spec in json.loads((root / "dependencies.json").read_text()).items():
    target = root / name
    if target.exists() and hashlib.sha256(target.read_bytes()).hexdigest() == spec["sha256"]:
        print("Verified", name)
        continue
    with urllib.request.urlopen(spec["url"], timeout=60) as response:
        data = response.read()
    if hashlib.sha256(data).hexdigest() != spec["sha256"]:
        raise RuntimeError("SHA-256 mismatch: " + name)
    temporary = target.with_suffix(".download")
    temporary.write_bytes(data)
    temporary.replace(target)
    print("Installed and verified", name)
