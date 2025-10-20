# 🧾 Proyecto Inventario — Aplicación Web con Jakarta EE 10 + Bootstrap 5

## 📘 Descripción General

Este proyecto es una aplicación web para la **gestión de inventario de productos**, desarrollada con **Jakarta EE 10**, **Maven**, **JSP/Servlets**, y **GlassFish Server 7**.  
Permite realizar operaciones **CRUD (Crear, Listar, Editar y Eliminar)** sobre productos almacenados en una base de datos MySQL, siguiendo una arquitectura en capas y buenas prácticas de diseño.

---

## 🚀 Tecnologías Utilizadas

| Tecnología / Herramienta | Versión | Descripción |
|---------------------------|----------|--------------|
| **Java JDK**              | 21       | Lenguaje de programación principal |
| **Jakarta EE**            | 10       | Plataforma para aplicaciones empresariales Java |
| **GlassFish Server**      | 7.0.9    | Servidor de aplicaciones |
| **Apache NetBeans IDE**   | 20       | Entorno de desarrollo |
| **Maven**                 | 3.9+     | Gestión de dependencias y empaquetado WAR |
| **MySQL**                 | 8.0.17   | Base de datos relacional |
| **Bootstrap 5**           | 5.3.2    | Framework CSS para diseño responsivo y moderno |
| **JSTL (Jakarta Tags)**   | 3.0.1    | Biblioteca de etiquetas para JSP |

---

## 🧱 Arquitectura del Proyecto

El proyecto está estructurado en **capas** para mantener separación de responsabilidades:

```
src/main/java/
 ├─ com.inventario.controller      → Servlets (controladores)
 │    └─ ProductoServlet.java
 ├─ com.inventario.facade          → Lógica de negocio
 │    └─ ProductoFacade.java
 ├─ com.inventario.persistence     → Acceso a datos (DAO)
 │    └─ ProductoDAO.java
 ├─ com.inventario.model           → Entidades (Beans)
 │    └─ Producto.java
 ├─ com.inventario.service         → Validaciones y reglas de negocio
 │    └─ ValidadorProducto.java
 ├─ com.inventario.view            → Beans de mensajes (CDI)
 │    └─ MensajeBean.java
 └─ com.inventario.config          → Configuración de recursos CDI
      └─ DataSourceProducer.java
```

```
src/main/webapp/
 ├─ productos.jsp           → Vista principal (listado y CRUD)
 ├─ index.jsp               → Página de inicio
 ├─ WEB-INF/web.xml         → Configuración del despliegue
 ├─ META-INF/               → Metadatos de la aplicación
 └─ styles/ (opcional)      → Archivos CSS adicionales
```

---

## ⚙️ Configuración del Entorno

### 🔧 1. Crear la base de datos en MySQL

```sql
CREATE DATABASE inventario_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE inventario_db;

CREATE TABLE producto (
  id INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(50) NOT NULL UNIQUE,
  nombre VARCHAR(150) NOT NULL,
  categoria VARCHAR(100),
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  activo BOOLEAN DEFAULT TRUE
);

-- Datos iniciales
INSERT INTO producto (codigo, nombre, categoria, precio, stock, activo) VALUES
('LAP001', 'Laptop Dell Inspiron 15', 'Electrónicos', 2499.99, 5, TRUE),
('MOU001', 'Mouse Inalámbrico Logitech', 'Accesorios', 79.99, 25, TRUE),
('CHR001', 'Silla Ergonómica de Oficina', 'Muebles', 899.99, 3, TRUE);
```

---

### 🗄️ 2. Configurar el DataSource en GlassFish

- **JDBC Connection Pool:**  
  - Name: `InventarioPool`  
  - Resource Type: `javax.sql.DataSource`  
  - Driver: `com.mysql.cj.jdbc.Driver`  
  - URL: `jdbc:mysql://localhost:3306/inventario_db?useSSL=false&serverTimezone=UTC`
  - User: `root`  
  - Password: *(tu contraseña)*

- **JDBC Resource:**  
  - JNDI Name: `jdbc/inventarioDS`  
  - Pool Name: `InventarioPool`

---

### 🧩 3. Configurar el archivo `DataSourceProducer.java`

```java
@Resource(lookup = "jdbc/inventarioDS")
@Produces
private DataSource ds;
```

---

### 🧱 4. Dependencias principales (`pom.xml`)

```xml
<dependencies>
  <!-- API Jakarta EE -->
  <dependency>
    <groupId>jakarta.platform</groupId>
    <artifactId>jakarta.jakartaee-web-api</artifactId>
    <version>10.0.0</version>
    <scope>provided</scope>
  </dependency>

  <!-- JSTL -->
  <dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
  </dependency>

  <!-- MySQL Driver -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.17</version>
  </dependency>
</dependencies>
```

