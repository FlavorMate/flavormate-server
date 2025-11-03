create table public.v3__account
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    username         text                      NOT NULL,
    display_name     text                      NOT NULL,
    password         text                      NOT NULL,
    enabled          bool        DEFAULT false NOT NULL,
    verified         bool        DEFAULT false NOT NULL,
    diet             text                      NOT NULL,
    email            text                      NOT NULL,
    first_login      bool        DEFAULT true  NOT NULL,
    avatar           text,
    created_on       timestamptz DEFAULT now() NOT NULL,
    last_modified_on timestamptz DEFAULT now() NOT NULL,
    version          bigint      DEFAULT 1     NOT NULL,
    owned_by         text
);

create table public.v3__book
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    label            text                      NOT NULL,
    visible          bool        DEFAULT false NOT NULL,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1     NOT NULL,
    owned_by         text                      NOT NULL,
    cover_recipe     text
);

create table v3__book__subscriber
(
    account_id text NOT NULL,
    book_id    text NOT NULL,
    PRIMARY KEY (account_id, book_id)
);

create table public.v3__category_group
(
    id    text DEFAULT gen_random_uuid() PRIMARY KEY,
    label text NOT NULL
);

create table public.v3__category_group__l10n
(
    language          text NOT NULL,
    value             text NOT NULL,
    category_group_id text NOT NULL,
    PRIMARY KEY (category_group_id, language)
);

create table public.v3__category
(
    id           text DEFAULT gen_random_uuid() PRIMARY KEY,
    label        text NOT NULL,
    group_id     text NOT NULL,
    cover_recipe text
);

create table public.v3__category__l10n
(
    language    text NOT NULL,
    value       text NOT NULL,
    category_id text NOT NULL,
    PRIMARY KEY (category_id, language)
);

create table public.v3__role
(
    id    text DEFAULT gen_random_uuid() PRIMARY KEY,
    value text NOT NULL
);

create table public.v3__account__role
(
    account_id text NOT NULL,
    role_id    text NOT NULL,
    PRIMARY KEY (account_id, role_id)
);

create table public.v3__recipe__serving
(
    id     text DEFAULT gen_random_uuid() PRIMARY KEY,
    amount real NOT NULL,
    label  text NOT NULL
);

create table public.v3__recipe
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    created_on       timestamptz DEFAULT now() NOT NULL,
    last_modified_on timestamptz DEFAULT now() NOT NULL,
    version          bigint      DEFAULT 1     NOT NULL,
    owned_by         text                      NOT NULL,

    cook_time        interval    DEFAULT 'PT0' NOT NULL,
    course           text                      NOT NULL,
    description      text,
    diet             text                      NOT NULL,
    label            text                      NOT NULL,
    prep_time        interval    DEFAULT 'PT0' NOT NULL,
    rest_time        interval    DEFAULT 'PT0' NOT NULL,
    serving_id       text                      NOT NULL,
    url              text,
    cover_file       text
);

create table public.v3__recipe_draft
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    created_on       timestamptz DEFAULT now() NOT NULL,
    last_modified_on timestamptz DEFAULT now() NOT NULL,
    version          bigint      DEFAULT 1     NOT NULL,
    owned_by         text                      NOT NULL,

    cook_time        interval    DEFAULT 'PT0' NOT NULL,
    course           text,
    description      text,
    diet             text,
    label            text,
    prep_time        interval    DEFAULT 'PT0' NOT NULL,
    rest_time        interval    DEFAULT 'PT0' NOT NULL,
    serving_id       text                      NOT NULL,
    tags             text[]      DEFAULT '{}'  NOT NULL,
    url              text,
    origin_id        text
);

create table public.v3__recipe_draft__serving
(
    id     text DEFAULT gen_random_uuid() PRIMARY KEY,
    amount real,
    label  text
);

create table public.v3__recipe_draft__ingredient_group
(
    id              text DEFAULT gen_random_uuid() PRIMARY KEY,
    label           text,
    index           int  NOT NULL,
    recipe_draft_id text NOT NULL
);

create table public.v3__recipe_draft__ingredient_group__item
(
    id           text DEFAULT gen_random_uuid() PRIMARY KEY,
    index        int  NOT NULL,
    amount       real,
    label        text,
    unit         text,
    group_id     text NOT NULL,
    nutrition_id text
);


create table public.v3__recipe_draft__instruction_group
(
    id              text DEFAULT gen_random_uuid() PRIMARY KEY,
    label           text,
    index           int  NOT NULL,
    recipe_draft_id text NOT NULL
);

create table public.v3__recipe_draft__instruction_group__item
(
    id       text DEFAULT gen_random_uuid() PRIMARY KEY,
    index    int  NOT NULL,
    label    text,
    group_id text NOT NULL
);

create table public.v3__recipe__rating
(
    account_id text NOT NULL,
    recipe_id  text NOT NULL,
    rating     real NOT NULL,
    PRIMARY KEY (account_id, recipe_id)
);

create table public.v3__book__recipe
(
    book_id   text NOT NULL,
    recipe_id text NOT NULL,
    PRIMARY KEY (book_id, recipe_id)
);

create table public.v3__category__recipe
(
    category_id text NOT NULL,
    recipe_id   text NOT NULL,
    PRIMARY KEY (category_id, recipe_id)
);

