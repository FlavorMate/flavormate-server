alter table public.ingredients
    alter column amount drop not null;

update public.ingredients
set amount = null
where amount <= 0;
