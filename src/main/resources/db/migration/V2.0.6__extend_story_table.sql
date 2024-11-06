truncate table public.stories;

alter table public.stories
    add author_id integer not null
        constraint authors_fk
            references public.authors;
