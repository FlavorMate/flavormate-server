create table accounts
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint                      not null,
    diet             smallint                    not null
        constraint accounts_diet_check
            check ((diet >= 0) AND (diet <= 3)),
    display_name     varchar(255)                not null,
    last_activity    timestamp(6) with time zone not null,
    mail             varchar(255),
    password         varchar(255)                not null,
    username         varchar(255)                not null,
    valid            boolean                     not null,
    avatar           bigint
);

alter table accounts
    owner to cookbook;

create table authors
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint not null,
    account_id       bigint not null
        constraint ukinntd6uw2p90aq5jlnxn7q0jh
            unique
        constraint fkb8jf1wsms2gi6nig6kyfxay1w
            references accounts
            on delete cascade
);

alter table authors
    owner to cookbook;

create table books
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    label            varchar(255) not null,
    visible          boolean      not null,
    owner_id         bigint       not null
        constraint fk3b4x801cccjrpndyqwkdr25cb
            references authors
            on delete cascade
);

alter table books
    owner to cookbook;

create table book_subscriber
(
    book_id   bigint not null
        constraint fkplg9n1jf4502upnpulf7crnsl
            references books,
    author_id bigint not null
        constraint fktou4415h3ibx9v4i3hr289fw4
            references authors
            on delete set null
);

alter table book_subscriber
    owner to cookbook;

create table category_groups
(
    id               bigint       not null
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    label            varchar(255) not null
);

alter table category_groups
    owner to cookbook;

create table categories
(
    id               bigint       not null
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    label            varchar(255) not null,
    group_id         bigint       not null
        constraint fkru1cyrjmgqtp5ivha5lhhhb4t
            references category_groups
);

alter table categories
    owner to cookbook;

create table category_groups_i18n
(
    language          varchar(255) not null,
    value             varchar(255),
    category_group_id bigint       not null
        constraint fk1bjv449ylkeb15u5q5rtsr9c6
            references category_groups,
    primary key (category_group_id, language)
);

alter table category_groups_i18n
    owner to cookbook;

create table category_i18n
(
    language    varchar(255) not null,
    value       varchar(255),
    category_id bigint       not null
        constraint fkhldehd6hr5c1d4dja0496k9le
            references categories,
    primary key (category_id, language)
);

alter table category_i18n
    owner to cookbook;

create table roles
(
    id               bigint       not null
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    label            varchar(255) not null
);

alter table roles
    owner to cookbook;

create table account_roles
(
    account_id bigint not null
        constraint fk61h48dsir3h82pxbq3cwgp0ce
            references accounts
            on delete cascade,
    role_id    bigint not null
        constraint fk6r8nxkn3hctohyllteivfr5hy
            references roles
);

alter table account_roles
    owner to cookbook;

create table servings
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint           not null,
    amount           double precision not null,
    label            varchar(255)     not null
);

alter table servings
    owner to cookbook;

create table recipes
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint           not null,
    cook_time        numeric(21)      not null,
    course           varchar(255)     not null
        constraint recipes_course_check
            check ((course)::text = ANY
                   ((ARRAY ['Appetizer'::character varying, 'MainDish'::character varying, 'Dessert'::character varying, 'SideDish'::character varying, 'Bakery'::character varying, 'Drink'::character varying])::text[])),
    description      varchar(255),
    diet             varchar(255)     not null
        constraint recipes_diet_check
            check ((diet)::text = ANY
                   ((ARRAY ['Meat'::character varying, 'Fish'::character varying, 'Vegetarian'::character varying, 'Vegan'::character varying])::text[])),
    label            varchar(255)     not null,
    prep_time        numeric(21)      not null,
    rating           double precision not null,
    rest_time        numeric(21)      not null,
    url              varchar(255),
    author_id        bigint           not null
        constraint fkm64mc6jhf9ohl3adwib8hfgk2
            references authors
            on delete cascade,
    serving_id       bigint           not null
        constraint fk9d2j24s025s9alnwvpiuux9q1
            references servings
);

alter table recipes
    owner to cookbook;

