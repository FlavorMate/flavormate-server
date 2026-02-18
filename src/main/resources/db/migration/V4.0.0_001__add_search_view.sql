CREATE OR REPLACE VIEW v3__search_index AS
SELECT ('Account:' || a.id)::text AS pk,
       a.id::text                 AS entity_id,
       'Account'::text            AS source,
       a.display_name::text       AS label,
       NULL::text                 AS recipe_id,
       a.avatar::text             AS file_id,
       NULL::boolean              AS book_visible,
       NULL::text                 AS book_owned_by,
       NULL::text                 AS category_language
FROM v3__account a

UNION ALL
SELECT ('Book:' || b.id)::text AS pk,
       b.id::text              AS entity_id,
       'Book'::text            AS source,
       b.label::text           AS label,
       r.id::text              AS recipe_id,
       r.cover_file::text      AS file_id,
       b.visible               AS book_visible,
       b.owned_by::text        AS book_owned_by,
       NULL::text              AS category_language
FROM v3__book b
         LEFT JOIN v3__recipe r ON r.id = b.cover_recipe

UNION ALL
SELECT ('Category:' || c.id)::text AS pk,
       c.id::text                  AS entity_id,
       'Category'::text            AS source,
       cl.value::text              AS label,
       r.id::text                  AS recipe_id,
       r.cover_file::text          AS file_id,
       NULL::boolean               AS book_visible,
       NULL::text                  AS book_owned_by,
       cl.language::text           AS category_language
FROM v3__category c
         LEFT JOIN v3__category__l10n cl ON cl.category_id = c.id
         LEFT JOIN v3__recipe r ON r.id = c.cover_recipe

UNION ALL
SELECT ('Recipe:' || r.id)::text AS pk,
       r.id::text                AS entity_id,
       'Recipe'::text            AS source,
       r.label::text             AS label,
       r.id::text                AS recipe_id,
       r.cover_file::text        AS file_id,
       NULL::boolean             AS book_visible,
       NULL::text                AS book_owned_by,
       NULL::text                AS category_language
FROM v3__recipe r

UNION ALL
SELECT ('Story:' || s.id)::text AS pk,
       s.id::text               AS entity_id,
       'Story'::text            AS source,
       s.label::text            AS label,
       r.id::text               AS recipe_id,
       r.cover_file::text       AS file_id,
       NULL::boolean            AS book_visible,
       NULL::text               AS book_owned_by,
       NULL::text               AS category_language
FROM v3__story s
         LEFT JOIN v3__recipe r ON s.recipe_id = r.id

UNION ALL
SELECT ('Tag:' || t.id)::text AS pk,
       t.id::text             AS entity_id,
       'Tag'::text            AS source,
       t.label::text          AS label,
       r.id::text             AS recipe_id,
       r.cover_file::text     AS file_id,
       NULL::boolean          AS book_visible,
       NULL::text             AS book_owned_by,
       NULL::text             AS category_language
FROM v3__tag t
         LEFT JOIN v3__recipe r ON r.id = t.cover_recipe
;