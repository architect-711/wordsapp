FROM eclipse-temurin:23.0.2_7-jre-ubi9-minimal

WORKDIR /app

ARG build
ENV BUILD=${build} 

COPY ${build} ${build}

ENTRYPOINT [ "sh", "-c", "java -jar /app/${BUILD}" ]