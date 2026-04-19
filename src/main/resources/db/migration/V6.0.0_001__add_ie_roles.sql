-- Adding new roles for import and export
INSERT INTO public.v3__role (id, value)
VALUES (DEFAULT, 'Import');

INSERT INTO public.v3__role (id, value)
VALUES (DEFAULT, 'Export');

-- Adding values to all users
INSERT INTO public.v3__account__role (account_id, role_id)
SELECT a.id, r.id
FROM public.v3__account a
         CROSS JOIN public.v3__role r
WHERE r.value IN ('Import', 'Export')
  AND NOT EXISTS (SELECT 1 FROM public.v3__account__role WHERE account_id = a.id AND role_id = r.id);