"""Retain every original entry except the two patched classes; add new classes."""
import pathlib
import sys
import zipfile

original, verified, destination = map(pathlib.Path, sys.argv[1:])
destination.parent.mkdir(parents=True, exist_ok=True)
patched = {"zxnsayePWpajv.class", "PyYQMyyC.class", "ceMGdFNBJnq.class", "NBAFgUXWGCkgPq.class", "yRvgxykm.class", "LZitKUUsicXFt.class"}
with zipfile.ZipFile(original) as src, zipfile.ZipFile(verified) as changed:
    if "AutoLevelHook.class" in src.namelist(): patched.update({"SJSCaeScqbiua.class", "AutoLevelHook.class"})
    additions = {n for n in changed.namelist() if n.startswith("AutoPet") and n.endswith(".class")}
    assert additions == {"AutoPetAccess.class", "AutoPetBridge.class", "AutoPetUI.class",
                         "AutoPetConfig.class", "AutoPetEngine.class",
                         "AutoPetEngine$Port.class", "AutoPetEngine$Item.class",
                         "AutoPetRules.class", "AutoPetRuntime.class", "AutoPetLevel.class", "AutoPetHunt.class"}, additions
    with zipfile.ZipFile(destination, "w", zipfile.ZIP_DEFLATED) as out:
        for entry in src.infolist():
            data = changed.read(entry.filename) if entry.filename in patched else src.read(entry.filename)
            out.writestr(entry, data)
        for name in sorted(additions):
            out.writestr(name, changed.read(name))
        out.writestr("AUTOPETLC-STATUS.txt", "TASHINO TEST BUILD. NPC/shop adapter is connected. Live server behavior has NOT been validated. See project README.md.\n")
with zipfile.ZipFile(original) as src, zipfile.ZipFile(destination) as result:
    for name in src.namelist():
        if name not in patched:
            assert src.read(name) == result.read(name), name
    assert result.testzip() is None
print("Verified: original resources and all unpatched classes are byte-identical")
