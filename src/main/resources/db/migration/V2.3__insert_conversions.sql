-- Insert conversion factors (pre-calculated)
INSERT INTO public.unit_conversions ("from", "to", factor)
VALUES
-- Teaspoon to other units
(1, 2, 0.33333),    -- 1 teaspoon = 0.33333 tablespoon
(1, 7, 4.92892),    -- 1 teaspoon = 4.92892 milliliters
(1, 10, 0.16667),   -- 1 teaspoon = 0.16667 fluid ounce
(1, 82, 0.8),       -- 1 teaspoon = 0.8 level teaspoon
(1, 83, 1.2),       -- 1 teaspoon = 1.2 heaped teaspoon

-- Tablespoon to other units
(2, 1, 3),          -- 1 tablespoon = 3 teaspoons
(2, 7, 14.7868),    -- 1 tablespoon = 14.7868 milliliters
(2, 10, 0.5),       -- 1 tablespoon = 0.5 fluid ounce
(2, 36, 1),         -- 1 tablespoon = 1 level tablespoon
(2, 37, 0.66667),   -- 1 tablespoon = 0.66667 heaped tablespoon

-- Level Tablespoon to other units
(36, 2, 1),         -- 1 level tablespoon = 1 tablespoon
(36, 7, 14.7868),   -- 1 level tablespoon = 14.7868 milliliters

-- Heaped Tablespoon to other units
(37, 2, 1.5),       -- 1 heaped tablespoon ≈ 1.5 tablespoons
(37, 7, 22.18),     -- 1 heaped tablespoon ≈ 22.18 milliliters

-- Level Teaspoon to other units
(83, 1, 1),         -- 1 level teaspoon = 1 teaspoon
(83, 7, 4.92892),   -- 1 level teaspoon = 4.92892 milliliters

-- Heaped Teaspoon to other units
(82, 1, 0.8),       -- 1 heaped teaspoon ≈ 0.8 teaspoons
(82, 7, 3.94314),   -- 1 heaped teaspoon ≈ 3.94314 milliliters

-- Cup to other units
(3, 1, 48),         -- 1 cup = 48 teaspoons
(3, 2, 16),         -- 1 cup = 16 tablespoons
(3, 4, 0.5),        -- 1 cup = 0.5 pint
(3, 5, 0.25),       -- 1 cup = 0.25 quart
(3, 6, 0.0625),     -- 1 cup = 0.0625 gallon
(3, 7, 236.588),    -- 1 cup = 236.588 milliliters
(3, 8, 0.236588),   -- 1 cup = 0.236588 liters
(3, 30, 23.6588),   -- 1 cup = 23.6588 centiliters
(3, 10, 8),         -- 1 cup = 8 fluid ounces

-- Pint to other units
(4, 3, 2),          -- 1 pint = 2 cups
(4, 5, 0.5),        -- 1 pint = 0.5 quart
(4, 6, 0.125),      -- 1 pint = 0.125 gallon
(4, 7, 473.176),    -- 1 pint = 473.176 milliliters
(4, 8, 0.473176),   -- 1 pint = 0.473176 liters
(4, 30, 47.3176),   -- 1 pint = 47.3176 centiliters
(4, 10, 16),        -- 1 pint = 16 fluid ounces

-- Quart to other units
(5, 3, 4),          -- 1 quart = 4 cups
(5, 4, 2),          -- 1 quart = 2 pints
(5, 6, 0.25),       -- 1 quart = 0.25 gallon
(5, 7, 946.353),    -- 1 quart = 946.353 milliliters
(5, 8, 0.946353),   -- 1 quart = 0.946353 liters
(5, 30, 94.6353),   -- 1 quart = 94.6353 centiliters
(5, 10, 32),        -- 1 quart = 32 fluid ounces

-- Gallon to other units
(6, 3, 16),         -- 1 gallon = 16 cups
(6, 4, 8),          -- 1 gallon = 8 pints
(6, 5, 4),          -- 1 gallon = 4 quarts
(6, 7, 3785.41),    -- 1 gallon = 3785.41 milliliters
(6, 8, 3.78541),    -- 1 gallon = 3.78541 liters
(6, 30, 378.541),   -- 1 gallon = 378.541 centiliters
(6, 10, 128),       -- 1 gallon = 128 fluid ounces

-- Milliliter to other units
(7, 1, 0.20288),    -- 1 milliliter = 0.20288 teaspoon
(7, 2, 0.06763),    -- 1 milliliter = 0.06763 tablespoon
(7, 3, 0.00423),    -- 1 milliliter = 0.00423 cup
(7, 8, 0.001),      -- 1 milliliter = 0.001 liter
(7, 30, 0.1),       -- 1 milliliter = 0.1 centiliter

-- Centiliter to other units
(30, 7, 10),        -- 1 centiliter = 10 milliliters
(30, 8, 0.01),      -- 1 centiliter = 0.01 liter
(30, 1, 2.02884),   -- 1 centiliter = 2.02884 teaspoons
(30, 2, 0.67628),   -- 1 centiliter = 0.67628 tablespoons
(30, 3, 0.04227),   -- 1 centiliter = 0.04227 cups
(30, 10, 0.33814),  -- 1 centiliter = 0.33814 fluid ounces

