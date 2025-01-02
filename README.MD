# Servicio de Acortador de URLs

## Descripción

Este proyecto es un servicio de acortador de URLs construido con Java, Spring Boot y Maven. Permite a los usuarios acortar URLs largas y redirigir a las URLs originales utilizando las URLs cortas generadas.

## Características

- Acortar URLs largas
- Redirigir a URLs originales usando URLs cortas
- Almacenar URLs en Cassandra
- Desplegar en Kubernetes

## Endpoints

### POST /shorten-url

**Descripción**: Acorta una URL larga.

**Parámetros**:
- `longUrl` (requerido): La URL larga que se desea acortar.
- `isEnabled` (opcional, por defecto: true): Indica si la URL acortada está habilitada.

**Respuesta**: La URL acortada.

### GET /{short_url}

**Descripción**: Redirige a la URL larga original usando la URL corta.

**Parámetros**:
- `short_url` (ruta): La URL corta.

**Respuesta**: Redirige a la URL larga original.

### PUT /edit-url/{shortUrl}

**Descripción**: Edita una asignación de URL existente.

**Parámetros**:
- `shortUrl` (ruta): La URL corta que se desea editar.
- `urlMapping` (cuerpo): Los nuevos detalles de la asignación de URL.

**Respuesta**: La URL corta actualizada.

### POST /authorize

**Description**: Autoriza a un usuario basado en las credenciales proporcionadas (mockeado como user y password).

**Parámetros**:

- `username` (required): El nombre de usuario del usuario (utilizar: user).
- `password` (required): La contraseña del usuario (utilizar: password).

**Respuesta**: Un objeto JSON que contiene un token de autorización si las credenciales son válidas.


## Configuración

### Cassandra

Asegúrate de que Cassandra esté corriendo y configurado correctamente. Puedes usar el archivo `kubernetes/cassandra.yaml` para desplegar Cassandra en tu clúster de Kubernetes o correr una instancia local en docker  usando el comando `docker run --name cassandra -d cassandra:latest` (asegurarse de usar el host correcto en "applcation.properties", ex: localhost si el docker cassandra corre en la misma máquina).

Para crear el keyspace y la tabla necesaria, ejecuta los siguientes comandos:

**En Kubernetes:**
```
kubectl exec -it cassandra-0 -- cqlsh -e "CREATE KEYSPACE IF NOT EXISTS shorten_urls WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"

kubectl exec -it cassandra-0 -- cqlsh -e "CREATE TABLE IF NOT EXISTS shorten_urls.url_mapping (shortUrl text PRIMARY KEY, longUrl text, userId text, createdDate timestamp, updatedDate timestamp, isEnabled boolean);"
```
**En Docker:**

```
docker exec -it cassandra cqlsh -e "CREATE KEYSPACE IF NOT EXISTS shorten_urls WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"

docker exec -it cassandra cqlsh -e "CREATE TABLE IF NOT EXISTS shorten_urls.url_mapping (shortUrl text PRIMARY KEY, longUrl text, userId text, createdDate timestamp, updatedDate timestamp, isEnabled boolean);"
```
### Redis

Redis se utiliza para el almacenamiento en caché. Puedes usar el archivo `kubernetes/redis.yaml` para desplegar Redis en tu clúster de Kubernetes o correr una instancia local en docker  usando el comando `docker run --name redis -d redis:latest` (asegurarse de usar el host correcto en "applcation.properties", ex: localhost si el docker redis corre en la misma máquina).

## Correr el Servicio (local)

```
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar target/url-shortener-0.0.1-SNAPSHOT.jar
``` 
O configure un run/debug configuration en su IDE.


## Despliegue en Kubernetes(sin funcionar)

Para el despliegue en kubernetes ejecute los siguientes comandos:
```
kubectl apply -f kubernetes/cassandra.yaml
kubectl apply -f kubernetes/redis.yaml
kubectl apply -f kubernetes/url-shortener.yaml
```
En caso de que se haya hecho algun cambio en el codigo fuente, se debe construir una nueva imagen de docker  y subirla a un repositorio de contenedores.

Luego, se debe actualizar el archivo `kubernetes/url-shortener.yaml` con la nueva imagen y ejecutar el comando `kubectl apply -f kubernetes/url-shortener.yaml` para actualizar el despliegue.

