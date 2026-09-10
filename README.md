# 🛍️ Tienda Virtual — Microservicio de Catálogo

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-8.0-47A248?logo=mongodb&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-eventos-FF6600?logo=rabbitmq&logoColor=white)
![Vue.js](https://img.shields.io/badge/Frontend-Vue.js-4FC08D?logo=vuedotjs&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-compose-2496ED?logo=docker&logoColor=white)

**Equipo B** · Ingeniería de Software II · Universidad Industrial de Santander (UIS)

Catálogo es la **fuente de verdad de los productos** de la tienda virtual. Expone un CRUD de productos por REST (a través de Kong) y publica eventos en RabbitMQ para que los demás servicios se mantengan sincronizados.

## Arquitectura

```mermaid
graph LR
    Web["Cliente web<br/>(Vue.js)"] -->|REST| Kong["Kong<br/>API Gateway"]
    Carro["Carro<br/>(Equipo C)"] -->|"GET /productos/{id}"| Kong
    Kong --> Catalogo["Catálogo<br/>(Spring Boot)"]
    Catalogo --> Mongo[(MongoDB)]
    Catalogo -->|eventos| Rabbit[["RabbitMQ<br/>catalogo.eventos"]]
    Rabbit --> Busqueda["Búsqueda<br/>(Equipo A)"]
    Catalogo -.->|registro| Eureka[Eureka]
```

El detalle de las capas internas está en [ARQUITECTURA-CATALOGO.md](docs/ARQUITECTURA-CATALOGO.md).

## Documentación

| Documento | Contenido |
|---|---|
| [Contrato de servicio](docs/CONTRATO-CATALOGO.md) | Endpoints, modelos, errores y eventos acordados con los equipos A y C (v2.2) |
| [Historias de usuario](docs/HISTORIAS.md) | Backlog con criterios de aceptación |
| [Arquitectura](docs/ARQUITECTURA-CATALOGO.md) | Stack, capas internas y decisiones de diseño |

## Estructura del repositorio

```
├── docs/      Documentación del proyecto
└── backend/   Microservicio de Catálogo (Spring Boot)
```

## Cómo ejecutar el backend

**Requisitos:** JDK 21. No hace falta instalar Maven: el proyecto trae el Maven Wrapper (`mvnw`).

```bash
cd backend
./mvnw test              # corre las pruebas
./mvnw spring-boot:run   # arranca el servicio en http://localhost:8080
```

En Windows usa `mvnw.cmd` en lugar de `./mvnw`.

> Esta sección se irá actualizando a medida que se integren MongoDB, RabbitMQ, Eureka y Kong con Docker.
>
> ¿Eres del equipo y vas a empezar? Sigue la [guía de inicio](GUIA-INICIO.md).

## Estado — Segunda entrega

- [x] Documentación: historias, arquitectura y contrato
- [x] Esqueleto del backend
- [ ] Conexión a MongoDB con Docker
- [ ] Historia 1 — Registrar producto
- [ ] Historia 2 — Listar productos
- [ ] Historia 3 — Ver detalle de un producto
- [ ] Historia 4 — Actualizar producto
- [ ] Historia 5 — Desactivar producto
- [ ] Historia 6 — Listar categorías
- [ ] Publicación de eventos en RabbitMQ
- [ ] Registro en Eureka
- [ ] Integración con Kong

## Equipo B

| Integrante | Parte |
|---|---|
| _Rances Ramirez_ | Backend |
| _Nombre_ | Backend |
| _Nombre_ | Backend |
| _Nombre_ | Backend |
| _Nombre_ | Frontend |
| _Nombre_ | Frontend |
| _Nombre_ | Frontend |
