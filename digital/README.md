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
- `EXCHANGE_SERVICE_URL` (por defecto `https://api.frankfurter.app`)
- `EXCHANGE_BASE_CURRENCY` (por defecto `USD`)
- `EXCHANGE_TARGET_CURRENCY` (por defecto `MXN`)
- `SERVER_PORT` (por defecto `8080`)

## Ejecutar

En Windows: `mvnw.cmd spring-boot:run`

En Linux/macOS: `./mvnw spring-boot:run`

## Evidencias de funcionamiento en Postman o Insomnia

Antes de tomar las capturas, inicia los microservicios propios de catálogo e
inventario en sus direcciones configuradas y después inicia este gateway en el
puerto `8080`. Los servicios de catálogo e inventario son dependencias externas a
este proyecto; esta aplicación contiene sus clientes, pero no sus implementaciones.

Usa el mismo identificador de producto (`P-101`) en todas las solicitudes. En
Postman o Insomnia selecciona el método `GET`, envía cada solicitud y captura la
pantalla mostrando la URL, el estado HTTP y el cuerpo JSON de respuesta.

| Captura | Solicitud | Qué debe mostrar |
|---|---|---|
| 1. Catálogo | `GET http://localhost:8081/microservices/products/P-101` | Respuesta JSON con `product_id`, `title`, `description`, `category` y `price_usd`. |
| 2. Inventario | `GET http://localhost:8082/microservices/inventory/P-101` | Respuesta JSON con `product_id`, `stock_status`, `available_units`, `warehouse_location` y `estimated_delivery`. |
| 3. Divisas | `GET https://api.frankfurter.app/latest?from=USD&to=MXN` | Respuesta JSON de Frankfurter que contenga la tasa MXN en `rates`. |
| 4. Composite API | `GET http://localhost:8080/api/v1/products/P-101/summary` | Un único JSON con los datos de producto e inventario, `price_usd`, `price_mxn`, `currency_conversion_source` y `status: "SUCCESS"`. |

La cuarta solicitud consulta automáticamente los tres proveedores. El precio MXN
se calcula con la tasa recibida de Frankfurter y se redondea a dos decimales; por
eso el valor puede variar cuando cambie la tasa publicada. Para reproducir la
captura compuesta, los servicios de catálogo e inventario deben responder con el
contrato descrito en las dos primeras filas. Si utilizas otros puertos o hosts,
configura `CATALOG_SERVICE_URL`, `INVENTORY_SERVICE_URL` y `SERVER_PORT` antes de
iniciar el gateway.
