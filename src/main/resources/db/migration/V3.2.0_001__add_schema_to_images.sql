alter table public.v3__account__file
    add schema integer default 1 not null;

alter table public.v3__recipe__file
    add schema integer default 1 not null;

alter table public.v3__recipe_draft__file
    add schema integer default 1 not null;