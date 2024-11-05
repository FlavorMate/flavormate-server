create table accounts
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1     not null,
    diet             smallint    default 0     not null
        constraint diet_check
            check ((diet >= 0) AND (diet <= 3)),
    display_name     text                      not null,
    last_activity    timestamptz default now() not null,
    mail             text                      not null,
    password         text                      not null,
    username         text                      not null,
    valid            boolean     default false not null,
    avatar           integer
);

alter table accounts
    owner to ${owner};

create table authors
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    account_id       integer               not null
        constraint account_id_fk
            references accounts
            on delete cascade
);

alter table authors
    owner to ${owner};

create table books
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1     not null,
    label            text                      not null,
    visible          boolean     default false not null,
    owner_id         integer                   not null
        constraint owner_id_fk
            references authors
            on delete cascade
);

alter table books
    owner to ${owner};

create table book_subscriber
(
    book_id   integer not null
        constraint book_id_ref
            references books
            on delete cascade,
    author_id integer not null
        constraint author_id_fk
            references authors
            on delete cascade,
    primary key (book_id, author_id)
);

alter table book_subscriber
    owner to ${owner};

create table category_groups
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text                  not null
);

alter table category_groups
    owner to ${owner};

create table categories
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text                  not null,
    group_id         integer               not null
        constraint group_id_fk
            references category_groups
            on delete cascade
);

alter table categories
    owner to ${owner};

create table category_groups_i18n
(
    language          text    not null,
    value             text,
    category_group_id integer not null
        constraint category_group_id_fk
            references category_groups,
    primary key (category_group_id, language)
);

alter table category_groups_i18n
    owner to ${owner};

create table category_i18n
(
    language    text    not null,
    value       text,
    category_id integer not null
        constraint category_id_fk
            references categories
            on delete cascade,
    primary key (category_id, language)
);

alter table category_i18n
    owner to ${owner};

create table roles
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text                  not null
);

alter table roles
    owner to ${owner};

create table account_roles
(
    account_id integer not null
        constraint account_id_fk
            references accounts
            on delete cascade,
    role_id    integer not null
        constraint role_id_fk
            references roles,
    primary key (account_id, role_id)
);

alter table account_roles
    owner to ${owner};

create table servings
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint           not null,
    amount           double precision not null,
    label            text             not null
);

alter table servings
    owner to ${owner};

create table recipes
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    cook_time        numeric(21)           not null,
    course           text                  not null
        constraint recipes_course_check
            check ((course)::text = ANY
                   ((ARRAY ['Appetizer'::character varying, 'MainDish'::character varying, 'Dessert'::character varying, 'SideDish'::character varying, 'Bakery'::character varying, 'Drink'::character varying])::text[])),
    description      text,
    diet             text                  not null
        constraint recipes_diet_check
            check ((diet)::text = ANY
                   ((ARRAY ['Meat'::character varying, 'Fish'::character varying, 'Vegetarian'::character varying, 'Vegan'::character varying])::text[])),
    label            text                  not null,
    prep_time        numeric(21)           not null,
    rating           double precision      not null,
    rest_time        numeric(21)           not null,
    url              text,
    author_id        integer               not null
        constraint author_id_fk
            references authors
            on delete cascade,
    serving_id       bigint                not null
        constraint serving_id_fk
            references servings
);

alter table recipes
    owner to ${owner};

create table book_recipe
(
    book_id   integer not null
        constraint book_id_fk
            references books
            on delete cascade,
    recipe_id bigint  not null
        constraint recipe_id_fk
            references recipes
            on delete cascade,
    primary key (book_id, recipe_id)
);

alter table book_recipe
    owner to ${owner};

create table category_recipe
(
    category_id integer not null
        constraint category_id_fk
            references categories
            on delete cascade,
    recipe_id   integer not null
        constraint recipe_id_fk
            references recipes
            on delete cascade,
    primary key (category_id, recipe_id)
);

alter table category_recipe
    owner to ${owner};

create table files
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    category         text                  not null
        constraint files_category_check
            check ((category)::text = ANY
                   ((ARRAY ['ACCOUNT'::character varying, 'AUTHOR'::character varying, 'RECIPE'::character varying])::text[])),
    owner            bigint                not null,
    type             text                  not null
        constraint files_type_check
            check ((type)::text = 'IMAGE'::text),
    recipe_id        integer
        constraint recipe_id_fk
            references recipes
            on delete cascade
);

alter table files
    owner to ${owner};

alter table accounts
    add constraint avatar_fk
        foreign key (avatar) references files
            on delete set null;

create table highlights
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    date             date                  not null,
    diet             text
        constraint highlights_diet_check
            check ((diet)::text = ANY
                   ((ARRAY ['Meat'::character varying, 'Fish'::character varying, 'Vegetarian'::character varying, 'Vegan'::character varying])::text[])),
    recipe_id        integer               not null
        constraint recipe_id_fk
            references recipes
            on delete cascade
);

alter table highlights
    owner to ${owner};

create table ingredient_groups
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text,
    recipe_id        bigint
        constraint recipe_id_fk
            references recipes
            on delete cascade
);

alter table ingredient_groups
    owner to ${owner};

create table instruction_groups
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text,
    recipe_id        bigint
        constraint recipe_id_fk
            references recipes
            on delete cascade
);

alter table instruction_groups
    owner to ${owner};

create table instructions
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text                  not null,
    group_id         bigint
        constraint group_id_fk
            references instruction_groups
            on delete cascade
);

alter table instructions
    owner to ${owner};

create index idx_course
    on recipes (course);

create table stories
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    content          text                  not null,
    label            text                  not null,
    recipe_id        bigint                not null
        constraint recipe_id_fk
            references recipes
            on delete cascade
);

alter table stories
    owner to ${owner};

create table tags
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text                  not null
);

alter table tags
    owner to ${owner};

create table tag_recipe
(
    tag_id    bigint not null
        constraint tag_id_fk
            references tags
            on delete cascade,
    recipe_id bigint not null
        constraint recipe_id_fk
            references recipes
            on delete cascade,
    primary key (tag_id, recipe_id)
);

alter table tag_recipe
    owner to ${owner};

create table tokens
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    token            text                  not null,
    type             text
        constraint tokens_type_check
            check ((type)::text = 'PASSWORD'::text),
    valid_for        numeric(21),
    owner_id         bigint                not null
        constraint owner_id_fk
            references accounts
            on delete cascade
);

alter table tokens
    owner to ${owner};

create table units
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    label            text                  not null
);

alter table units
    owner to ${owner};

create table ingredients
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    amount           double precision      not null,
    label            text                  not null,
    unit_id          integer
        constraint unit_id_fk
            references units,
    group_id         integer
        constraint group_id_fk
            references ingredient_groups
            on delete cascade
);

alter table ingredients
    owner to ${owner};

