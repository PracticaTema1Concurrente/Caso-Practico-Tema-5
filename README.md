Gabriel Kaakedjian e Iván Hidalgo

# 🙅‍♂️ Wakanda Smart City - Sistema de Gestión de Servicios

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.8-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

## 📋 Enunciado del Proyecto

### Resumen
Wakanda está desarrollando un sistema avanzado para la gestión de servicios tecnológicos en su ciudad inteligente utilizando microservicios. Este sistema debe ser capaz de manejar múltiples servicios de manera eficiente, asegurando la resiliencia, la escalabilidad y la capacidad de respuesta. Utilizando técnicas avanzadas de desarrollo de microservicios con Spring Boot y Spring Cloud, se busca implementar un sistema modular y robusto que permita la integración y gestión de diferentes servicios de manera eficiente y segura.

### Reto
El principal desafío es gestionar de manera eficiente y modular los diferentes servicios de la ciudad inteligente, asegurando que el sistema pueda escalar y mantenerse resiliente ante posibles fallos. Además, es necesario implementar mecanismos de descubrimiento de servicios, balanceo de carga y monitoreo centralizado.

### Áreas de Gestión
1.  **Gestión Inteligente del Tráfico:** Semáforos, sensores, parking y rutas de transporte.
2.  **Gestión de Residuos:** Contenedores con sensores, reciclaje inteligente y plataformas de gestión.
3.  **Seguridad y Vigilancia:** Cámaras con IA, flota de drones y alertas en tiempo real.
4.  **Salud y Bienestar:** Telemedicina, monitoreo IoT de signos vitales y centros conectados.
5.  **Servicios de Emergencia:** Respuesta inteligente, alertas geolocalizadas y rescate con drones.

---

## 🛠️ Lo que hemos implementado (Resumen Técnico)

En este proyecto, hemos construido una arquitectura distribuida completa desde cero. Hemos diseñado cada módulo mencionado en el enunciado como un **microservicio independiente**, comunicados entre sí y gestionados de forma centralizada.

**Aspectos clave de nuestro código:**
*   **Arquitectura de Red:** Implementamos un ecosistema con un **Config Server** para gestión centralizada de YAMLs, un **Discovery Server (Eureka)** para el registro dinámico de instancias y un **API Gateway** como punto único de entrada.
*   **Lógica de Negocio Real:** Cada controlador incluye endpoints funcionales (simulación de sensores IoT, gestión de drones, agendas médicas, etc.) con datos generados dinámicamente.
*   **Resiliencia:** Aplicamos el patrón **Circuit Breaker** con Resilience4j en todos los servicios críticos para asegurar que un fallo en un módulo no tumbe el sistema completo.
*   **Gestión de Configuración:** Utilizamos perfiles nativos en el Config Server para inyectar puertos, nombres de aplicación y configuraciones de seguridad de forma externa a los servicios.

---

## 🏗️ Arquitectura del Sistema

El proyecto se divide en capas de infraestructura y servicios de negocio:

### Infraestructura (Core)
*   **`wakanda-config-server` (Puerto 8888):** Repositorio central de configuración.
*   **`wakanda-discovery` (Puerto 8761):** Servidor Eureka para localización de servicios.
*   **`wakanda-gateway` (Puerto 8080):** Puerta de enlace unificada.

### Microservicios de Ciudad
*   **`wakanda-traffic` (Puerto 8081):** Control de tráfico, parking y transporte público.
*   **`wakanda-emergency` (Puerto 8082):** Coordinación de ambulancias y alertas de desastre.
*   **`wakanda-waste` (Puerto 8083):** Gestión de sensores de basura y reciclaje.
*   **`wakanda-health` (Puerto 8084):** Telemedicina y monitoreo de constantes vitales.
*   **`wakanda-security` (Puerto 8085):** Vigilancia por cámaras e IA y patrullas de drones.

---

## 🚀 Cómo arrancar el proyecto

1.  **Requisitos:** Java 17+ y Maven instalado.
2.  **Orden de encendido (Crucial):**
    1.  Arrancar `ConfigServerApplication`.
    2.  Arrancar `DiscoveryApplication` (Eureka).
    3.  Arrancar `GatewayApplication`.
    4.  Arrancar el resto de servicios (`Traffic`, `Health`, `Emergency`, etc.).
3.  **Verificación:** Accede a `http://localhost:8761` para ver todos los servicios registrados.

---

## 🚦 Endpoints Principales (vía Gateway)

Puedes probar todo el sistema a través del puerto `8080`:

| Servicio | Endpoint de prueba | Descripción |
| :--- | :--- | :--- |
| **Salud** | `GET /wakanda-health/health/guia` | Guía interactiva del sistema médico |
| **Tráfico** | `GET /wakanda-traffic/traffic/parking/estado` | Estado de parkings inteligentes |
| **Seguridad** | `GET /wakanda-security/security/estado` | Dashboard general de vigilancia |
| **Emergencia** | `POST /wakanda-emergency/emergency/asignar` | Asignación de unidades de rescate |
| **Residuos** | `GET /wakanda-waste/waste/puntos` | Mapa de puntos de recolección |

---

## 🛡️ Resiliencia y Monitoreo

Todos los servicios exponen métricas a través de **Spring Actuator**:
*   Salud del sistema: `GET /actuator/health`
*   Estado de Circuit Breakers: `GET /actuator/circuitbreakers`

El sistema está preparado para integrarse con **Prometheus** y **Grafana** (archivo `prometheus.yml` incluido) para visualización en tiempo real.

---
*Desarrollado para el Caso Práctico - Tema 5. Wakanda Forever! 🙅‍♂️🏾*
