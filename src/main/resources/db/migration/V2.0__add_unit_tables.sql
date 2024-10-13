create table public.unit_refs
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,
    description      text                  not null
);

alter table public.unit_refs
    owner to ${owner};


create table public.unit_localizations
(
    id               serial primary key,
    created_on       timestamptz default now(),
    last_modified_on timestamptz default now(),
    version          bigint      default 1 not null,

    unit_ref         integer               not null
        constraint unit_ref_fk
            references public.unit_refs,

    language         text                  not null,

    label_sg         text                  not null,
    label_sg_abrv    text,
    label_pl         text,
    label_pl_abrv    text
);

alter table public.unit_localizations
    owner to ${owner};


create table public.unit_conversions
(
    "from" integer          not null
        constraint unit_a_fk
            references public.unit_refs,
    "to"   integer          not null
        constraint unit_b_fk
            references public.unit_refs,
    factor double precision not null
);

alter table public.unit_conversions
    owner to ${owner};


alter table public.ingredients
    add schema         integer default 1 not null,
    add unit_localized integer
        constraint unit_localized_fk
            references public.unit_localizations;
