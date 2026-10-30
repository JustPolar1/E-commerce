# E-Commerce Gateway

Backend de una API agregadora de datos de producto. El frontend consulta un único
endpoint y el gateway reúne información del catálogo, inventario y tipo de cambio.

## Stack

- Java 21
- Spring Boot
- Maven

## Estructura

- `config`: configuración de la aplicación y clientes HTTP.
- `controller`: endpoints REST.
- `client/catalog`, `client/inventory`, `client/exchange`: clientes HTTP para los proveedores.
- `dto/request` y `dto/response`: contratos de entrada y salida.
- `service`: orquestación concurrente y composición del resumen.
- `exception`: manejo centralizado de errores para entradas inválidas y fallos de proveedores.

## Endpoint

- `GET /api/v1/products/{id}/summary`: devuelve el resumen del producto con precio
  convertido a MXN, disponibilidad y datos relevantes de catálogo e inventario.

Las consultas a los tres proveedores se ejecutan concurrentemente. El precio local
se redondea a dos decimales. Los clientes aplican límites de conexión y lectura; los
errores de proveedor se devuelven en un contrato uniforme con estado y ruta.

## Configuración

Las direcciones de servicios se configuran con propiedades Spring o variables de entorno:

- `CATALOG_SERVICE_URL` (por defecto `http://localhost:8081`)
- `INVENTORY_SERVICE_URL` (por defecto `http://localhost:8082`)
- `EXCHANGE_SERVICE_URL` (por defecto `https://api.frankfurter.dev/v1`)
- `EXCHANGE_BASE_CURRENCY` (por defecto `USD`)
- `EXCHANGE_TARGET_CURRENCY` (por defecto `MXN`)
- `SERVER_PORT` (por defecto `8080`)

## Ejecutar

En Windows: `mvnw.cmd spring-boot:run`

En Linux/macOS: `./mvnw spring-boot:run`

## Evidencias de funcionamiento

Los servicios de catálogo e inventario se encuentran en las carpetas hermanas
`catalog-service` e `inventory-service`. Para iniciar los tres procesos y obtener
las cuatro capturas de Postman o Insomnia, sigue la guía de la [raíz del repositorio](../README.md).
