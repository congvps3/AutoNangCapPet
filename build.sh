#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
original=${1:-input/AutoUpLv.jar}
if [[ ! -f "$original" ]]; then
    echo "Usage: bash build.sh /path/to/original/251.jar" >&2
    exit 1
fi
mkdir -p build/classes build/ui-test dist
java -jar tools/ecj.jar -1.3 -encoding UTF-8 \
  -bootclasspath tools/cldc-api.jar:tools/microemu-midp.jar \
  -classpath "$original" -d build/classes src/*.java
java -cp build/classes tests/EngineTest.java
java -cp "build/classes:$original" tests/MapRulesTest.java
java -jar tools/ecj.jar -1.3 -encoding UTF-8 \
  -bootclasspath tools/cldc-api.jar:tools/microemu-midp.jar \
  -classpath build/classes -d build/ui-test tests/UiSmokeMidlet.java
java -Djava.awt.headless=true -cp tools/microemulator.jar org.microemu.app.Headless \
  --rms memory --quit --appclasspath build/classes:build/ui-test UiSmokeMidlet > build/ui-test.log 2>&1
cat build/ui-test.log
if ! rg -q 'UI_SMOKE_PASS 5' build/ui-test.log || rg -q 'UI_SMOKE_FAIL' build/ui-test.log; then
    echo 'UI smoke test failed' >&2
    exit 1
fi
java -cp tools/asm.jar tools/Patch.java "$original" build/classes build/patched.jar
java -jar tools/proguard.jar -injars build/patched.jar -outjars build/preverified.jar \
  -libraryjars tools/cldc-api.jar -libraryjars tools/microemu-midp.jar \
  -libraryjars tools/microemu-wma.jar -dontshrink -dontoptimize -dontobfuscate -microedition -dontnote
python tools/assemble.py "$original" build/preverified.jar dist/AutoUpLv-AutoPetLC-r18.jar
java -Xverify:all -cp tools/microemulator.jar:tools/microemu-wma.jar:dist/AutoUpLv-AutoPetLC-r18.jar \
  tests/VerifyJar.java dist/AutoUpLv-AutoPetLC-r18.jar
java -cp tools/asm.jar tests/MockTransport.java dist/AutoUpLv-AutoPetLC-r18.jar build/runtime-fixture.jar
java -jar tools/ecj.jar -1.8 -encoding UTF-8 \
  -classpath tools/microemulator.jar:build/runtime-fixture.jar \
  -d build/ui-test tests/TestTransport.java tests/RuntimeSmokeMidlet.java
java -Djava.awt.headless=true -cp tools/microemulator.jar org.microemu.app.Headless \
  --rms memory --quit --appclasspath build/runtime-fixture.jar:build/ui-test RuntimeSmokeMidlet > build/runtime-test.log 2>&1
if ! rg -q 'RUNTIME_SMOKE_PASS' build/runtime-test.log || rg -q 'RUNTIME_SMOKE_FAIL' build/runtime-test.log; then
    cat build/runtime-test.log
    exit 1
fi
rg 'RUNTIME_SMOKE_PASS' build/runtime-test.log
java -cp tools/asm.jar tests/MockTransport.java dist/AutoUpLv-AutoPetLC-r18.jar build/wire-fixture.jar wire
java -jar tools/ecj.jar -1.8 -encoding UTF-8 \
  -classpath tools/microemulator.jar:build/wire-fixture.jar \
  -d build/ui-test tests/TestTransport.java tests/RuntimeSmokeMidlet.java
java -Djava.awt.headless=true -cp tools/microemulator.jar org.microemu.app.Headless \
  --rms memory --quit --appclasspath build/wire-fixture.jar:build/ui-test RuntimeSmokeMidlet > build/wire-test.log 2>&1
if ! rg -q 'RUNTIME_SMOKE_PASS' build/wire-test.log || rg -q 'RUNTIME_SMOKE_FAIL' build/wire-test.log; then
    cat build/wire-test.log
    exit 1
fi
rg 'RUNTIME_SMOKE_PASS' build/wire-test.log
