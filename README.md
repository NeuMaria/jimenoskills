# Proyecto JPA tienda sin Spring Boot

Proyecto básico para aprender JPA con Java, Maven, Hibernate y MySQL.

## Qué contiene

- Entidad `Cliente`
- Entidad `Articulo`
- Entidad `Venta`
- Relación `Venta` → `Cliente` con `@ManyToOne`
- Relación `Venta` → `Articulo` con `@ManyToOne`
- DAO para clientes, artículos y ventas
- Menú por consola

## Base de datos

Crea una base de datos en MySQL:

```sql
CREATE DATABASE tienda_jpa;
```

Hibernate creará automáticamente las tablas:

- `Cliente`
- `Articulo`
- `Venta`

Gracias a esta propiedad del `persistence.xml`:

```xml
<property name="hibernate.hbm2ddl.auto" value="update"/>
```

## Configuración

Revisa el archivo:

```text
src/main/resources/META-INF/persistence.xml
```

Y cambia usuario y contraseña si es necesario:

```xml
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value=""/>
```

## Clase principal

Ejecuta:

```text
com.ejemplo.Main
```

## Orden recomendado para probar

1. Insertar cliente
2. Insertar artículo
3. Registrar venta
4. Listar ventas

Al registrar una venta, el programa descuenta automáticamente el stock del artículo.
