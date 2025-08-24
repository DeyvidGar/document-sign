# document-sign

## Descripción

<p>Proyecto de integración desarrollado con Apache Camel.</p>
<p>Implementa una solución para el procesamiento de archivos desde un servidor SFTP hacia una cola JMS en Apache ActiveMQ Artemis, complementado con persistencia en base de datos en Postgres y procesamiento mediante un servicio HTTP externo.</p>

## Architecture

![Architecture diagram.](https://raw.githubusercontent.com/DeyvidGar/assets/master/Sign-Document-component-diagram.png)

## Funcionalidades

- Conexión y monitoreo continuo a servidor SFTP para detección automática de archivos entrantes.
- Validación de nombres de archivos mediante reglas definidas (por ejemplo: formato, extensión, patrones específicos) para asegurar que solo se procesen archivos válidos.
- Registro de metadatos de archivos (nombre, fecha, estado) en base de datos relacional para trazabilidad.
- Invocación a servicio HTTP externo para procesamiento o validación de nombres de archivos.
- Publicación de mensajes en cola JMS de Apache ActiveMQ Artemis para integración asíncrona con otros sistemas.

## Casos de Prueba

- Verificar conexión exitosa y detección de archivos en SFTP.
- Validar que solo archivos con nombres correctos sigan en el flujo, y los inválidos se descarten o manejen como error.
- Confirmar llamadas al servicio HTTP para procesar nombres y manejo correcto de respuestas y errores.
- Comprobar registro correcto de datos de archivos procesados en la base de datos.
- Validar publicación correcta de mensajes en la cola JMS de ActiveMQ Artemis.
- Simular errores para asegurar reintentos y manejo adecuado de mensajes fallidos.

## Requisitos

- Java 17+
- Apache Maven 3.6+
- Spring boot 3.14.+
- Camel 3.14.+
- Docker
- PostgreSql
- Apache ActiveMQ

## What run the application?

<p>We can run the application in console following the next steps:</p>

<p>First ensure the path variable in JAVA_HOME should be 17.</p>

1. To find all paths environment variables.

   - In GitBash, execute the command:
       ```console
       env
       ```
   - In Command prompt, execute the command:
       ```console
       set
       ```

2. Of only get the JAVA_HOME value.
   - In GitBash, execute the command:
       ```console
       echo $JAVA_HOME
       ```
   - In Command prompt, execute the command:
       ```console
       echo JAVA_HOME
       ```

3. Now set the route of your jdk-17.+
   - In GitBash, execute the command:
       ```console
       export JAVA_HOME='/c/Program Files/Java/jdk-17'
       ```
   - In Command prompt, execute the command:
       ```console
       set JAVA_HOME="C:\Program Files\Java\jdk-17"
       ```

4. uncompress or download the project.

5. With docker, open the folder _docker_:
```console
cd docker
```

6. Execute the next command to up all containers in docker-compose.yml:
```console
docker compose up -d
```

7. In the same level of pom.xml execute the next command to ensure that the correct running.
```console
mvn clean install
```

8. Finally execute the command to start spring boot application.
```console
mvn spring-boot:run
```

### For Intellij IDE

<p>We can run the application in intellij following the next steps:</p>

1. Find the main class in com.midominio.camel.documentsign.MySpringBootApplication.java

2. Right click and Run 'MySpringBootApplication.main()'

### For Eclipse or STS IDE

<p>We can run the application in Eclipse following the next steps:</p>

> 1. Find in the package explorer this proyect

> 2. Right click and Run as -> Spring boot application.