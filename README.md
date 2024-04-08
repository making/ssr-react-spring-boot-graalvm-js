# Server Side Rendering example with React + React Router + Vite + GraalVM (JS) + Spring Boot

> [!NOTE]
> The following assumes the use of GraalVM 21, but other than the creation of a native image, it should work with a different JDK (21) as well.

Please execute the following command to bundle the necessary JavaScript resources before running the application in an IDE.

```
./mvnw compile
```

## How to run the local server

```
./mvnw spring-javaformat:apply spring-boot:run
```

```
curl -s http://localhost:8080/posts/1
```

## How to run the UI only

```
cd ui
npm run dev
```

To develop the UI, you need to have the backend server (localhost:8080) running.

## How to create an executable jar file

```
./mvnw package -DskipTests
```

```
java -jar target/ssr-react-spring-boot-graalvm-js-0.0.1-SNAPSHOT.jar 
```

```
curl -s http://localhost:8080/posts/1
```

## How to create a native image

Make sure you `JAVE_HOME` points to GraalVM 21

```
./mvnw -Pnative native:compile -DskipTests
```

```
./target/ssr-react-spring-boot-graalvm-js
```

```
curl -s http://localhost:8080/posts/1
```