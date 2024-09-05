# API de prueba técnica  BCI
- Autor: Carlos Lozano
- Compañia: Globallogic

## Descripción de la API

### Descripción
Esta API proporciona servicios para gestionar usuarios.


### Base Path
localhost:8080/api

### Endpoints

| Operation | Método | Path           |
|-----------|--------|----------------|
| Sign Up   | POST   | /users/sign-up |
| Login     | POST   | /users/login   |

### Colección de Postman
la colección de postman que se usó para probar la API se encuentra en: `src/main/resources/postman/BCI technical test.postman_collection.json` 

### Diagrama de Secuencia
![Diagrama de la solución](src/main/resources/diagrams/sequence-diagram.png)

###  Correr la aplicación

- En una terminal bash ubicarse en la carpeta root del proyecto y ejecutar el siguiente comando

```bash
./gradlew bootRun
```

## Base de datos
El microservicio está configurado para trabajar con la base de datos H2 

Para conectarse a la consola de H2 ejecute la aplicación e ingrese a la siguiente URL: http://localhost:8080/api/h2-console y utilice los siguientes parámetros:

- JDBC URL: jdbc:h2:mem:test
- Username: sa
- Password: (Sin Contraseña)


## Ejemplos

### 1. sign up
- **Descripción**: Crea un nuevo usuario en la base de datos.
- **Endpoint**: `POST localhost:8080/api/users/sign-up`
- **Body Request de Ejemplo**:
  ```json
  {
    "name": "Dummy Martinez",
    "email": "dummy@martinez.org",
    "password": "Hunter123*",
    "phones": [
                {
                "number": "74528",
                "cityCode": "2",
                "countryCode": "58"
                }
        ]    
   }
  ```

- **Body Response de Ejemplo**:
  ```json
  {
    "id": "fd9f942e-4de5-44c2-9c0c-4d72af7d9927",
    "created": "2024-09-05T01:15:24.762445",
    "lastLogin": "2024-09-05T01:15:24.762475",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkdW1teUBtYXJ0aW5lei5vcmciLCJleHAiOjE3MjU1NTI5MjQsImlhdCI6MTcyNTUxNjkyNH0.zvllpkvD7yCd3P-qwi6HSgmQd1PoLIPX4BDetDNHaMU",
    "isActive": true
  }
  ```

### 1.1 sign up con email no válido
- **Descripción**: Error por email no válido.
- **Endpoint**: `POST localhost:8080/api/users/sign-up`
- **Body Request de Ejemplo**:
  ```json
  {
    "name": "Dummy Martinez",
    "email": "dummymartinez.org",
    "password": "Hunter123*",
    "phones": [
                {
                "number": "74528",
                "cityCode": "2",
                "countryCode": "58"
                }
        ]    
   }
  ```

- **Body Response de Ejemplo**:
  ```json
  {
    "code": 400,
    "reason": "Bad Request",
    "messages": [
        "default message [Correo no válido]]"
    ],
    "date": "2024-09-05 01:26:58",
    "path": "/api/users/sign-up"
  }
  ```

### 1.2 sign up con email no duplicado
- **Descripción**: Error por email duplicado.
- **Endpoint**: `POST localhost:8080/api/users/sign-up`
- **Body Request de Ejemplo**:
  ```json
  {
    "name": "Dummy Martinez",
    "email": "dummy@martinez.org",
    "password": "Hunter123*",
    "phones": [
                {
                "number": "74528",
                "cityCode": "2",
                "countryCode": "58"
                }
        ]    
   }
  ```

- **Body Response de Ejemplo**:
  ```json
  {
    "code": 500,
    "reason": "Internal Server Error",
    "messages": [
        "Unique index or primary key violation: 'Correo ya resgistrado'"
    ],
    "date": "2024-09-05 01:27:47",
    "path": "/api/users/sign-up"
  }
  ```
  ### 1.2 sign up con contraseña no válida
- **Descripción**: Error cuando la contraseña no cumple los requisitos.
- **Endpoint**: `POST localhost:8080/api/users/sign-up`
- **Body Request de Ejemplo**:
  ```json
  {
    "name": "Dummy Martinez",
    "email": "dummy@martinez.org",
    "password": "Hun",
    "phones": [
                {
                "number": "74528",
                "cityCode": "2",
                "countryCode": "58"
                }
        ]    
   }
  ```

- **Body Response de Ejemplo**:
  ```json
  {
    "code": 400,
    "reason": "Bad Request",
    "messages": [
        "default message [Contraseña no cumple con los requisitos]]"
    ],
    "date": "2024-09-05 01:28:43",
    "path": "/api/users/sign-up"
  }
  ```


### 2. login
- **Descripción**: Consulta un usuario en la base de datos y valida el token.
- **Endpoint**: `POST localhost:8080/api/users/login`
- **Header**:  Authorization: Bearer (JWT generado en el endpoint de sign-up)
- **Body Request de Ejemplo**:
  ```json
  {
    "email": "dummy@martinez.org",
    "password":"Hunter123*"
   }
  ```

- **Body Response de Ejemplo**:
  ```json
  {
    "id": "6f23c9ce-d7f7-41be-8029-28628c5b7cca",
    "created": "2024-09-05T01:06:03.290063",
    "lastLogin": "2024-09-05T01:06:03.29008",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkdW1teUBtYXJ0aW5lei5vcmciLCJleHAiOjE3MjU1NTIzNjMsImlhdCI6MTcyNTUxNjM2M30.VgstMYjYetT_2mfbEWl6Z1cZ7nWlfv_WCDFdwWu3mW4",
    "isActive": true,
    "name": "Dummy Martinez",
    "email": "dummy@martinez.org",
    "password": "$2a$10$.FZFNk26c8IO6q6KcEhyXOsIfPAb4p8jDiXk6X8.FQ5oYEPGfamWi",
    "phones": [
        {
            "number": 74528,
            "cityCode": 2,
            "countryCode": "58"
        }
    ]
  }
  ```

### 3. login con JWT no válido
- **Descripción**: Consulta un usuario en la base de datos y valida el token.
- **Endpoint**: `POST localhost:8080/api/users/login`
- **Header**:  Authorization: Bearer (JWT no válido)
- **Body Request de Ejemplo**:
  ```json
  {
    "email": "dummy@martinez.org",
    "password":"Hunter123*"
   }
  ```

- **Body Response de Ejemplo**:
  ```json
  {
    "timestamp": "2024-09-05T06:25:15.857+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/api/users/login"
  }
  ```


