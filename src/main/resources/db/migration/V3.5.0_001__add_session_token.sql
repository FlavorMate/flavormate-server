CREATE TABLE public.v3__account__session
(
    id               text        DEFAULT gen_random_uuid() PRIMARY KEY,
    token_hash       text                      NOT NULL,
    expires_at       timestamptz               NOT NULL,
    revoked          boolean     DEFAULT false,

    created_on       timestamptz DEFAULT now() NOT NULL,
    last_modified_on timestamptz DEFAULT now() NOT NULL,
    version          bigint      DEFAULT 1     NOT NULL,

    account_id       text                      NOT NULL,
    user_agent       text
);

ALTER TABLE public.v3__account__session
    ADD CONSTRAINT v3__account__session__account_id_fk
        FOREIGN KEY (account_id) REFERENCES public.v3__account (id);
