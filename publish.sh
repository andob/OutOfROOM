set -o allexport

echo "Publishing..."

./gradlew :common-ddl:publishToMavenLocal
./gradlew :common-dml:publishToMavenLocal
./gradlew :common-query-builder:publishToMavenLocal
./gradlew :processor:publishToMavenLocal
./gradlew :binding-jdbc:publishToMavenLocal
./gradlew :binding-latest-sqlite:publishToMavenLocal
./gradlew :binding-system-sqlite:publishToMavenLocal
