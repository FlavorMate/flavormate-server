-- recipe draft file
alter table public.v3__recipe_draft__file
    add temporary_file text;

comment on column public.v3__recipe_draft__file.temporary_file is 'Used to generate thumbnails. Null if thumbnails were generated.';