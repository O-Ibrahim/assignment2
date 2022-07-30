
mvnw:
	./mvnw spring-boot:run

run:
	mvn clean install
fast:
	mvn clean install -DskipTests