---

## 🧩 Funcionalidades Implementadas

| Funcionalidad | Descripción |
|----------------|-------------|
| **Listar productos** | Muestra todos los registros de la base de datos en una tabla. |
| **Agregar producto** | Crea un nuevo registro mediante el formulario principal. |
| **Editar producto** | Abre un **modal Bootstrap** con los datos del producto para editarlo. |
| **Eliminar producto** | Confirma y elimina el registro seleccionado. |
| **Mensajes dinámicos** | Muestra toasts personalizados para éxito/error. |
| **Validaciones de negocio** | Código, nombre, precio y stock verificados antes de guardar. |

<img width="589" height="294" alt="image" src="https://github.com/user-attachments/assets/29ac9c70-d251-4cb5-8fcb-4ddec2bc968b" />


---

## 🎨 Interfaz y Diseño

El diseño utiliza **Bootstrap 5** con una paleta clara y componentes modernos:

- Encabezados y botones con bordes redondeados.  
- Tabla con hover y badges de colores (activo/inactivo).  
- Modal centralizado para edición de producto.  
- Toasts para feedback visual al usuario.

---

## 🔁 Flujo de Trabajo

1. El usuario accede a `/productos`.
2. El servlet `ProductoServlet` lista todos los productos.
3. Desde el formulario se pueden crear nuevos productos.
4. Al presionar **Editar**, se abre el modal con los datos cargados.
5. Al presionar **Guardar cambios**, se actualiza en BD.
6. Los mensajes se muestran mediante **toasts Bootstrap**.
7. El botón **Eliminar** ejecuta la acción de borrado tras confirmación.


---

## 🧠 Reglas de Validación (ValidadorProducto.java)

| Campo | Regla |
|--------|-------|
| Código | Mínimo 3 caracteres |
| Nombre | Mínimo 5 caracteres |
| Precio | Mayor a 0 |
| Stock  | No negativo |
| Activo | Por defecto `true` |

---

## 🧰 Despliegue en GlassFish

1. Limpiar y compilar el proyecto:
   ```bash
   mvn clean package
   ```
2. Desplegar el `.war` generado:
   ```
   target/Proyecto_Inventario-1.0-SNAPSHOT.war
   ```
3. Iniciar GlassFish y verificar en:
   ```
   http://localhost:8080/Proyecto_Inventario/productos
   ```

---

## 🧪 Pruebas sugeridas

- [ ] Crear un nuevo producto válido.  
- [ ] Probar validaciones (campos vacíos o incorrectos).  
- [ ] Editar un producto existente.  
- [ ] Eliminar un registro.  
- [ ] Verificar mensajes visuales (toasts).  

---

## 👨‍💻 Autores

**Nombre:** Smichell Contreras Y Johana Anaya 

**Asignatura:** Desarrollo de Aplicaciones Empresariales 

---

## 📸 Evidencias Visuales
Inicio
 <img width="589" height="294" alt="image" src="https://github.com/user-attachments/assets/230e1592-9cb3-44bb-bf00-477529d58b57" />

Nos muestra el listado de productos actualmente en la base de datos y nos permite hacer diferentes funciones con cada uno.
Agregando un nuevo producto al inventario:

<img width="589" height="171" alt="image" src="https://github.com/user-attachments/assets/f0776d7b-631f-4fd1-8e12-3916d0343289" />
<img width="589" height="151" alt="image" src="https://github.com/user-attachments/assets/7b7efb5e-061d-409c-aaee-457091ed3f69" />

Editando un producto ya creado:
<img width="589" height="291" alt="image" src="https://github.com/user-attachments/assets/7ccbb75f-683a-4a80-a5e8-132905aba4ad" />
<img width="589" height="137" alt="image" src="https://github.com/user-attachments/assets/15872084-5a1c-4f73-b74b-e837a1fdf9db" />

Eliminando un producto del inventario:
<img width="589" height="294" alt="image" src="https://github.com/user-attachments/assets/0594a84f-fd0b-427b-8bea-8b70b531f01b" />

<img width="589" height="236" alt="image" src="https://github.com/user-attachments/assets/9608e115-f96d-4b0e-9ee2-3292c3e4f8d0" />

---

## 🏁 Conclusión

El proyecto **Proyecto Inventario** demuestra el uso integral de **Jakarta EE 10**, aplicando conceptos de **arquitectura en capas**, **inyección de dependencias (CDI)**, **DAO pattern**, **validaciones de negocio** y **diseño moderno con Bootstrap 5**.  
Es una base sólida para escalar hacia sistemas empresariales más completos o integraciones con APIs REST.

---

🧡 *“La mejor forma de aprender backend es construir algo que realmente funcione.”*  
— *Fin del Taller Jakarta EE*
