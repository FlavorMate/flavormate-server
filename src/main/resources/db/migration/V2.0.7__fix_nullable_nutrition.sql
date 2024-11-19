UPDATE public.nutrition
SET carbohydrates = null
WHERE carbohydrates = 0;

UPDATE public.nutrition
SET energy_kcal = null
WHERE energy_kcal = 0;

UPDATE public.nutrition
SET fat = null
WHERE fat = 0;

UPDATE public.nutrition
SET saturated_fat = null
WHERE saturated_fat = 0;

UPDATE public.nutrition
SET sugars = null
WHERE sugars = 0;

UPDATE public.nutrition
SET fiber = null
WHERE fiber = 0;

UPDATE public.nutrition
SET proteins = null
WHERE proteins = 0;

UPDATE public.nutrition
SET salt = null
WHERE salt = 0;

UPDATE public.nutrition
SET sodium = null
WHERE sodium = 0;
