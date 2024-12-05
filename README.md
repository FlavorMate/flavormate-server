# FlavorMate

<p align="center">
    <img src="docs/logo_transparent.png" alt="FlavorMate logo" height="64px">
</p>

FlavorMate is your personal, self-hosted, open-source recipe management app, available on iOS, macOS, Android, and as a
web application. You can also build it from source for Linux and Windows. Organize your culinary creations by
categorizing and tagging them to suit your needs. Whether you’re crafting a recipe from scratch or importing one from
the web, FlavorMate makes it easy.

Stuck on what to cook or bake? Let FlavorMate inspire you with the Recipe of the Day or choose a dish at random. For
those following vegetarian or vegan lifestyles, simply set your preference in your profile, and you'll receive recipes
tailored just for you.

> [!TIP]
> This is the repository for the FlavorMate backend, which is written in Java with SpringBoot.<br>
> For the frontend, please visit [this repository](https://github.com/FlavorMate/flavormate-app).

## Migration Guides

<details>
<summary>v2.0.* to 2.1.*</summary>

### Environment Changes:

The following properties have been added:

**General:**

|       new property        | required |       note        |
|---------------------------|----------|-------------------|
| FLAVORMATE_SHARE_DURATION | No       | ISO 8601 Duration |

**Features**

|        new property        | required | note |
|----------------------------|----------|------|
| `FLAVORMATE_FEATURE_SHARE` | No       |      |

</details>

<details>
<summary>v1.* to v2.0.*</summary>

### Environment Changes:

The following properties have been CHANGED:

**General:**

|       old property        |     new property      | required |                                                        note                                                         |
|---------------------------|-----------------------|----------|---------------------------------------------------------------------------------------------------------------------|
| -                         | `FLAVORMATE_LANGUAGE` | Yes      | Either `de` or `en`                                                                                                 |
| `FLAVORMATE_FRONTEND_URL` |                       | No       | no longer required                                                                                                  |
| `FLAVORMATE_JWT_TOKEN`    |                       | No       | default is now `file:${user.home}/.flavormate/secret.key`, please change to `file:/opt/app/secret.key` if necessary |

**Paths:**

|      old property      |       new property        | required | note |
|------------------------|---------------------------|----------|------|
| `FLAVORMATE_DATA_PATH` | `FLAVORMATE_PATH_CONTENT` | No       |      |

**Mail**

| old property | new property | required |             note             |
|--------------|--------------|----------|------------------------------|
| `MAIL_FROM`  |              | No       | should only contain the mail |

**Features**

|        old property        | new property | required |          note          |
|----------------------------|--------------|----------|------------------------|
| `FLAVORMATE_FEATURE_STORY` |              | No       | default is now `false` |
| `FLAVORMATE_FEATURE_BRING` |              | No       | default is now `false` |

The following properties have been ADDED:

**General**

|     new property      | required |        note         |
|-----------------------|----------|---------------------|
| `FLAVORMATE_LANGUAGE` | Yes      | Either `de` or `en` |

### Change the docker image

Change the image tag from `ghcr.io/flavormate/flavormate-server:1` to `ghcr.io/flavormate/flavormate-server:2`

### Migrate ingredient units

To support features like the [Open Food Facts](https://world.openfoodfacts.org) integration and unit conversion, a new
unit
system has been implemented. The server will attempt to convert all free-text units to the new system. If a conversion
fails, manual editing of the
recipe will be required to align with the new unit system. Failed conversions will be logged in a separate file for
review. Recipes will continue to function as usual, even if units are not migrated.

</details>

## Getting Started

<details>
<summary>Docker</summary>

1. Create a `docker-compose.yaml` file (or download one from the [examples](./example))
2. Create the folders the container needs.
3. Create a `secret.key` file with `openssl rand -hex 64 > secret.key` and copy it into the right folder.
4. Download the [.env.template](./example/.env.template) file and rename it to `.env`.
5. Enter your details into the `.env` file
6. Start your container with `docker compose up -d --remove-orphans`

</details>

<details>
<summary>Barebone</summary>

You must have these dependencies installed:

- Postgresql
- Java 21

1. Download the latest [FlavorMate-Server.jar](https://github.com/FlavorMate/flavormate-server/releases).
2. Create a `secret.key` file with `openssl rand -hex 64 > secret.key` and copy it into the right folder.
3. Download the [.env.template](./example/.env.template) file and rename it to `.env`.
4. Enter your details in the `.env` file
5. Export your `.env` file
6. Start the backend with `java -jar -Dspring.profiles.active=release FlavorMate-Server.jar`.

</details>

## Environment Variables

<details open>
<summary>General</summary>

|            Key             | Required |                                             Description                                             |           Example           |                  Default                   |
|----------------------------|----------|-----------------------------------------------------------------------------------------------------|-----------------------------|--------------------------------------------|
| FLAVORMATE_PORT            | No       | Port the server runs inside the container                                                           | `8095`                      | `8095`                                     |
| FLAVORMATE_HIGHLIGHT_COUNT | No       | Amount of highlights getting generated                                                              | `14`                        | `14`                                       |
| FLAVORMATE_PATH            | No       | The path the server uses. Useful when hosting frontend and backend on the same url                  | `/api`                      |                                            |
| FLAVORMATE_LANGUAGE        | Yes      | Either `de` or `en`                                                                                 | `de`                        |                                            |
| FLAVORMATE_JWT_TOKEN       | No       | The path where the `secret.key`-file is saved                                                       | `/opt/app/secret.key`       | `file:${user.home}/.flavormate/secret.key` |
| FLAVORMATE_BACKEND_URL     | Yes      | The URL the server is running on. Including the port if it is non standard                          | `http://localhost:8095`     |                                            |
| FLAVORMATE_FRONTEND_URL    | No       | [WebApp](https://github.com/FlavorMate/flavormate-app) is required                                  | `https://app.flavormate.de` |                                            |
| FLAVORMATE_SHARE_DURATION  | No       | Sets the duration a recipe share is valid. If empty a share is valid forever. Use ISO 8601 Duration | `P1M`                       |                                            |

</details>

<details open>
<summary>Features</summary>

|                Key                 | Required |                                Description                                 | Example | Default |
|------------------------------------|----------|----------------------------------------------------------------------------|---------|---------|
| FLAVORMATE_FEATURE_STORY           | No       | Enables the story feature                                                  | `true`  | `false` |
| FLAVORMATE_FEATURE_RECOVERY        | No       | Enables the password recovery feature !!! Mail config is required!!!       | `true`  | `false` |
| FLAVORMATE_FEATURE_REGISTRATION    | No       | Enables the registration                                                   | `true`  | `false` |
| FLAVORMATE_FEATURE_BRING           | No       | Enables the [Bring!](https://www.getbring.com) integration                 | `true`  | `false` |
| FLAVORMATE_FEATURE_OPEN_FOOD_FACTS | No       | Enables the [Open Food Facts](https://world.openfoodfacts.org) integration | `true`  | `false` |
| FLAVORMATE_FEATURE_SHARE           | No       | Enables the ability to share recipes                                       | `true`  | `false` |

</details>

<details open>
<summary>Paths</summary>

|           Key           | Required |                    Description                    |                Example                 |                Default                 |
|-------------------------|----------|---------------------------------------------------|----------------------------------------|----------------------------------------|
| FLAVORMATE_PATH_BACKUP  | No       | Path where backups are temporarily saved          | `file:${user.home}/.flavormate/backup` | `file:${user.home}/.flavormate/backup` |
| FLAVORMATE_PATH_CONTENT | No       | Path where files (e.g. recipe pictures) are saved | `file:${user.home}/.flavormate/files`  | `file:${user.home}/.flavormate/files`  |
| FLAVORMATE_PATH_LOG     | No       | Path where logs are saved                         | `file:${user.home}/.flavormate/logs`   | `file:${user.home}/.flavormate/logs`   |

</details>

<details open>
<summary>Admin account</summary>

|             Key              | Required |            Description             |        Example         | Default |
|------------------------------|----------|------------------------------------|------------------------|---------|
| FLAVORMATE_ADMIN_USERNAME    | Yes      | Username for the admin account     | `admin`                |         |
| FLAVORMATE_ADMIN_DISPLAYNAME | Yes      | Display name for the admin account | `Administrator`        |         |
| FLAVORMATE_ADMIN_MAIL        | Yes      | Mail address for the admin account | `example@localhost.de` |         |
| FLAVORMATE_ADMIN_PASSWORD    | Yes      | Password for the admin account     | `Passw0rd!`            |         |

</details>

<details open>
<summary>Database</summary>

|     Key     | Required |               Description               |     Example      | Default |
|-------------|----------|-----------------------------------------|------------------|---------|
| DB_HOST     | Yes      | Host address for the postgres database  | `localhost:5432` |         |
| DB_USER     | Yes      | User for the postgres database          | `flavormate`     |         |
| DB_PASSWORD | Yes      | Password for the postgres database      | `Passw0rd!`      |         |
| DB_DATABASE | Yes      | Database name for the postgres database | `flavormate`     |         |

</details>

<details open>
<summary>Mail</summary>

|      Key      | Required |       Description       |        Example        | Default |
|---------------|----------|-------------------------|-----------------------|---------|
| MAIL_FROM     | No       | Mail From header        | `noreply@example.de`  |         |
| MAIL_HOST     | No       | Mail host               | `smtp.example.com`    |         |
| MAIL_PORT     | No       | Mail port               | `465`                 | `587`   |
| MAIL_USERNAME | No       | Mail user               | `noreply@example.com` |         |
| MAIL_PASSWORD | No       | Mail password           | `Passw0rd!`           |         |
| MAIL_STARTTLS | No       | Use StartTLS?           | `true`                | `true`  |
| MAIL_AUTH     | No       | Does the mail use auth? | `true`                | `true`  |

</details>