create table public.v3__category__recipe_draft
(
    category_id     text NOT NULL,
    recipe_draft_id text NOT NULL,
    PRIMARY KEY (category_id, recipe_draft_id)
);

create table public.v3__account__file
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    mime_type        text                  NOT NULL,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    owned_by         text                  NOT NULL
);

create table public.v3__recipe__file
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    mime_type        text                  NOT NULL,
    recipe_id        text                  NOT NULL,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    owned_by         text                  NOT NULL
);

create table public.v3__recipe_draft__file
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    mime_type        text                  NOT NULL,
    recipe_draft_id  text                  NOT NULL,
    origin_id        text,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    owned_by         text                  NOT NULL
);

create table public.v3__highlight
(
    id        text DEFAULT gen_random_uuid() PRIMARY KEY,
    date      date NOT NULL,
    diet      text NOT NULL,
    recipe_id text NOT NULL
);

create table public.v3__recipe__ingredient_group
(
    id        text DEFAULT gen_random_uuid() PRIMARY KEY,
    label     text,
    index     int  NOT NULL,
    recipe_id text NOT NULL
);

create table public.v3__recipe__ingredient_group__item
(
    id           text DEFAULT gen_random_uuid() PRIMARY KEY,
    index        int  NOT NULL,
    amount       real,
    label        text NOT NULL,
    unit         text,
    group_id     text NOT NULL,
    nutrition_id text
);


create table public.v3__recipe__instruction_group
(
    id        text DEFAULT gen_random_uuid() PRIMARY KEY,
    label     text,
    index     int  NOT NULL,
    recipe_id text NOT NULL
);

create table public.v3__recipe__instruction_group__item
(
    id       text DEFAULT gen_random_uuid() PRIMARY KEY,
    index    int  NOT NULL,
    label    text NOT NULL,
    group_id text NOT NULL
);

create table public.v3__recipe_draft__ingredient_group__item__nutrition
(
    id                 text DEFAULT gen_random_uuid() PRIMARY KEY,
    open_food_facts_id text,
    carbohydrates      real,
    energy_kcal        real,
    fat                real,
    saturated_fat      real,
    sugars             real,
    fiber              real,
    proteins           real,
    salt               real,
    sodium             real
);

create table public.v3__story
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    content          text                  NOT NULL,
    label            text                  NOT NULL,
    recipe_id        text                  NOT NULL,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    owned_by         text                  NOT NULL
);

create table public.v3__story_draft
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    content          text,
    label            text,
    recipe           text,
    origin_id        text,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    owned_by         text                  NOT NULL
);

create table public.v3__tag
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    label            text                  NOT NULL,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    cover_recipe     text
);

create table public.v3__tag__recipe
(
    tag_id    text NOT NULL,
    recipe_id text NOT NULL,
    PRIMARY KEY (tag_id, recipe_id)
);

create table public.v3__token
(
    id               text    DEFAULT gen_random_uuid() PRIMARY KEY,
    issued_at        timestamptz           NOT NULL,
    expired_at       timestamptz,
    uses             bigint  DEFAULT 0     NOT NULL,
    revoked          boolean DEFAULT false NOT NULL,
    type             text                  NOT NULL,
    secured_resource text                  NOT NULL,
    owned_by         text
);

create table public.v3__unit_ref
(
    id          text DEFAULT gen_random_uuid() PRIMARY KEY,
    description text NOT NULL
);

create table public.v3__unit_l10n
(
    id            text DEFAULT gen_random_uuid() PRIMARY KEY,
    unit_ref      text NOT NULL,
    language      text NOT NULL,
    label_sg      text NOT NULL,
    label_sg_abrv text,
    label_pl      text,
    label_pl_abrv text
);

create table public.v3__unit_conversion
(
    source text NOT NULL,
    target text NOT NULL,
    factor real NOT NULL,
    PRIMARY KEY (source, target)
);


create table public.v3__recipe__ingredient_group__item__nutrition
(
    id                 text DEFAULT gen_random_uuid() PRIMARY KEY,
    open_food_facts_id text,
    carbohydrates      real,
    energy_kcal        real,
    fat                real,
    saturated_fat      real,
    sugars             real,
    fiber              real,
    proteins           real,
    salt               real,
    sodium             real
);


create table public.v3__ext__url_shortener
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    created_on       timestamptz DEFAULT now(),
    last_modified_on timestamptz DEFAULT now(),
    version          bigint      DEFAULT 1 NOT NULL,
    short_path       text                  NOT NULL,
    original_path    text                  NOT NULL
);

create table public.v3__ext__auth__oidc_mapping
(
    issuer     text not null,
    subject    text not null,
    account_id text not null
);

create table public.v3__ext__off__product
(
    id            text               not null primary key,
    carbohydrates real,
    energy_kcal   real,
    fat           real,
    saturated_fat real,
    sugars        real,
    fiber         real,
    proteins      real,
    salt          real,
    sodium        real,
    state         text DEFAULT 'New' not null
);

create table public.v3__ext__off__api_usage
(
    id        text        DEFAULT gen_random_uuid() primary key,
    timestamp timestamptz DEFAULT now() not null,
    requests  integer     DEFAULT 0     not null

);

