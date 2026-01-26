ALTER TABLE public.v3__token
    DROP COLUMN uses;

DELETE
FROM public.v3__token
WHERE type = 'ACCOUNT';