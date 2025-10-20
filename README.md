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

<!-- Aquí va captura de la base de datos -->

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

<!-- Aquí va captura del sistema funcionando: listado y formulario -->

---

## 🎨 Interfaz y Diseño

El diseño utiliza **Bootstrap 5** con una paleta clara y componentes modernos:

- Encabezados y botones con bordes redondeados.  
- Tabla con hover y badges de colores (activo/inactivo).  
- Modal centralizado para edición de producto.  
- Toasts para feedback visual al usuario.

<!-- Aquí va captura del modal y los toasts -->

---

## 🔁 Flujo de Trabajo

1. El usuario accede a `/productos`.
2. El servlet `ProductoServlet` lista todos los productos.
3. Desde el formulario se pueden crear nuevos productos.
4. Al presionar **Editar**, se abre el modal con los datos cargados.
5. Al presionar **Guardar cambios**, se actualiza en BD.
6. Los mensajes se muestran mediante **toasts Bootstrap**.
7. El botón **Eliminar** ejecuta la acción de borrado tras confirmación.

<!-- Aquí va un diagrama o captura del flujo -->

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

<!-- Aquí va captura del despliegue exitoso -->

---

## 🧪 Pruebas sugeridas

- [ ] Crear un nuevo producto válido.  
- [ ] Probar validaciones (campos vacíos o incorrectos).  
- [ ] Editar un producto existente.  
- [ ] Eliminar un registro.  
- [ ] Verificar mensajes visuales (toasts).  

---

## 👨‍💻 Autor

**Nombre:** _[Tu nombre completo]_  
**Correo:** _[tu correo institucional]_  
**Fecha de entrega:** _[día/mes/año]_  
**Docente:** _[nombre del profesor]_  
**Asignatura:** _[nombre del curso o taller]_  

---

## 📸 Evidencias Visuales

📷 *Inserta aquí tus pantallazos del proyecto funcionando:*  
- Base de datos inicial  
- Despliegue en GlassFish  
- Listado y formulario  
- Modal de edición  
- Mensajes de éxito/error  

---

## 🏁 Conclusión

El proyecto **Proyecto Inventario** demuestra el uso integral de **Jakarta EE 10**, aplicando conceptos de **arquitectura en capas**, **inyección de dependencias (CDI)**, **DAO pattern**, **validaciones de negocio** y **diseño moderno con Bootstrap 5**.  
Es una base sólida para escalar hacia sistemas empresariales más completos o integraciones con APIs REST.

---

🧡 *“La mejor forma de aprender backend es construir algo que realmente funcione.”*  
— *Fin del Taller Jakarta EE*
