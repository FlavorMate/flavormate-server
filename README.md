# FlavorMate

This is the Project for the FlavorMate backend, which is written in Java with Spring Boot.

## Getting Started

### Docker

1. Create a `docker-compose.yaml`-file (or download one from the [examples](./example))
2. Create the folders the container mounts.
3. Create a `secret.key`-file with `openssl rand -hex 64 > secret.key` and copy it into the right folder.
4. Download the [.env.template](./example/.env.template)-file and rename it to `.env`.
5. Enter your details in the `.env`-file
6. Start your container with `docker compose up -d --remove-orphans`

### Barebone

You must have these dependencies installed:

- Postgresql
- Java 21

1. Download the latest [FlavorMate-Server.jar]().
2. Create a `secret.key`-file with `openssl rand -hex 64 > secret.key` and copy it into the right folder.
3. Download the [.env.template](./example/.env.template)-file and rename it to `.env`.
4. Enter your details in the `.env`-file
5. Export your `.env`-file
6. Start the backend with
   ` java -jar -Dspring.profiles.active=release FlavorMate-Server.jar`.

## Environment Variables

| Key                             | Required | Description                                                                                                                     | Example                               | Default                               |
|---------------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------|---------------------------------------|
| FLAVORMATE_DATA_PATH            | No       | Path where files (e.g. recipe pictures) are saved                                                                               | `file:${user.home}/.flavormate/files` | `file:${user.home}/.flavormate/files` |
| FLAVORMATE_PORT                 | No       | Port the server runs inside the container                                                                                       | `8095`                                | `8095`                                |
| FLAVORMATE_HIGHLIGHT_COUNT      | No       | Amount of highlights getting generated                                                                                          | `14`                                  | `14`                                  |
| FLAVORMATE_PATH                 | No       | The path the server uses. Useful when hosting frontend and backend on the same url                                              | `/api`                                |                                       |                                     |
| FLAVORMATE_JWT_TOKEN            | No       | The path where the `secret.key`-file is saved                                                                                   | `/opt/app/secret.key`                 | `/opt/app/secret.key`                 |
| FLAVORMATE_BACKEND_URL          | Yes      | The URL the server is running on. Including the port if it is non standard                                                      | `http://localhost:8095`               |                                       |
| FLAVORMATE_FRONTEND_URL         | No       | Is only needed for links inside mails (e.g. password reset). [WebApp](https://github.com/FlavorMate/flavormate-app) is required | `https://app.flavormate.de`           |                                       |
| FLAVORMATE_ADMIN_USERNAME       | Yes      | Username for the admin account                                                                                                  | `admin`                               |                                       |
| FLAVORMATE_ADMIN_DISPLAYNAME    | Yes      | Display name for the admin account                                                                                              | `Administrator`                       |                                       |
| FLAVORMATE_ADMIN_MAIL           | Yes      | Mail address for the admin account                                                                                              | `example@localhost.de`                |                                       |
| FLAVORMATE_ADMIN_PASSWORD       | Yes      | Password for the admin account                                                                                                  | `Passw0rd!`                           |                                       |
| FLAVORMATE_FEATURE_STORY        | No       | Enables the functionality to create and view stories                                                                            | `true`                                | `true`                                |
| FLAVORMATE_FEATURE_REGISTRATION | No       | Allows the user to sign up. An admin has to activate the user.                                                                  | `true`                                | `false`                               |
| FLAVORMATE_FEATURE_RECOVERY     | No       | Allows the user to reset its password. (Mail config is required!)                                                               | `true`                                | `false`                               |
| DB_HOST                         | Yes      | Host address for the postgres database                                                                                          | `localhost:5432`                      |                                       |
| DB_USER                         | Yes      | User for the postgres database                                                                                                  | `flavormate`                          |                                       |
| DB_PASSWORD                     | Yes      | Password for the postgres database                                                                                              | `Passw0rd!`                           |                                       |
| DB_DATABASE                     | Yes      | Database name for the postgres database                                                                                         | `flavormate`                          |                                       |
| MAIL_FROM                       | No       | Mail From header                                                                                                                | `FlavorMate <noreply@example.de>`     |                                       |       
| MAIL_HOST                       | No       | Mail host                                                                                                                       | `smtp.example.com`                    |                                       |
| MAIL_PORT                       | No       | Mail port                                                                                                                       | `465`                                 |                                       |
| MAIL_USERNAME                   | No       | Mail user                                                                                                                       | `noreply@example.com`                 |                                       |
| MAIL_PASSWORD                   | No       | Mail password                                                                                                                   | `Passw0rd!`                           |                                       |
| MAIL_STARTTLS                   | No       | Use StartTLS?                                                                                                                   | `true`                                |                                       |
