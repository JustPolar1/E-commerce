# E-Commerce Composite API

Proyecto Java 21 / Spring Boot con dos microservicios propios y un gateway
agregador. Los servicios publican el producto de prueba `P-101`; IDs desconocidos
responden `404`. No requieren base de datos.

## Levantar las aplicaciones

Abre una terminal de PowerShell por aplicación y ejecuta desde la raíz del repo:

```powershell
cd catalog-service
..\digital\mvnw.cmd spring-boot:run
```

```powershell
cd inventory-service
..\digital\mvnw.cmd spring-boot:run
```

```powershell
cd digital
.\mvnw.cmd spring-boot:run
```

Por defecto, catálogo usa `8081`, inventario `8082` y el gateway `8080`.

## Capturas en Postman o Insomnia

Envía estas solicitudes `GET`, usando `P-101` para mantener el mismo producto en
todas las capturas:

1. Catálogo: `http://localhost:8081/microservices/products/P-101`
2. Inventario: `http://localhost:8082/microservices/inventory/P-101`
3. Divisas: `https://api.frankfurter.dev/v1/latest?from=USD&to=MXN`
4. Composite API: `http://localhost:8080/api/v1/products/P-101/summary`

En cada captura muestra la URL, el estado HTTP y el JSON de respuesta. La última
solicitud consulta automáticamente catálogo, inventario y Frankfurter. El tipo de
cambio es la última tasa publicada por Frankfurter y puede variar.

## Pruebas

Desde cada carpeta de servicio ejecuta `..\digital\mvnw.cmd test`; desde `digital`
ejecuta `.\mvnw.cmd test`.
