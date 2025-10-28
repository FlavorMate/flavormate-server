ALTER TABLE public.v3__account
    ADD CONSTRAINT avatar__fk
        FOREIGN KEY (avatar)
            REFERENCES public.v3__account__file (id)
            ON DELETE SET NULL,
    ADD CONSTRAINT username__unique
        UNIQUE (username),
    ADD CONSTRAINT email__unique
        UNIQUE (email);

ALTER TABLE public.v3__book
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT cover_recipe__fk
        FOREIGN KEY (cover_recipe)
            REFERENCES public.v3__recipe (id)
            ON DELETE SET NULL;

ALTER TABLE public.v3__book__subscriber
    ADD CONSTRAINT account_id__fk
        FOREIGN KEY (account_id)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT book_id__fk
        FOREIGN KEY (book_id)
            REFERENCES public.v3__book (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__category
    ADD CONSTRAINT group_id__fk
        FOREIGN KEY (group_id)
            REFERENCES public.v3__category_group (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT cover_recipe__fk
        FOREIGN KEY (cover_recipe)
            REFERENCES public.v3__recipe (id)
            ON DELETE SET NULL;

ALTER TABLE public.v3__category_group__l10n
    ADD CONSTRAINT category_group_id__fk
        FOREIGN KEY (category_group_id)
            REFERENCES public.v3__category_group (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__category__l10n
    ADD CONSTRAINT category_id__fk
        FOREIGN KEY (category_id)
            REFERENCES public.v3__category (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__account__role
    ADD CONSTRAINT account_id__fk
        FOREIGN KEY (account_id)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT role_id__fk
        FOREIGN KEY (role_id)
            REFERENCES public.v3__role (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe
    ADD CONSTRAINT serving_id__fk
        FOREIGN KEY (serving_id)
            REFERENCES public.v3__recipe__serving (id),
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT cover_file__fk
        FOREIGN KEY (cover_file)
            REFERENCES public.v3__recipe__file (id)
            ON DELETE SET NULL;
create index v3__recipe__idx_course
    on public.v3__recipe (course);

ALTER TABLE public.v3__recipe_draft
    ADD CONSTRAINT serving_id__fk
        FOREIGN KEY (serving_id)
            REFERENCES public.v3__recipe_draft__serving (id),
    ADD CONSTRAINT origin_id__fk
        FOREIGN KEY (origin_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe_draft__ingredient_group
    ADD CONSTRAINT recipe_draft_id__fk
        FOREIGN KEY (recipe_draft_id)
            REFERENCES public.v3__recipe_draft (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe_draft__ingredient_group__item
    ADD CONSTRAINT group_id__fk
        FOREIGN KEY (group_id)
            REFERENCES public.v3__recipe_draft__ingredient_group (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT unit_l10n__fk
        FOREIGN KEY (unit)
            REFERENCES public.v3__unit_l10n (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT nutrition_id__fk
        FOREIGN KEY (nutrition_id)
            REFERENCES public.v3__recipe_draft__ingredient_group__item__nutrition (id)
            ON DELETE SET NULL;

ALTER TABLE public.v3__recipe_draft__instruction_group
    ADD CONSTRAINT recipe_draft_id__fk
        FOREIGN KEY (recipe_draft_id)
            REFERENCES public.v3__recipe_draft (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe_draft__instruction_group__item
    ADD CONSTRAINT group_id__fk
        FOREIGN KEY (group_id)
            REFERENCES public.v3__recipe_draft__instruction_group (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__category__recipe_draft
    ADD CONSTRAINT category_id__fk
        FOREIGN KEY (category_id)
            REFERENCES public.v3__category (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT recipe_draft_id__fk
        FOREIGN KEY (recipe_draft_id)
            REFERENCES public.v3__recipe_draft (id)
            ON DELETE CASCADE;


ALTER TABLE public.v3__recipe__rating
    ADD CONSTRAINT account_id__fk
        FOREIGN KEY (account_id)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__book__recipe
    ADD CONSTRAINT book_id__fk
        FOREIGN KEY (book_id)
            REFERENCES public.v3__book (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__category__recipe
    ADD CONSTRAINT category_id__fk
        FOREIGN KEY (category_id)
            REFERENCES public.v3__category (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__account__file
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe__file
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe_draft__file
    ADD CONSTRAINT origin_id__fk
        FOREIGN KEY (origin_id)
            REFERENCES public.v3__recipe__file (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_draft_id)
            REFERENCES public.v3__recipe_draft (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__highlight
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe__ingredient_group
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe__ingredient_group__item
    ADD CONSTRAINT group_id__fk
        FOREIGN KEY (group_id)
            REFERENCES public.v3__recipe__ingredient_group (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT unit_l10n__fk
        FOREIGN KEY (unit)
            REFERENCES public.v3__unit_l10n (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT nutrition_id__fk
        FOREIGN KEY (nutrition_id)
            REFERENCES public.v3__recipe__ingredient_group__item__nutrition (id)
            ON DELETE SET NULL;

ALTER TABLE public.v3__recipe__instruction_group
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__recipe__instruction_group__item
    ADD CONSTRAINT group_id__fk
        FOREIGN KEY (group_id)
            REFERENCES public.v3__recipe__instruction_group (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__story
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__story_draft
    ADD CONSTRAINT origin_id__fk
        FOREIGN KEY (origin_id)
            REFERENCES public.v3__story (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE,
    add constraint recipe__fk
        foreign key (recipe) references v3__recipe
            on delete cascade;;

ALTER TABLE public.v3__tag
    ADD CONSTRAINT cover_recipe__fk
        FOREIGN KEY (cover_recipe)
            REFERENCES public.v3__recipe (id)
            ON DELETE SET NULL;

ALTER TABLE public.v3__tag__recipe
    ADD CONSTRAINT recipe_id__fk
        FOREIGN KEY (recipe_id)
            REFERENCES public.v3__recipe (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT tag_id__fk
        FOREIGN KEY (tag_id)
            REFERENCES public.v3__tag (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__token
    ADD CONSTRAINT owned_by__fk
        FOREIGN KEY (owned_by)
            REFERENCES public.v3__account (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__unit_l10n
    ADD CONSTRAINT unit_ref__fk
        FOREIGN KEY (unit_ref)
            REFERENCES public.v3__unit_ref (id)
            ON DELETE CASCADE;

ALTER TABLE public.v3__unit_conversion
    ADD CONSTRAINT unit_source__fk
        FOREIGN KEY (source)
            REFERENCES public.v3__unit_ref (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT unit_target__fk
        FOREIGN KEY (target)
            REFERENCES public.v3__unit_ref (id)
            ON DELETE CASCADE;


ALTER TABLE public.v3__recipe__ingredient_group__item__nutrition
    ADD CONSTRAINT open_food_facts_id_fk
        FOREIGN KEY (open_food_facts_id)
            REFERENCES public.v3__ext__off__product (id)
            ON DELETE CASCADE;


ALTER TABLE public.v3__ext__url_shortener
    ADD CONSTRAINT short_path_unique
        UNIQUE (short_path),
    ADD CONSTRAINT original_path_unique
        UNIQUE (original_path);

ALTER TABLE public.v3__ext__auth__oidc_mapping
    ADD CONSTRAINT v3_oidc_mapping_accounts_fk
        FOREIGN KEY (account_id)
            REFERENCES public.v3__account
            ON DELETE CASCADE,
    ADD CONSTRAINT v3_oidc_mapping_pk_2
        PRIMARY KEY (subject, issuer, account_id),
    ADD CONSTRAINT v3_oidc_mapping_pk
        UNIQUE (issuer, subject);
