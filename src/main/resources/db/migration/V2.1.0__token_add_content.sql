alter table public.tokens
    add content bigint default NULL,
    drop constraint tokens_type_check,
    add constraint tokens_type_check
        check ((type)::text = ANY (ARRAY [('PASSWORD'::character varying)::text, ('SHARE'::character varying)::text])),
    add uses bigint default 0;
