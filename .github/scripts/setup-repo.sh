#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd $SCRIPT_DIR/../..

COMMIT=$(git rev-parse --short HEAD)
BRANCH=$(git rev-parse --abbrev-ref HEAD)

echo "$BRANCH" > src/main/resources/staticVars/BRANCH
echo "$COMMIT" > src/main/resources/staticVars/COMMIT

if [ -n "$VERSION" ]; then
  sed -i "s/dev-snapshot/dev-snapshot-$VERSION/" build.gradle.kts
  echo "$VERSION" > src/main/resources/staticVars/VERSION
else
  sed -i "s/dev-snapshot/dev-snapshot-$COMMIT/" build.gradle.kts
  echo "dev-snapshot-$COMMIT" > src/main/resources/staticVars/VERSION
fi