create table book_recipe
(
    book_id   bigint not null
        constraint fkimgdi4wbthl5cy4yyj1l4ttv7
            references books,
    recipe_id bigint not null
        constraint fkess8x268p4wa5obne3y6footf
            references recipes
            on delete cascade
);

alter table book_recipe
    owner to cookbook;

create table category_recipe
(
    category_id bigint not null
        constraint fkfd7gywqyi664i8jxr22axijwm
            references categories,
    recipe_id   bigint not null
        constraint fkih3r1312kx7njohfqbhudvq6c
            references recipes
            on delete cascade
);

alter table category_recipe
    owner to cookbook;

create table files
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    category         varchar(255) not null
        constraint files_category_check
            check ((category)::text = ANY
                   ((ARRAY ['ACCOUNT'::character varying, 'AUTHOR'::character varying, 'RECIPE'::character varying])::text[])),
    owner            bigint       not null,
    type             varchar(255) not null
        constraint files_type_check
            check ((type)::text = 'IMAGE'::text),
    recipe_id        bigint
        constraint fkhkbcgo73xdix60sq61uvsrqc7
            references recipes
);

alter table files
    owner to cookbook;

alter table accounts
    add constraint fk2plw5lmm66i5lghcbi04sqpg5
        foreign key (avatar) references files
            on delete set null;

create table highlights
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint not null,
    date             date   not null,
    diet             varchar(255)
        constraint highlights_diet_check
            check ((diet)::text = ANY
                   ((ARRAY ['Meat'::character varying, 'Fish'::character varying, 'Vegetarian'::character varying, 'Vegan'::character varying])::text[])),
    recipe_id        bigint not null
        constraint fkms4nrilovhbpnf2k5pjrio9mk
            references recipes
            on delete cascade
);

alter table highlights
    owner to cookbook;

create table ingredient_groups
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint not null,
    label            varchar(255),
    recipe_id        bigint
        constraint fkg0jkfbako110l1s60wn0j1mbr
            references recipes
);

alter table ingredient_groups
    owner to cookbook;

create table instruction_groups
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint not null,
    label            varchar(255),
    recipe_id        bigint
        constraint fkpm4dndk3rd9e4uermp7bwvbs3
            references recipes
);

alter table instruction_groups
    owner to cookbook;

create table instructions
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint not null,
    label            text   not null,
    group_id         bigint
        constraint fkocl0ssmebm17huf9e0lf2dcdo
            references instruction_groups
);

alter table instructions
    owner to cookbook;

create index idx_course
    on recipes (course);

create table stories
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint not null,
    content          text   not null,
    label            text   not null,
    recipe_id        bigint not null
        constraint fkc6ioxrvivfenrysm1jkx9ije5
            references recipes
            on delete cascade
);

alter table stories
    owner to cookbook;

create table tags
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    label            varchar(255) not null
);

alter table tags
    owner to cookbook;

create table tag_recipe
(
    tag_id    bigint not null
        constraint fk6o0ymvp02gy61vbnn41kgm1dj
            references tags,
    recipe_id bigint not null
        constraint fkf9yhuygnjkp0m57g8iat3hukv
            references recipes
            on delete cascade
);

alter table tag_recipe
    owner to cookbook;

create table tokens
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    token            varchar(255) not null,
    type             varchar(255)
        constraint tokens_type_check
            check ((type)::text = 'PASSWORD'::text),
    valid_for        numeric(21),
    owner_id         bigint       not null
        constraint fkjkq2syiydqiuvk0c74kr70bhr
            references accounts
);

alter table tokens
    owner to cookbook;

create table units
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint       not null,
    label            varchar(255) not null
);

alter table units
    owner to cookbook;

create table ingredients
(
    id               bigint generated by default as identity
        primary key,
    created_on       timestamp(6) with time zone,
    last_modified_on timestamp(6) with time zone,
    version          bigint           not null,
    amount           double precision not null,
    label            text             not null,
    unit_id          bigint
        constraint fk6f6x9alhbwl1snfjlqur2xgqn
            references units,
    group_id         bigint
        constraint fks62uoq4eoq5m56195jsdmpjn4
            references ingredient_groups
);

alter table ingredients
    owner to cookbook;

