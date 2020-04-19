
echo "Starting generating property file"
./gradlew generatePropertyFile
echo "End of generating property file"
echo "Starting building"
./gradlew bootJar
echo "End of building"

echo "Starting releasing"
echo $GITHUB_TOKEN | docker login -u $GITHUB_USER --password-stdin docker.pkg.github.com
docker build -t docker.pkg.github.com/$GITHUB_ORGANIZATION/$GITHUB_REPO/$GITHUB_PACKAGE:$RELEASE_VERSION .
docker push docker.pkg.github.com/$GITHUB_ORGANIZATION/$GITHUB_REPO/$GITHUB_PACKAGE:$RELEASE_VERSION
echo "End of releasing"
