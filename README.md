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
> This is the repository for the FlavorMate backend, which is written in Kotlin with Quarkus.<br>
> For the frontend, please visit [this repository](https://github.com/FlavorMate/flavormate-app).

## Migration Guides

<details>
<summary>3.4.0</summary>

### Environment Changes:

The following properties have been added:

**Database**

|          new property          | required |                         note                          |
|--------------------------------|----------|-------------------------------------------------------|
| `FLAVORMATE_DB_ENCRYPTION_KEY` | yes      | 256 AES Key (base64) used to encrypt database entries |

> Create the `FLAVORMATE_DB_ENCRYPTION_KEY` with `openssl rand -base64 32`
>
> </details>
>
> <details>
> <summary>3.3.0</summary>
>
  ### Environment Changes:

The following properties have been added:

**Auth - JWT - OIDC**

|                  new property                   | required |                                                       note                                                       |
|-------------------------------------------------|----------|------------------------------------------------------------------------------------------------------------------|
| `FLAVORMATE_AUTH_OIDC[*]_CLIENT_SECRET`         | no       | Adds a secret for the given oidc provider                                                                        |
| `FLAVORMATE_AUTH_OIDC[*]_REDIRECT_URI_OVERRIDE` | no       | Replaces the oauth callback address `flavormate://oauth` with `${FLAVORMATE_SERVER_URL}/v3/oidc/mobile-redirect` |

</details>

<details>
<summary>3.2.1</summary>

### Environment Changes:

The following properties have been added:

**Server**

|               new property               | required |                 note                  |
|------------------------------------------|----------|---------------------------------------|
| `FLAVORMATE_SERVER_CORS_ALLOWED_ORIGINS` | no       | a list of allowed origins, `*`for all |

</details>

<details>
<summary>v3.0.* to 3.1.*</summary>

### Environment Changes:

The following properties have been added:

**Features**

|         new property         | required | note |
|------------------------------|----------|------|
| `FLAVORMATE_FEATURE_RATINGS` | No       |      |

</details>

<details>
<summary>v2* to v3</summary>

Version 3 is a complete rewrite of the FlavorMate backend, featuring significant architectural changes:

- The project transitioned from Java with SpringBoot to Kotlin with Quarkus
- The database schema has been optimized and changed
- The API has been completely rewritten

To enable a smooth transition from v2.\* to v3, there is a dedicated migration tool available as both an executable
and Docker container [in this repository](https://github.com/FlavorMate/flavormate-migrator).

> [!TIP]
> This process is **non-destructive**.
> Your old data remains unchanged and can be used as a backup.

After performing the data migration, fully described in the other repo, the configurations of this backend have to be
changed.

1. Migrate your data using the migration tool (detailed instructions are available in
   the [FlavorMate migrator repository](https://github.com/FlavorMate/flavormate-migrator))
2. Update your environment configuration according to the changes outlined below

### Environment Changes:

The following properties have been changed:

<details open>
<summary>Server</summary>

|      Old property       |      New Property      | required |                                           Notes                                            |
|-------------------------|------------------------|----------|--------------------------------------------------------------------------------------------|
| FLAVORMATE_BACKEND_URL  | FLAVORMATE_SERVER_URL  | Yes      | -                                                                                          |
| FLAVORMATE_PORT         | FLAVORMATE_SERVER_PORT | -        | -                                                                                          |
| FLAVORMATE_PATH         | FLAVORMATE_SERVER_PATH | -        | -                                                                                          |
| FLAVORMATE_FRONTEND_URL | -                      | -        | Not required anymore since the web application should register the `flavormate://` schema. |

</details>

<details open>
<summary>General</summary>

|        Old property        |                  New Property                  | required |                                           Notes                                            |
|----------------------------|------------------------------------------------|----------|--------------------------------------------------------------------------------------------|
| FLAVORMATE_HIGHLIGHT_COUNT | FLAVORMATE_GENERAL_HIGHLIGHTS_DAYS_TO_GENERATE | -        | -                                                                                          |
| FLAVORMATE_FRONTEND_URL    | -                                              | -        | Not required anymore since the web application should register the `flavormate://` schema. |
| FLAVORMATE_LANGUAGE        | -                                              | -        | The user sets the language in the request                                                  |

</details>

<details open>
<summary>General - Admin</summary>

|         Old property         |             New Property              | required | Notes |
|------------------------------|---------------------------------------|----------|-------|
| FLAVORMATE_ADMIN_DISPLAYNAME | FLAVORMATE_GENERAL_ADMIN_DISPLAY_NAME | Yes      | -     |
| FLAVORMATE_ADMIN_USERNAME    | FLAVORMATE_GENERAL_ADMIN_USERNAME     | Yes      | -     |
| FLAVORMATE_ADMIN_PASSWORD    | FLAVORMATE_GENERAL_ADMIN_PASSWORD     | Yes      | -     |
| FLAVORMATE_ADMIN_MAIL        | FLAVORMATE_GENERAL_ADMIN_EMAIL        | Yes      |       |

</details>

<details open>
<summary>Database</summary>

| Old property |      New Property      | required | Notes |
|--------------|------------------------|----------|-------|
| DB_HOST      | FLAVORMATE_DB_HOST     | Yes      | -     |
| DB_DATABASE  | FLAVORMATE_DB_DATABASE | Yes      | -     |
| -            | FLAVORMATE_DB_PORT     | Yes      | -     |
| DB_USER      | FLAVORMATE_DB_USER     | Yes      | -     |
| DB_PASSWORD  | FLAVORMATE_DB_PASSWORD | Yes      | -     |

</details>

<details open>
<summary>Paths</summary>

|      Old property       |      New Property      | required |                                      Notes                                       |
|-------------------------|------------------------|----------|----------------------------------------------------------------------------------|
| FLAVORMATE_PATH_CONTENT | FLAVORMATE_PATHS_FILES | Yes      | -                                                                                |
| FLAVORMATE_PATH_BACKUP  | -                      | -        | Wasn't used yet.                                                                 |
| FLAVORMATE_PATH_LOG     | -                      | -        | Was used for logs regarding the migration from V1 to V2 and isn't needed anymore |

</details>

<details open>
<summary>Auth</summary>

|     Old property     |        New Property         | required |             Notes              |
|----------------------|-----------------------------|----------|--------------------------------|
| FLAVORMATE_JWT_TOKEN | -                           | -        | -                              |
| -                    | FLAVORMATE_AUTH_PUBLIC_KEY  | Yes      | Path to the public key (.pem)  |
| -                    | FLAVORMATE_AUTH_PRIVATE_KEY | Yes      | Path to the private key (.pem) |

</details>

<details open>
<summary>Auth - JWT</summary>

|       Old property        |             New Property             | required | Notes |
|---------------------------|--------------------------------------|----------|-------|
| FLAVORMATE_SHARE_DURATION | FLAVORMATE_AUTH_SHARE_TOKEN_DURATION | -        | -     |

</details>

<details open>
<summary>Email</summary>

| Old property  |        New Property         | required |                             Notes                             |
|---------------|-----------------------------|----------|---------------------------------------------------------------|
| MAIL_FROM     | FLAVORMATE_MAILER_FROM      | -        | -                                                             |
| MAIL_HOST     | FLAVORMATE_MAILER_HOST      | -        | -                                                             |
| MAIL_PORT     | FLAVORMATE_MAILER_PORT      | -        | -                                                             |
| MAIL_STARTTLS | FLAVORMATE_MAILER_START_TLS | -        | The signature changed. Please see the full explanation below. |
| -             | FLAVORMATE_MAILER_TLS       | -        | -                                                             |
| MAIL_USERNAME | FLAVORMATE_MAILER_USERNAME  | -        | -                                                             |
| MAIL_PASSWORD | FLAVORMATE_MAILER_PASSWORD  | -        | -                                                             |
| MAIL_AUTH     | -                           | -        | -                                                             |

</details>

</details>

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

1. Create a `docker-compose.yaml` file (or download one from the [examples](./examples))
2. Create the folders the container needs.
3. Create a key pair and copy `publicKey.pem` and `privateKey.pem` into the right folder.

   ```bash
   # Generate the private key
   openssl genrsa -out rsaPrivateKey.pem 2048
   # Generate the public key
   openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
   # Convert the private key to PKCS8 format
   openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
   ```
4. Download the [.env.template](./example/.env.template) file and rename it to `.env`.
5. Enter your details into the `.env` file
   - Create the `FLAVORMATE_DB_ENCRYPTION_KEY` with `openssl rand -base64 32`
6. Start your container with `docker compose up -d --remove-orphans`

</details>

<details>
<summary>Barebone</summary>

You must have these dependencies installed:

- PostgreSQL
- Java 21
- ImageMagick 7+ (with WEBP and other used formats plugins)

1. Download the latest [FlavorMate-Server.jar](https://github.com/FlavorMate/flavormate-server/releases).
2. Create a key pair and copy `publicKey.pem` and `privateKey.pem` into the right folder.

   ```bash
   # Generate the private key
   openssl genrsa -out rsaPrivateKey.pem 2048
   # Generate the public key
   openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
   # Convert the private key to PKCS8 format
   openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
   ```
3. Download the [.env.template](./examples/.env.template) file and rename it to `.env`.
4. Enter your details in the `.env` file
   - Create the `FLAVORMATE_DB_ENCRYPTION_KEY` with `openssl rand -base64 32`
5. Export your `.env` file
6. Start the backend with `java -jar FlavorMate-Server.jar`.

</details>

## Environment Variables

<details>
<summary>Server</summary>

|                  Key                   | Required |                                    Description                                     |          Example           | Default |
|----------------------------------------|----------|------------------------------------------------------------------------------------|----------------------------|---------|
| FLAVORMATE_SERVER_URL                  | Yes      | The url the server is accessible                                                   | `http://flavormate.intern` | -       |
| FLAVORMATE_SERVER_PATH                 | -        | The path the server uses. Useful when hosting frontend and backend on the same url | `/api`                     | `/`     |
| FLAVORMATE_SERVER_PORT                 | -        | The port the server uses                                                           | `8095`                     | `8080`  |
| FLAVORMATE_SERVER_CORS_ALLOWED_ORIGINS | -        | Comma separated list of allowed origins                                            | `http://localhost:3000`    | -       |

</details>

<details>
<summary>General</summary>

|                      Key                       | Required |                         Description                         |       Example       | Default |
|------------------------------------------------|----------|-------------------------------------------------------------|---------------------|---------|
| FLAVORMATE_GENERAL_ADMIN_DISPLAY_NAME          | Yes      | Admin accounts display name                                 | `Admin`             | -       |
| FLAVORMATE_GENERAL_ADMIN_USERNAME              | Yes      | Admin accounts username                                     | `admin`             | -       |
| FLAVORMATE_GENERAL_ADMIN_PASSWORD              | Yes      | Admin accounts password                                     | `Passw0rd!`         | -       |
| FLAVORMATE_GENERAL_ADMIN_EMAIL                 | Yes      | Admin accounts email                                        | `admin@example.com` | -       |
| FLAVORMATE_GENERAL_HIGHLIGHTS_DAYS_TO_GENERATE | -        | The amount of days for which highlights should be generated | `30`                | `14`    |

</details>

<details>
<summary>Database</summary>

|             Key              | Required |                     Description                      |   Example    | Default |
|------------------------------|----------|------------------------------------------------------|--------------|---------|
| FLAVORMATE_DB_HOST           | Yes      | Database host                                        | `localhost`  | -       |
| FLAVORMATE_DB_DATABASE       | Yes      | Database database name                               | `flavormate` | -       |
| FLAVORMATE_DB_PORT           | -        | Database port                                        | `5432`       | `5432`  |
| FLAVORMATE_DB_USER           | Yes      | Database user                                        | `flavormate` | -       |
| FLAVORMATE_DB_PASSWORD       | Yes      | Database user password                               | `Passw0rd!`  | -       |
| FLAVORMATE_DB_ENCRYPTION_KEY | Yes      | 256 AES Key (base64) used to encrypt database values |              | -       |

</details>

<details >
<summary>Paths</summary>

|            Key             | Required |                    Description                    |             Example             |             Default             |
|----------------------------|----------|---------------------------------------------------|---------------------------------|---------------------------------|
| FLAVORMATE_PATHS_FILES     | -        | Path where files (e.g. recipe pictures) are saved | `${HOME}/.flavormate/files`     | `${HOME}/.flavormate/files`     |
| FLAVORMATE_PATHS_PROVIDERS | -        | Path where OIDC images are saved                  | `${HOME}/.flavormate/providers` | `${HOME}/.flavormate/providers` |

</details>

<details>
<summary>Features</summary>

|                Key                 | Required |                                Description                                 | Example | Default |
|------------------------------------|----------|----------------------------------------------------------------------------|---------|---------|
| FLAVORMATE_FEATURE_RATINGS         | -        | Enables recipe ratings                                                     | `true`  | `false` |
| FLAVORMATE_FEATURE_REGISTRATION    | -        | Enables the registration                                                   | `true`  | `false` |
| FLAVORMATE_FEATURE_RECOVERY        | -        | Enables the password recovery feature !!! Mail config is required!!!       | `true`  | `false` |
| FLAVORMATE_FEATURE_STORY           | -        | Enables the story feature                                                  | `true`  | `false` |
| FLAVORMATE_FEATURE_BRING           | -        | Enables the [Bring!](https://www.getbring.com) integration                 | `true`  | `false` |
| FLAVORMATE_FEATURE_OPEN_FOOD_FACTS | -        | Enables the [Open Food Facts](https://world.openfoodfacts.org) integration | `true`  | `false` |
| FLAVORMATE_FEATURE_SHARE           | -        | Enables the ability to share recipes                                       | `true`  | `false` |

</details>

<details>
<summary>Auth - JWT</summary>

|             Key             | Required |                Description                |     Example      | Default |
|-----------------------------|----------|-------------------------------------------|------------------|---------|
| FLAVORMATE_AUTH_PUBLIC_KEY  | Yes      | The path where the public key is located  | `publicKey.pem`  | -       |
| FLAVORMATE_AUTH_PRIVATE_KEY | Yes      | The path where the private key is located | `privateKey.pem` | -       |

</details>

<details>
<summary>Auth - JWT - Tokens</summary>

All values have to be in ISO 8601 Duration format.
Only days, hours, minutes, and seconds are supported.

|                  Key                   | Required |            Description             | Example | Default  |
|----------------------------------------|----------|------------------------------------|---------|----------|
| FLAVORMATE_AUTH_REFRESH_TOKEN_DURATION | -        | The duration the refresh token has | `PT1M`  | `P30D`   |
| FLAVORMATE_AUTH_ACCESS_TOKEN_DURATION  | -        | The duration the access token has  | `PT1M`  | `PT5M`   |
| FLAVORMATE_AUTH_RESET_TOKEN_DURATION   | -        | The duration the reset token has   | `PT1M`  | `PT5M`   |
| FLAVORMATE_AUTH_VERIFY_TOKEN_DURATION  | -        | The duration the verify token has  | `PT1M`  | `PT5M`   |
| FLAVORMATE_AUTH_BRING_TOKEN_DURATION   | -        | The duration the bring token has   | `PT1M`  | `PT5M`   |
| FLAVORMATE_AUTH_SHARE_TOKEN_DURATION   | -        | The duration the share token has   | `PT1M`  | `P1800D` |

</details>

<details>
<summary>Auth - JWT - OIDC</summary>

Please note that only OIDC Providers with the PKCE flow are supported.

Some providers (e.g. Google) require redirect uri override set to `true`, because they only support `http(s)` scheme.

This section represents an array. Please change the index inside the `[]` - starting at 0

|                      Key                      | Required |                                                                   Description                                                                   |                     Example                     | Default |
|-----------------------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|---------|
| FLAVORMATE_AUTH_OIDC[0]_NAME                  | -        | The name the Provider should have                                                                                                               | `Authentik`                                     | -       |
| FLAVORMATE_AUTH_OIDC[0]_ID                    | -        | The id the Provider should have                                                                                                                 | `authentik`                                     | -       |
| FLAVORMATE_AUTH_OIDC[0]_URL                   | -        | The provided openid url                                                                                                                         | `https://example.com/application/o/flavormate/` | -       |
| FLAVORMATE_AUTH_OIDC[0]_CLIENT_ID             | -        | The provided client id                                                                                                                          | `foo-client-id`                                 | -       |
| FLAVORMATE_AUTH_OIDC[0]_CLIENT_SECRET         | -        | The provided client secret                                                                                                                      | `bar-client-secret`                             | -       |
| FLAVORMATE_AUTH_OIDC[0]_ICON                  | -        | The icon that should be displayed. Path is relative to `FLAVORMATE_PATHS_PROVIDERS`                                                             | `authentik.png`                                 | -       |
| FLAVORMATE_AUTH_OIDC[0]_REDIRECT_URI_OVERRIDE | -        | Replaces the oauth callback address `flavormate://oauth` with `${FLAVORMATE_SERVER_URL}/v3/oidc/mobile-redirect` </br> (needed for e.g. Google) | `true`                                          | `false` |

</details>

<details>
<summary>Mail</summary>

|             Key             | Required |             Description              |               Example                |   Default   |
|-----------------------------|----------|--------------------------------------|--------------------------------------|-------------|
| FLAVORMATE_MAILER_FROM      | -        | E-Mail From header                   | `FlavorMate<noreply@example.de>`     | -           |
| FLAVORMATE_MAILER_HOST      | -        | E-Mail host                          | `smtp.example.com`                   | `localhost` |
| FLAVORMATE_MAILER_PORT      | -        | E-Mail port                          | `465`                                | -           |
| FLAVORMATE_MAILER_START_TLS | -        | Which Start-TLS method is used?      | `DISABLED`, `OPTIONAL` or `REQUIRED` | `OPTIONAL`  |
| FLAVORMATE_MAILER_TLS       | -        | Is the connection secured using TLS? | `true`                               | -           |
| FLAVORMATE_MAILER_USERNAME  | -        | E-Mail user                          | `noreply@example.com`                | -           |
| FLAVORMATE_MAILER_PASSWORD  | -        | E-Mail password                      | `Passw0rd!`                          | -           |

</details>

