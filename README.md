# FlavorMate

This is the Project for the FlavorMate backend, which is written in Java with Spring Boot.

## Migrate to V2

### Migrate the .env file

The following properties have been changed:

|        old property        |       new property        | required |             note             |
|----------------------------|---------------------------|----------|------------------------------|
| -                          | `FLAVORMATE_LANGUAGE`     | x        | Either `de` or `en`          |
| `FLAVORMATE_DATA_PATH`     | `FLAVORMATE_PATH_CONTENT` |          |                              |
| `FLAVORMATE_FRONTEND_URL`  |                           |          | no longer required           |
| `MAIL_FROM`                |                           |          | should only contain the mail |
| `FLAVORMATE_FEATURE_STORY` |                           |          | default is now `false`       |
| `FLAVORMATE_FEATURE_BRING` |                           |          | default is now `false`       |

### Migrate image to V2

Change the image tag from `ghcr.io/flavormate/flavormate-server:1` to `ghcr.io/flavormate/flavormate-server:2`

### Migrate ingredient units

To support features like Open Food Facts integration and unit conversion, a new unit system has been implemented. The
server will attempt to convert all free-text units to the new system. If a conversion fails, manual editing of the
recipe will be required to align with the new unit system. Failed conversions will be logged in a separate file for
review. Recipes will continue to function as usual, even if units are not migrated.

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

### General

|            Key             | Required |                                    Description                                     |           Example           |                  Default                   |
|----------------------------|----------|------------------------------------------------------------------------------------|-----------------------------|--------------------------------------------|
| FLAVORMATE_PORT            | No       | Port the server runs inside the container                                          | `8095`                      | `8095`                                     |
| FLAVORMATE_HIGHLIGHT_COUNT | No       | Amount of highlights getting generated                                             | `14`                        | `14`                                       |
| FLAVORMATE_PATH            | No       | The path the server uses. Useful when hosting frontend and backend on the same url | `/api`                      |                                            |
| FLAVORMATE_LANGUAGE        | Yes      | Either `de` or `en`                                                                | `de`                        |                                            |
| FLAVORMATE_JWT_TOKEN       | No       | The path where the `secret.key`-file is saved                                      | `/opt/app/secret.key`       | `file:${user.home}/.flavormate/secret.key` |
| FLAVORMATE_BACKEND_URL     | Yes      | The URL the server is running on. Including the port if it is non standard         | `http://localhost:8095`     |                                            |
| FLAVORMATE_FRONTEND_URL    | No       | [WebApp](https://github.com/FlavorMate/flavormate-app) is required                 | `https://app.flavormate.de` |                                            |

### Features

|                Key                 | Required |                                Description                                 | Example | Default |
|------------------------------------|----------|----------------------------------------------------------------------------|---------|---------|
| FLAVORMATE_FEATURE_STORY           | No       | Enables the story feature                                                  | `true`  | `false` |
| FLAVORMATE_FEATURE_RECOVERY        | No       | Enables the password recovery feature !!! Mail config is required!!!       | `true`  | `false` |
| FLAVORMATE_FEATURE_REGISTRATION    | No       | Enables the registration                                                   | `true`  | `false` |
| FLAVORMATE_FEATURE_BRING           | No       | Enables the [Bring!](https://www.getbring.com) integration                 | `true`  | `false` |
| FLAVORMATE_FEATURE_OPEN-FOOD-FACTS | No       | Enables the [Open Food Facts](https://world.openfoodfacts.org) integration | `true`  | `false` |

### Paths

|           Key           | Required |                    Description                    |                Example                 |                Default                 |
|-------------------------|----------|---------------------------------------------------|----------------------------------------|----------------------------------------|
| FLAVORMATE_PATH_BACKUP  | No       | Path where backups are temporarily saved          | `file:${user.home}/.flavormate/backup` | `file:${user.home}/.flavormate/backup` |
| FLAVORMATE_PATH_CONTENT | No       | Path where files (e.g. recipe pictures) are saved | `file:${user.home}/.flavormate/files`  | `file:${user.home}/.flavormate/files`  |
| FLAVORMATE_PATH_LOG     | No       | Path where logs are saved                         | `file:${user.home}/.flavormate/logs`   | `file:${user.home}/.flavormate/logs`   |

### Admin account

|             Key              | Required |            Description             |        Example         | Default |
|------------------------------|----------|------------------------------------|------------------------|---------|
| FLAVORMATE_ADMIN_USERNAME    | Yes      | Username for the admin account     | `admin`                |         |
| FLAVORMATE_ADMIN_DISPLAYNAME | Yes      | Display name for the admin account | `Administrator`        |         |
| FLAVORMATE_ADMIN_MAIL        | Yes      | Mail address for the admin account | `example@localhost.de` |         |
| FLAVORMATE_ADMIN_PASSWORD    | Yes      | Password for the admin account     | `Passw0rd!`            |         |

### Database

|     Key     | Required |               Description               |     Example      | Default |
|-------------|----------|-----------------------------------------|------------------|---------|
| DB_HOST     | Yes      | Host address for the postgres database  | `localhost:5432` |         |
| DB_USER     | Yes      | User for the postgres database          | `flavormate`     |         |
| DB_PASSWORD | Yes      | Password for the postgres database      | `Passw0rd!`      |         |
| DB_DATABASE | Yes      | Database name for the postgres database | `flavormate`     |         |

### Mail

|      Key      | Required |   Description    |        Example        | Default |
|---------------|----------|------------------|-----------------------|---------|
| MAIL_FROM     | No       | Mail From header | `noreply@example.de`  |         |
| MAIL_HOST     | No       | Mail host        | `smtp.example.com`    |         |
| MAIL_PORT     | No       | Mail port        | `465`                 |         |
| MAIL_USERNAME | No       | Mail user        | `noreply@example.com` |         |
| MAIL_PASSWORD | No       | Mail password    | `Passw0rd!`           |         |
| MAIL_STARTTLS | No       | Use StartTLS?    | `true`                |         |

