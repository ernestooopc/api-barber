# Sistema de Reservas para Barbería

## 📋 Descripción general
Aplicación **full-stack** para gestión de citas en una barbería, que cubre:

- **Usuarios**: registro, login con JWT y roles.  
- **Barberos**: CRUD completo.  
- **Categorías** y **Tipos de Corte**: CRUD con validaciones de datos únicos.  
- **Horarios Disponibles**: generación automática por rango, filtrado según reservas y pausas de almuerzo.  
- **Reservas**: creación, actualización, cancelación, búsqueda por usuario/estado/rango de fechas.  
- **Pagos**: integración con Stripe para crear `PaymentIntent` (moneda PEN), confirmar pago y exponer URL de recibo.  
- **Boletas**: generación dinámica de PDF a partir de un DTO (`BoletaData`) y plantilla de datos.

---

## 🚀 Tecnologías y herramientas

### 🔙 Back-end
- **Java 17** + **Spring Boot 3.5**  
- **Spring Data JPA** + Hibernate    
- **PDF**: DTO `BoletaData` y `BoletaService` para generación de boletas  
- **Stripe**: `StripeService` para `PaymentIntent` y URL de recibos  


## 📡 Endpoints principales (Back-end)

| **Ruta**                                     | **Método**                      | **Descripción**                                                   |
|----------------------------------------------|---------------------------------|-------------------------------------------------------------------|
| `/api/usuarios`                              | `POST`                          | Crear usuario (validación de email único).                        |
| `/api/usuarios/login`                        | `POST`                          | Autenticación, devuelve JWT + rol + id.                           |
| `/api/barberos`                              | `GET` / `POST` / `PUT` / `DELETE` | CRUD de barberos.                                              |
| `/api/categorias`                            | CRUD                            | CRUD de categorías con manejo de excepciones.                     |
| `/api/tipocortes`                            | CRUD                            | CRUD de tipos de corte; validación de nombre único.               |
| `/api/horarios-disponibles`                  | `GET`                           | Listar por barbero+fecha, excluyendo reservas y rango de almuerzo.|
| `/api/horarios-disponibles/rango`            | `POST`                          | Generar franjas en un rango con intervalo configurable.           |
| `/api/reservas`                              | CRUD + `PATCH`                  | Crear, listar, actualizar, cancelar, filtrar por fecha/estado.    |
| `/api/pagos/crear-intento`                   | `POST`                          | Crea `PaymentIntent` en Stripe (monto calculado de `TipoCorte`).  |
| `/api/pagos/confirmar-pago/{reservaId}`      | `POST`                          | Marca reserva como **PAGADO**.                                    |
| `/api/pagos/boleta`                          | `POST`                          | Genera PDF de boleta a partir de `BoletaData`.                    |
| `/api/pagos/recibo/{paymentIntentId}`        | `GET`                           | Devuelve URL de recibo de Stripe.                                 |
| `/api/pagos/boleta/{reservaId}`              | `GET`                           | Genera boleta desde reserva existente.                            |

---

## 🏗️ Cómo arrancar el proyecto

1. Clona el repositorio  
   ```bash
   git clone https://github.com/ernestooopc/api-barber.git
