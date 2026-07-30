A common production setup for **Spring Boot + Flyway** is to let Flyway manage all schema changes automatically during application startup, with versioned SQL migrations stored in your project.

## Project structure

```text
src/
 └── main/
     ├── java/
     │    └── com/example/demo/
     └── resources/
          ├── application.yml
          └── db/
              └── migration/
                  ├── V1__create_users_table.sql
                  ├── V2__add_email_index.sql
                  └── V3__create_orders_table.sql
```

---

## Maven dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

---

## Production configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db-server:5432/appdb
    username: appuser
    password: ${DB_PASSWORD}

  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: false
    validate-on-migrate: true
    clean-disabled: true

  jpa:
    hibernate:
      ddl-auto: validate
```

### Why these settings?

* `ddl-auto=validate`

  * Hibernate verifies the schema matches entities.
  * It does **not** create or modify tables.

* `validate-on-migrate=true`

  * Detects modified migration files.

* `clean-disabled=true`

  * Prevents accidental database deletion in production.

* `baseline-on-migrate=false`

  * Recommended for new production databases.
  * Only enable when introducing Flyway to an existing schema.

---

## Migration example

### V1__create_users_table.sql

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

### V2__add_email_index.sql

```sql
ALTER TABLE users
ADD COLUMN email VARCHAR(255);

CREATE UNIQUE INDEX idx_users_email
ON users(email);
```

---

### V3__create_orders_table.sql

```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
```

---

## CI/CD workflow

A typical production deployment looks like this:

```text
Developer
      │
      ▼
Create V4__add_status_column.sql
      │
      ▼
Commit to Git
      │
      ▼
CI builds Docker image
      │
      ▼
Deploy application
      │
      ▼
Application starts
      │
      ▼
Flyway checks flyway_schema_history
      │
      ▼
Runs V4 migration
      │
      ▼
Application becomes ready
```

Flyway records each successful migration in the `flyway_schema_history` table, ensuring each migration runs only once.

---

## Best practices for production

### 1. Never edit an executed migration

❌ Don't modify:

```sql
V2__add_email.sql
```

after it has been applied.

Instead create:

```text
V3__rename_email.sql
```

---

### 2. One migration per logical change

Good:

```text
V10__create_product_table.sql
V11__add_product_category.sql
V12__create_product_indexes.sql
```

---

### 3. Make migrations backward-compatible whenever it's possible.

Instead of:

```sql
ALTER TABLE users DROP COLUMN username;
```

use a staged approach:

1. Add a new column.
2. Deploy application supporting both.
3. Migrate data.
4. Remove an old column in a later release.

This enables safer zero- or low-downtime deployments.

---

### 4. Keep DDL out of Hibernate

Use:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Avoid in production:

```yaml
ddl-auto: update
ddl-auto: create
ddl-auto: create-drop
```

These can lead to unpredictable schema changes or data loss.

---

### 5. Test migrations before production

Run migrations against a staging database that closely matches production, and include migration execution in your CI pipeline to catch SQL errors before deployment.

---

## Example release sequence

Version 1.0:

```
V1__create_users.sql
V2__create_orders.sql
```

Version 1.1:

```
V3__add_order_status.sql
```

Version 1.2:

```
V4__create_payments.sql
V5__add_payment_index.sql
```

When upgrading directly from 1.0 to 1.2, Flyway will automatically execute `V3`, `V4`, and `V5` in order, leaving the database at the expected schema version.

This approach—versioned SQL migrations, `ddl-auto=validate`, immutable migration files, and automated execution during deployment—is the standard pattern for using Spring Boot and Flyway in production.
