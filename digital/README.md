# E-Commerce Gateway

Punto de partida para el backend de una API agregadora de datos de producto.

## Stack

- Java 21
- Spring Boot
- Maven

## Estructura

- `config`: configuración de la aplicación y clientes HTTP.
- `controller`: futura capa de entrada REST.
- `client/catalog`, `client/inventory`, `client/exchange`: adaptadores para servicios propios y de terceros.
- `dto/request` y `dto/response`: contratos de entrada y salida.
- `service`: futura orquestación de la API compuesta.
- `exception`: manejo centralizado de errores.

La estructura está preparada, sin endpoints ni lógica de negocio implementados.

## Configuración

Las direcciones de servicios se configuran con propiedades Spring o variables de entorno:

- `CATALOG_SERVICE_URL` (por defecto `http://localhost:8081`)
- `INVENTORY_SERVICE_URL` (por defecto `http://localhost:8082`)
- `EXCHANGE_SERVICE_URL` (por defecto `https://api.frankfurter.app`)
- `EXCHANGE_BASE_CURRENCY` (por defecto `USD`)
- `EXCHANGE_TARGET_CURRENCY` (por defecto `MXN`)
- `SERVER_PORT` (por defecto `8080`)

## Ejecutar

En Windows: `mvnw.cmd spring-boot:run`

En Linux/macOS: `./mvnw spring-boot:run`
