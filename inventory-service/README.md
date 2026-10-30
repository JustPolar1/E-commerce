# Inventory Service

Servicio REST de inventario para la práctica Composite API. Expone
`GET /microservices/inventory/{productId}` en el puerto `8082` y proporciona el
inventario de prueba para `P-101`.

Desde esta carpeta, inicia el servicio con `..\digital\mvnw.cmd spring-boot:run`
y ejecuta sus pruebas con `..\digital\mvnw.cmd test`.
