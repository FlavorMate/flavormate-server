create table public.nutrition
(
    id                 serial primary key,
    created_on         timestamptz default now(),
    last_modified_on   timestamptz default now(),
    version            bigint      default 1 not null,
    open_food_facts_id text,
    carbohydrates      double precision,
    energy_kcal        double precision,
    fat                double precision,
    saturated_fat      double precision,
    sugars             double precision,
    fiber              double precision,
    proteins           double precision,
    salt               double precision,
    sodium             double precision
);

alter table public.nutrition
    owner to ${owner};


alter table public.ingredients
    add nutrition_id integer
        constraint nutrition_fk
            references public.nutrition;