-- Liter to other units
(8, 3, 4.22675),    -- 1 liter = 4.22675 cups
(8, 4, 2.11338),    -- 1 liter = 2.11338 pints
(8, 5, 1.05669),    -- 1 liter = 1.05669 quarts
(8, 6, 0.26417),    -- 1 liter = 0.26417 gallons
(8, 7, 1000),       -- 1 liter = 1000 milliliters
(8, 30, 100),       -- 1 liter = 100 centiliters

-- Ounce to other units
(11, 12, 0.0625),   -- 1 ounce = 0.0625 pound
(11, 13, 28.3495),  -- 1 ounce = 28.3495 grams
(11, 14, 0.02835),  -- 1 ounce = 0.02835 kilograms

-- Pound to other units
(12, 11, 16),       -- 1 pound = 16 ounces
(12, 13, 453.592),  -- 1 pound = 453.592 grams
(12, 14, 0.45359),  -- 1 pound = 0.45359 kilograms

-- Gram to other units
(13, 11, 0.03527),  -- 1 gram = 0.03527 ounces
(13, 12, 0.0022),   -- 1 gram = 0.0022 pounds
(13, 14, 0.001),    -- 1 gram = 0.001 kilograms

-- Kilogram to other units
(14, 11, 35.274),   -- 1 kilogram = 35.274 ounces
(14, 12, 2.20462),  -- 1 kilogram = 2.20462 pounds
(14, 13, 1000),     -- 1 kilogram = 1000 grams

-- Milligram to gram
(15, 13, 0.001),    -- 1 milligram = 0.001 grams

-- Centimeter to other units
(16, 17, 0.01),     -- 1 centimeter = 0.01 meters
(16, 18, 0.393701), -- 1 centimeter = 0.393701 inches

-- Meter to other units
(17, 16, 100),      -- 1 meter = 100 centimeters
(17, 18, 39.3701),  -- 1 meter = 39.3701 inches
(17, 19, 3.28084),  -- 1 meter = 3.28084 feet

-- Inch to other units
(18, 16, 2.54),     -- 1 inch = 2.54 centimeters
(18, 17, 0.0254),   -- 1 inch = 0.0254 meters
(18, 19, 0.08333),  -- 1 inch = 0.08333 feet

-- Foot to other units
(19, 16, 30.48),    -- 1 foot = 30.48 centimeters
(19, 17, 0.3048),   -- 1 foot = 0.3048 meters
(19, 18, 12),       -- 1 foot = 12 inches

-- Half to other units
(51, 3, 0.5),       -- 1 half = 0.5 cup (context-dependent)
(51, 8, 0.5),       -- 1 half = 0.5 liter (context-dependent)
(51, 12, 0.5),      -- 1 half = 0.5 pound (context-dependent)

-- Handful to other units (approximate)
(52, 2, 2),         -- 1 handful ≈ 2 tablespoons
(52, 7, 30),        -- 1 handful ≈ 30 milliliters

-- Root to other units (assumed to be a piece)
(89, 27, 1),        -- 1 root = 1 piece

-- Splash to other units (approximate)
(75, 1, 0.5),       -- 1 splash ≈ 0.5 teaspoon
(75, 7, 2.5),       -- 1 splash ≈ 2.5 milliliters

-- Dash to other units (approximate)
(22, 1, 0.125),     -- 1 dash ≈ 0.125 teaspoon
(22, 7, 0.616),     -- 1 dash ≈ 0.616 milliliters

-- Pinch to other units (approximate)
(21, 1, 0.0625),    -- 1 pinch ≈ 0.0625 teaspoon
(21, 7, 0.308),     -- 1 pinch ≈ 0.308 milliliters

-- Sprig to other units (assumed to be a piece)
(28, 27, 1),        -- 1 sprig = 1 piece

-- Clove to other units (assumed to be a piece)
(23, 27, 1),        -- 1 clove = 1 piece

-- Grain to other units (mass, approximate)
(63, 15, 64.7989),  -- 1 grain = 64.7989 milligrams
(63, 13, 0.0648),   -- 1 grain = 0.0648 grams

-- Bar (of chocolate) to other units (mass, approximate)
(80, 13, 100),      -- 1 bar = 100 grams (common chocolate bar size)

-- Tablet to other units (mass, approximate)
(79, 13, 1),        -- 1 tablet = 1 gram (approximate)

-- Tube to other units (volume, approximate)
(85, 7, 100),       -- 1 tube = 100 milliliters (depends on tube size)

-- Pot to other units (volume, approximate)
(84, 7, 125),       -- 1 pot = 125 milliliters (common yogurt pot size)

-- Bowl to other units (volume, approximate)
(74, 3, 1),         -- 1 bowl = 1 cup (depends on bowl size)

-- Small bowl to other units (volume, approximate)
(73, 3, 0.5),       -- 1 small bowl = 0.5 cup

-- Bag to other units (mass or volume, context-dependent)
(86, 13, 1000),     -- 1 bag = 1000 grams (e.g., bag of flour)

-- Plate to other units (serving size)
(69, 70, 1),        -- 1 plate = 1 portion

-- Portion to other units (context-dependent)
(70, 27, 1) -- 1 portion = 1 piece

;
