set -o allexport

echo "Type Archiva username:"
read -r MAVEN_PUBLISH_USERNAME
echo "Type Archiva password:"
read -s -r MAVEN_PUBLISH_PASSWORD

echo "Publishing..."

./gradlew :common-ddl:publish
./gradlew :common-dml:publish
./gradlew :common-query-builder:publish
./gradlew :processor:publish
./gradlew :binding-jdbc:publish
./gradlew :binding-latest-sqlite:publish
./gradlew :binding-system-sqlite:publish

set +o allexport

read -s -r TEMP
