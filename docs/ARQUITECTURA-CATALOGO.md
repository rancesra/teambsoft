# Arquitectura — Catálogo (Equipo B)

**Entrega:** Primera entrega — Ciclo 1
**Fecha:** 2026-08-21

## 1. Rol en el sistema

Catálogo es uno de los 5 microservicios del sistema (junto a Búsqueda, Carro, Descuento y Órdenes), y es la **fuente de verdad de los productos**. Todo el tráfico externo pasa por Kong (API Gateway); Catálogo se registra en Eureka para ser descubierto, y publica eventos a RabbitMQ para que Búsqueda mantenga su índice sin consultarlo directamente. El diagrama general del sistema (ya entregado por el equipo) muestra este contexto completo; este documento se enfoca solo en la pieza de Catálogo.

## 2. Stack tecnológico

| Tecnología | Uso en Catálogo |
|---|---|
| Spring Boot | Framework del microservicio |
| MongoDB | Persistencia de productos y categorías (documento, no relacional) |
| Spring Data MongoDB | Capa de acceso a datos |
| Eureka Client | Registro y descubrimiento del servicio |
| Docker | Empaquetado y entorno reproducible |
| RabbitMQ (vía Spring AMQP) | Publicación de eventos de dominio |

## 3. Arquitectura interna

```mermaid
graph TD
    Kong[Kong API Gateway] -->|REST| Controller[ProductoController]
    Controller --> Service[ProductoService]
    Service --> Repository[ProductoRepository]
    Repository --> Mongo[(MongoDB)]
    Service --> Publisher[EventPublisher]
    Publisher -->|eventos| RabbitMQ[RabbitMQ Event Bus]
    Controller -.->|se registra al iniciar| Eureka[Eureka]
```

Capas: **Controller** expone los endpoints REST y valida entrada; **Service** contiene la lógica de negocio (validaciones, reglas de soft delete); **Repository** es la interfaz de Spring Data hacia MongoDB; **EventPublisher** emite los eventos de dominio después de cada operación de escritura exitosa.

> Nota: este diagrama se renderiza automáticamente al ver el archivo en GitHub (soporta Mermaid de forma nativa en Markdown).

## 4. Modelo de datos

Producto y Categoría, con sus campos y reglas de validación, están definidos en detalle en `CONTRATO-CATALOGO.md`. En resumen: Producto es el documento principal (nombre, precio, categoría, stock, imágenes, activo); Categoría es una lista precargada y de solo lectura en esta fase.

## 5. Decisiones de diseño

- **MongoDB en vez de relacional:** los productos no siempre comparten los mismos atributos, y el modelo de documento evita forzar una estructura rígida.
- **Soft delete (`activo:false`) en vez de borrado físico:** preserva integridad si otro servicio (como Órdenes, más adelante) referencia un producto históricamente.
- **El precio y el stock los valida y devuelve siempre Catálogo, nunca el cliente:** evita que Carro u otro servicio confíen en datos que el usuario podría manipular.
- **Eventos hacia Búsqueda, llamada síncrona para Carro:** Búsqueda no necesita el dato al instante y vive bien con un índice eventualmente consistente; Carro sí necesita confirmar en el momento antes de dejar continuar al usuario.
- **Errores con código, no solo texto:** permite que cualquier consumidor reaccione programáticamente sin parsear mensajes.

## 6. Documentos relacionados

- `CONTRATO-CATALOGO.md` — contrato completo de endpoints, modelos y eventos (entregable de Segunda entrega, Ciclo 2)
- `HISTORIAS.md` — backlog de historias de usuario de Catálogo
