# HomeSweetHome-backend

Es una aplicación backend inspirada en Airbnb, desarrollada con Spring Boot (Java 17) y PostgreSQL (Neon Cloud).
Su objetivo es gestionar usuarios, alojamientos, reservas, pagos y reseñas de forma escalable, segura y moderna.

🚀 Tecnologías principales
Tecnología	Descripción
Java 17	Lenguaje principal del proyecto
Spring Boot 3	Framework backend
Spring Data JPA	ORM para la persistencia de datos
PostgreSQL (Neon)	Base de datos en la nube
pgAdmin 4	Herramienta de administración de la base
Maven	Gestor de dependencias y build tool
Lombok	Generación automática de getters, setters y builders
🧩 Arquitectura del proyecto
openLodge/
├── src/
│   ├── main/
│   │   ├── java/com/openlodge/
│   │   │   ├── entities/         # Entidades JPA
│   │   │   ├── repositories/     # Repositorios (interfaces JPA)
│   │   │   ├── services/         # Lógica de negocio
│   │   │   ├── controllers/      # Endpoints REST
│   │   │   └── OpenLodgeApplication.java
│   │   └── resources/
│   │       ├── application.yml   # Configuración principal
│   │       └── static / templates (si aplica)
│   └── test/                     # Tests automáticos
└── pom.xml                       # Dependencias y build

⚙️ Configuración inicial
1️⃣ Requisitos previos

Asegurate de tener instalado:

Java 17+

Maven 3.8+

pgAdmin 4

Una cuenta en Neon.tech
 (para PostgreSQL en la nube)

2️⃣ Clonar el repositorio
git clone https://github.com/tuusuario/openlodge-backend.git
cd openlodge-backend

3️⃣ Configurar la base de datos (Neon)

Crea una base en Neon.tech
 y anota tus credenciales.
Luego, configuralas en src/main/resources/application.yml:

spring:
  datasource:
    url: jdbc:postgresql://<TU_HOST_NEON>/<TU_DB_NAME>?sslmode=require
    username: <TU_USUARIO>
    password: <TU_PASSWORD>
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080


💡 Al ejecutar el proyecto, Hibernate creará automáticamente todas las tablas en la base Neon.

4️⃣ Compilar y ejecutar
mvn clean install
mvn spring-boot:run


Luego, abrí tu navegador y accedé a:

👉 http://localhost:8080

🧠 Entidades principales
Entidad	Descripción
User	Usuarios del sistema (huéspedes y anfitriones)
Accommodation	Alojamiento publicado (casa, depto, habitación)
Reservation	Reservas realizadas por los huéspedes
Review	Reseñas y calificaciones de los alojamientos
Location	Dirección y coordenadas de cada alojamiento
Image	Imágenes asociadas a los alojamientos
Payment	Pagos vinculados a las reservas
Amenity	Servicios y comodidades disponibles
🧩 Relaciones entre entidades

User (host) ↔️ Accommodation → 1:N

User (guest) ↔️ Reservation → 1:N

Accommodation ↔️ Image → 1:N

Accommodation ↔️ Amenity → N:M

Accommodation ↔️ Review → 1:N

Reservation ↔️ Payment → 1:1

Accommodation ↔️ Location → 1:1

📦 Scripts útiles en pgAdmin
Ver todas las tablas
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';

Vaciar todas las tablas (borrar esquema)
DO $$ DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END $$;

🧪 Prueba de conexión rápida

Podés agregar este componente para confirmar la conexión a Neon al iniciar la app:

@Component
public class DatabaseChecker implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;
    public DatabaseChecker(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    @Override
    public void run(String... args) {
        String dbName = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
        String user = jdbcTemplate.queryForObject("SELECT current_user", String.class);
        System.out.println("✅ Connected to database: " + dbName + " as user: " + user);
    }
}

🧰 Dependencias principales (pom.xml)
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

👥 Equipo y desarrollo
Rol	Nombre	Tareas principales
💻 Backend Developer	Tu nombre	Modelado de entidades, endpoints REST, integración con PostgreSQL
🎨 Frontend Developer	—	(Próxima etapa)
🧠 QA / Tester	—	(Pendiente de asignación)
📈 Próximos pasos

 Implementar Repositories (JPA interfaces)

 Crear capa de servicios (@Service)

 Exponer endpoints REST (@RestController)

 Añadir autenticación JWT

 Integrar con el frontend (React / Next.js)

 Desplegar en entorno de prueba (Railway / Render / Fly.io)

🧾 Licencia

Este proyecto es de uso académico y educativo.
Desarrollado por OpenLodge Team © 2025.

