drop table if exists public.v3__ext__auth__oidc_mapping;
drop table if exists public.v3__ext__auth__oidc_provider;

CREATE TABLE public.v3__ext__auth__oidc_provider
(
    id text DEFAULT gen_random_uuid() primary key,
    url_discovery_endpoint text    NOT NULL,
    url_token_endpoint     text    NOT NULL,
    issuer                 text    NOT NULL,
    client_id              text    NOT NULL,
    client_secret          text,
    label                  text    NOT NULL,
    icon                   bytea,
    override_redirect_uri  boolean NOT NULL,
    enabled                boolean NOT NULL DEFAULT true
);

ALTER TABLE public.v3__ext__auth__oidc_provider
    ADD CONSTRAINT v3__ext__auth__oidc_provider__issuer_client_id_unique
        UNIQUE (issuer, client_id);

CREATE TABLE public.v3__ext__auth__oidc_mapping
(
    id          text default gen_random_uuid() primary key,
    provider_id text not null,
    subject            text                      not null,
    account_id         text                      not null,
    name               text,
    email              text,
    created_on         timestamptz default now() not null
);

ALTER TABLE public.v3__ext__auth__oidc_mapping
    ADD CONSTRAINT v3__ext__auth__oidc_mapping__provider_fk
        FOREIGN KEY (provider_id)
            REFERENCES public.v3__ext__auth__oidc_provider
            ON DELETE CASCADE,

    ADD CONSTRAINT v3__ext__auth__oidc_mapping__account_fk
        FOREIGN KEY (account_id)
            REFERENCES public.v3__account
            ON DELETE CASCADE,

    ADD CONSTRAINT v3__ext__auth__oidc_mapping__unique
        UNIQUE (provider_id, subject, account_id);
