# document-sign

## Descripción
Proyecto de integración desarrollado con Apache Camel que implementa una solución para el procesamiento de archivos desde un servidor SFTP hacia una cola JMS en Apache ActiveMQ Artemis, complementado con persistencia en base de datos y procesamiento mediante un servicio HTTP externo.

## Requisitos
- Java 11+
- Apache Maven 3.6+
- Docker
- Spring boot 3.14.+
- Camel 3.14.+

## Instalación
1. Descomprimir el proyecto
2. Ejecutar: 
```console
mvn clean install
```
3. Abrir la carpeta docker: cd docker
```console
cd docker
```
4. Levantar los contenedores: docker compose up -d
```console
docker compose up -d
```
5. Regresar a la raiz: cd ..
```console
cd ..
```
6. Levantar el proyecto camel: mvn spring-boot:run
```console
mvn spring-boot:run
```

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