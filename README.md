# Server Side Rendering example with React + React Router + Vite + GraalVM (JS) + Spring Boot

This project demonstrates server-side rendering (SSR) using React with Spring Boot and GraalVM's JavaScript engine. The approach allows for faster page loads and improved SEO compared to client-side rendering.

Demo: https://demo-blog.ik.am

> [!NOTE]
> This implementation assumes the use of GraalVM 21, however, except for native image creation, it should work with other JDK 21 implementations as well.

Before running the application in your IDE, please execute the following command to bundle the required JavaScript resources:

```
./mvnw compile
```

## Running the local server

Start the application with proper code formatting applied:

```
./mvnw spring-javaformat:apply spring-boot:run
```

Once the application is running, you can access it in your browser at:

http://localhost:8080

<img width="1024" alt="Image" src="https://github.com/user-attachments/assets/854990cb-c1e5-4e75-b3f4-aba3cb3ce1b0" />

You can also test the server-side rendering by making a request to one of the routes:

```
curl -s http://localhost:8080/posts/1
```

This will return the fully rendered HTML for the requested route.

## UI development only

For UI development with hot-reloading capabilities, navigate to the UI directory and start the Vite development server:

```
cd ui
npm run dev
```

Note that for UI development, you need to have the backend server (localhost:8080) running. The UI development server will proxy API requests to the backend.

## Creating an executable jar file

Build an executable JAR file with the following command:

```
./mvnw package -DskipTests
```

Run the packaged application:

```
java -jar target/ssr-react-spring-boot-graalvm-js-0.0.1-SNAPSHOT.jar 
```

Test that the application is working correctly:

```
curl -s http://localhost:8080/posts/1
```

## Native image creation

Native images provide faster startup and reduced memory usage. To build a native image, ensure your `JAVA_HOME` points to GraalVM 21:

```
./mvnw -Pnative native:compile -DskipTests
```

Run the compiled native executable:

```
./target/ssr-react-spring-boot-graalvm-js --management.otlp.tracing.endpoint=https://httpbin.org/post
```

Verify the application is working:

```
curl -s http://localhost:8080/posts/1
```

You can also create a docker image as follows:

```
./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=ghcr.io/making/ssr-react-spring-boot-graalvm-js:native -Pnative
```

Run the docker image:

```
docker run --name demo --rm ghcr.io/making/ssr-react-spring-boot-graalvm-js:native --management.otlp.tracing.endpoint=https://httpbin.org/post
```

This containerized approach ensures consistent runtime environments across different deployment platforms.