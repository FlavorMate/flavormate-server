-- Insert conversion factors (pre-calculated)
INSERT INTO public.v3__unit_conversion (source, target, factor)
VALUES
-- Teaspoon to other units
('5804c120-43c5-4627-a808-01a6cb5ebbeb', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20',
 0.33333),                                                                                  -- 1 teaspoon = 0.33333 tablespoon
('5804c120-43c5-4627-a808-01a6cb5ebbeb', '1adbb935-c116-4712-97c9-cb830c95ee33',
 4.92892),                                                                                  -- 1 teaspoon = 4.92892 milliliters
('5804c120-43c5-4627-a808-01a6cb5ebbeb', '9831a5a3-4284-4112-bcde-c478f81f6185',
 0.16667),                                                                                  -- 1 teaspoon = 0.16667 fluid ounce
('5804c120-43c5-4627-a808-01a6cb5ebbeb', '18ff2ed2-44ba-4f7b-b52e-57b757887528',
 0.8),                                                                                      -- 1 teaspoon = 0.8 level teaspoon
('5804c120-43c5-4627-a808-01a6cb5ebbeb', '638c3e65-0be4-46d0-942a-d590abb70e24',
 1.2),                                                                                      -- 1 teaspoon = 1.2 heaped teaspoon
-- Tablespoon to other units
('fe039e8e-cb9a-4b71-b52e-ca05596dda20', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 3),        -- 1 tablespoon = 3 teaspoons
('fe039e8e-cb9a-4b71-b52e-ca05596dda20', '1adbb935-c116-4712-97c9-cb830c95ee33',
 14.7868),                                                                                  -- 1 tablespoon = 14.7868 milliliters
('fe039e8e-cb9a-4b71-b52e-ca05596dda20', '9831a5a3-4284-4112-bcde-c478f81f6185', 0.5),      -- 1 tablespoon = 0.5 fluid ounce
('fe039e8e-cb9a-4b71-b52e-ca05596dda20', 'a1acca17-d836-430d-b41b-fd633881e967',
 1),                                                                                        -- 1 tablespoon = 1 level tablespoon
('fe039e8e-cb9a-4b71-b52e-ca05596dda20', 'e0202667-4523-4971-8e83-c22cab7a62ea',
 0.66667),                                                                                  -- 1 tablespoon = 0.66667 heaped tablespoon
-- Level Tablespoon to other units
('a1acca17-d836-430d-b41b-fd633881e967', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20',
 1),                                                                                        -- 1 level tablespoon = 1 tablespoon
('a1acca17-d836-430d-b41b-fd633881e967', '1adbb935-c116-4712-97c9-cb830c95ee33',
 14.7868),                                                                                  -- 1 level tablespoon = 14.7868 milliliters
-- Heaped Tablespoon to other units
('e0202667-4523-4971-8e83-c22cab7a62ea', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20',
 1.5),                                                                                      -- 1 heaped tablespoon ≈ 1.5 tablespoons
('e0202667-4523-4971-8e83-c22cab7a62ea', '1adbb935-c116-4712-97c9-cb830c95ee33',
 22.18),                                                                                    -- 1 heaped tablespoon ≈ 22.18 milliliters
-- Level Teaspoon to other units
('638c3e65-0be4-46d0-942a-d590abb70e24', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 1),        -- 1 level teaspoon = 1 teaspoon
('638c3e65-0be4-46d0-942a-d590abb70e24', '1adbb935-c116-4712-97c9-cb830c95ee33',
 4.92892),                                                                                  -- 1 level teaspoon = 4.92892 milliliters
-- Heaped Teaspoon to other units
('18ff2ed2-44ba-4f7b-b52e-57b757887528', '5804c120-43c5-4627-a808-01a6cb5ebbeb',
 0.8),                                                                                      -- 1 heaped teaspoon ≈ 0.8 teaspoons
('18ff2ed2-44ba-4f7b-b52e-57b757887528', '1adbb935-c116-4712-97c9-cb830c95ee33',
 3.94314),                                                                                  -- 1 heaped teaspoon ≈ 3.94314 milliliters
-- Cup to other units
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 48),       -- 1 cup = 48 teaspoons
('1c7893cb-19a4-4253-90bf-8fa4efdff669', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20', 16),       -- 1 cup = 16 tablespoons
('1c7893cb-19a4-4253-90bf-8fa4efdff669', 'e8b16338-19dc-4c7a-afee-12fdbefded4b', 0.5),      -- 1 cup = 0.5 pint
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '419eb0f6-5846-4ef4-adef-24c9ac1291a2', 0.25),     -- 1 cup = 0.25 quart
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '27f76825-b921-4d5b-bd7e-b4e05665ae63', 0.0625),   -- 1 cup = 0.0625 gallon
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '1adbb935-c116-4712-97c9-cb830c95ee33',
 236.588),                                                                                  -- 1 cup = 236.588 milliliters
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 0.236588), -- 1 cup = 0.236588 liters
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d',
 23.6588),                                                                                  -- 1 cup = 23.6588 centiliters
('1c7893cb-19a4-4253-90bf-8fa4efdff669', '9831a5a3-4284-4112-bcde-c478f81f6185', 8),        -- 1 cup = 8 fluid ounces
-- Pint to other units
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 2),        -- 1 pint = 2 cups
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '419eb0f6-5846-4ef4-adef-24c9ac1291a2', 0.5),      -- 1 pint = 0.5 quart
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '27f76825-b921-4d5b-bd7e-b4e05665ae63', 0.125),    -- 1 pint = 0.125 gallon
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '1adbb935-c116-4712-97c9-cb830c95ee33',
 473.176),                                                                                  -- 1 pint = 473.176 milliliters
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 0.473176), -- 1 pint = 0.473176 liters
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d',
 47.3176),                                                                                  -- 1 pint = 47.3176 centiliters
('e8b16338-19dc-4c7a-afee-12fdbefded4b', '9831a5a3-4284-4112-bcde-c478f81f6185', 16),       -- 1 pint = 16 fluid ounces
-- Quart to other units
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 4),        -- 1 quart = 4 cups
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', 'e8b16338-19dc-4c7a-afee-12fdbefded4b', 2),        -- 1 quart = 2 pints
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', '27f76825-b921-4d5b-bd7e-b4e05665ae63', 0.25),     -- 1 quart = 0.25 gallon
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', '1adbb935-c116-4712-97c9-cb830c95ee33',
 946.353),                                                                                  -- 1 quart = 946.353 milliliters
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 0.946353), -- 1 quart = 0.946353 liters
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d',
 94.6353),                                                                                  -- 1 quart = 94.6353 centiliters
('419eb0f6-5846-4ef4-adef-24c9ac1291a2', '9831a5a3-4284-4112-bcde-c478f81f6185', 32),       -- 1 quart = 32 fluid ounces
-- Gallon to other units
('27f76825-b921-4d5b-bd7e-b4e05665ae63', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 16),       -- 1 gallon = 16 cups
('27f76825-b921-4d5b-bd7e-b4e05665ae63', 'e8b16338-19dc-4c7a-afee-12fdbefded4b', 8),        -- 1 gallon = 8 pints
('27f76825-b921-4d5b-bd7e-b4e05665ae63', '419eb0f6-5846-4ef4-adef-24c9ac1291a2', 4),        -- 1 gallon = 4 quarts
('27f76825-b921-4d5b-bd7e-b4e05665ae63', '1adbb935-c116-4712-97c9-cb830c95ee33',
 3785.41),                                                                                  -- 1 gallon = 3785.41 milliliters
('27f76825-b921-4d5b-bd7e-b4e05665ae63', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 3.78541),  -- 1 gallon = 3.78541 liters
('27f76825-b921-4d5b-bd7e-b4e05665ae63', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d',
 378.541),                                                                                  -- 1 gallon = 378.541 centiliters
('27f76825-b921-4d5b-bd7e-b4e05665ae63', '9831a5a3-4284-4112-bcde-c478f81f6185', 128),      -- 1 gallon = 128 fluid ounces
-- Milliliter to other units
('1adbb935-c116-4712-97c9-cb830c95ee33', '5804c120-43c5-4627-a808-01a6cb5ebbeb',
 0.20288),                                                                                  -- 1 milliliter = 0.20288 teaspoon
('1adbb935-c116-4712-97c9-cb830c95ee33', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20',
 0.06763),                                                                                  -- 1 milliliter = 0.06763 tablespoon
('1adbb935-c116-4712-97c9-cb830c95ee33', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 0.00423),  -- 1 milliliter = 0.00423 cup
('1adbb935-c116-4712-97c9-cb830c95ee33', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 0.001),    -- 1 milliliter = 0.001 liter
('1adbb935-c116-4712-97c9-cb830c95ee33', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', 0.1),      -- 1 milliliter = 0.1 centiliter
('1adbb935-c116-4712-97c9-cb830c95ee33', '68cd5d5f-d369-464e-b0f6-945de5197e69', 0.01),     -- 1 milliliter = 0.01 deciliter
-- Centiliter to other units
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', '1adbb935-c116-4712-97c9-cb830c95ee33', 10),       -- 1 centiliter = 10 milliliters
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 0.01),     -- 1 centiliter = 0.01 liter
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', '68cd5d5f-d369-464e-b0f6-945de5197e69', 0.1),      -- 1 centiliter = 0.1 deciliter
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', '5804c120-43c5-4627-a808-01a6cb5ebbeb',
 2.02884),                                                                                  -- 1 centiliter = 2.02884 teaspoons
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20',
 0.67628),                                                                                  -- 1 centiliter = 0.67628 tablespoons
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', '1c7893cb-19a4-4253-90bf-8fa4efdff669',
 0.04227),                                                                                  -- 1 centiliter = 0.04227 cups
('751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', '9831a5a3-4284-4112-bcde-c478f81f6185',
 0.33814),                                                                                  -- 1 centiliter = 0.33814 fluid ounces
-- Deciliter to other units
('68cd5d5f-d369-464e-b0f6-945de5197e69', '1adbb935-c116-4712-97c9-cb830c95ee33', 10),       -- 1 deciliter = 100 milliliters
('68cd5d5f-d369-464e-b0f6-945de5197e69', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 0.01),     -- 1 deciliter = 0.1 liter
('68cd5d5f-d369-464e-b0f6-945de5197e69', '5804c120-43c5-4627-a808-01a6cb5ebbeb',
 2.02884),                                                                                  -- 1 deciliter = 20.2884 teaspoons
('68cd5d5f-d369-464e-b0f6-945de5197e69', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20',
 0.67628),                                                                                  -- 1 deciliter = 6.7628 tablespoons
('68cd5d5f-d369-464e-b0f6-945de5197e69', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 0.04227),  -- 1 deciliter = 0.4227 cups
('68cd5d5f-d369-464e-b0f6-945de5197e69', '9831a5a3-4284-4112-bcde-c478f81f6185',
 0.33814),                                                                                  -- 1 deciliter = 3.3814 fluid ounces
-- Liter to other units
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 4.22675),  -- 1 liter = 4.22675 cups
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 'e8b16338-19dc-4c7a-afee-12fdbefded4b', 2.11338),  -- 1 liter = 2.11338 pints
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', '419eb0f6-5846-4ef4-adef-24c9ac1291a2', 1.05669),  -- 1 liter = 1.05669 quarts
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', '27f76825-b921-4d5b-bd7e-b4e05665ae63', 0.26417),  -- 1 liter = 0.26417 gallons
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', '1adbb935-c116-4712-97c9-cb830c95ee33', 1000),     -- 1 liter = 1000 milliliters
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', 100),      -- 1 liter = 100 centiliters
('54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', '68cd5d5f-d369-464e-b0f6-945de5197e69', 10),       -- 1 liter = 10 deziliters
-- Ounce to other units
('9a37f351-c84f-404a-83cf-8254ca031874', 'c752b40a-f68b-4f7e-b7b8-26dda2494b1d', 0.0625),   -- 1 ounce = 0.0625 pound
('9a37f351-c84f-404a-83cf-8254ca031874', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 28.3495),  -- 1 ounce = 28.3495 grams
('9a37f351-c84f-404a-83cf-8254ca031874', 'e0b04679-da06-4ff0-b8f4-c79964700b0f',
 0.02835),                                                                                  -- 1 ounce = 0.02835 kilograms
-- Pound to other units
('c752b40a-f68b-4f7e-b7b8-26dda2494b1d', '9a37f351-c84f-404a-83cf-8254ca031874', 16),       -- 1 pound = 16 ounces
('c752b40a-f68b-4f7e-b7b8-26dda2494b1d', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 453.592),  -- 1 pound = 453.592 grams
('c752b40a-f68b-4f7e-b7b8-26dda2494b1d', 'e0b04679-da06-4ff0-b8f4-c79964700b0f',
 0.45359),                                                                                  -- 1 pound = 0.45359 kilograms
-- Gram to other units
('a736bf0e-e8a4-4605-a173-c710bfa0a4e4', '9a37f351-c84f-404a-83cf-8254ca031874', 0.03527),  -- 1 gram = 0.03527 ounces
('a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 'c752b40a-f68b-4f7e-b7b8-26dda2494b1d', 0.0022),   -- 1 gram = 0.0022 pounds
('a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 'e0b04679-da06-4ff0-b8f4-c79964700b0f', 0.001),    -- 1 gram = 0.001 kilograms
('a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 'f185b324-5871-4ca9-a45d-b90f4bb7c867', 0.001),    -- 1 gram = 1000 milligram
-- Kilogram to other units
('e0b04679-da06-4ff0-b8f4-c79964700b0f', '9a37f351-c84f-404a-83cf-8254ca031874', 35.274),   -- 1 kilogram = 35.274 ounces
('e0b04679-da06-4ff0-b8f4-c79964700b0f', 'c752b40a-f68b-4f7e-b7b8-26dda2494b1d',
 2.20462),                                                                                  -- 1 kilogram = 2.20462 pounds
('e0b04679-da06-4ff0-b8f4-c79964700b0f', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 1000),     -- 1 kilogram = 1000 grams
('e0b04679-da06-4ff0-b8f4-c79964700b0f', 'f185b324-5871-4ca9-a45d-b90f4bb7c867',
 1000000),                                                                                  -- 1 kilogram = 1000000 milligrams
-- Milligram to other units
('f185b324-5871-4ca9-a45d-b90f4bb7c867', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 0.001),    -- 1 milligram = 0.001 grams
('f185b324-5871-4ca9-a45d-b90f4bb7c867', 'e0b04679-da06-4ff0-b8f4-c79964700b0f',
 0.000001),                                                                                 -- 1 milligram = 0.000001 kilograms
-- Centimeter to other units
('9763afcc-3c16-4e92-a172-28dd26c1e555', '771280ac-2b4d-4bf1-99eb-4df945a90ce4', 0.01),     -- 1 centimeter = 0.01 meters
('9763afcc-3c16-4e92-a172-28dd26c1e555', 'ef519ac6-9dd5-4805-b5df-dc99ef179c76',
 0.393701),                                                                                 -- 1 centimeter = 0.393701 inches
-- Meter to other units
('771280ac-2b4d-4bf1-99eb-4df945a90ce4', '9763afcc-3c16-4e92-a172-28dd26c1e555', 100),      -- 1 meter = 100 centimeters
('771280ac-2b4d-4bf1-99eb-4df945a90ce4', 'ef519ac6-9dd5-4805-b5df-dc99ef179c76', 39.3701),  -- 1 meter = 39.3701 inches
('771280ac-2b4d-4bf1-99eb-4df945a90ce4', '0fd704fe-6fe6-4b17-99c1-f85badedc59b', 3.28084),  -- 1 meter = 3.28084 feet
-- Inch to other units
('ef519ac6-9dd5-4805-b5df-dc99ef179c76', '9763afcc-3c16-4e92-a172-28dd26c1e555', 2.54),     -- 1 inch = 2.54 centimeters
('ef519ac6-9dd5-4805-b5df-dc99ef179c76', '771280ac-2b4d-4bf1-99eb-4df945a90ce4', 0.0254),   -- 1 inch = 0.0254 meters
('ef519ac6-9dd5-4805-b5df-dc99ef179c76', '0fd704fe-6fe6-4b17-99c1-f85badedc59b', 0.08333),  -- 1 inch = 0.08333 feet
-- Foot to other units
('0fd704fe-6fe6-4b17-99c1-f85badedc59b', '9763afcc-3c16-4e92-a172-28dd26c1e555', 30.48),    -- 1 foot = 30.48 centimeters
('0fd704fe-6fe6-4b17-99c1-f85badedc59b', '771280ac-2b4d-4bf1-99eb-4df945a90ce4', 0.3048),   -- 1 foot = 0.3048 meters
('0fd704fe-6fe6-4b17-99c1-f85badedc59b', 'ef519ac6-9dd5-4805-b5df-dc99ef179c76', 12),       -- 1 foot = 12 inches
-- Half to other units
('1e73ff7f-5f70-4b28-8b26-a132a9e33c77', '1c7893cb-19a4-4253-90bf-8fa4efdff669',
 0.5),                                                                                      -- 1 half = 0.5 cup (context-dependent)
('1e73ff7f-5f70-4b28-8b26-a132a9e33c77', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7',
 0.5),                                                                                      -- 1 half = 0.5 liter (context-dependent)
('1e73ff7f-5f70-4b28-8b26-a132a9e33c77', 'c752b40a-f68b-4f7e-b7b8-26dda2494b1d',
 0.5),                                                                                      -- 1 half = 0.5 pound (context-dependent)
-- Handful to other units (approximate)
('ee99d667-1498-4e64-ad46-cf50c9aaa541', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20', 2),        -- 1 handful ≈ 2 tablespoons
('ee99d667-1498-4e64-ad46-cf50c9aaa541', '1adbb935-c116-4712-97c9-cb830c95ee33', 30),       -- 1 handful ≈ 30 milliliters
-- Root to other units (assumed to be a piece)
('6c00d1ee-1b35-494f-81f1-934cd839c1d7', '45b7874e-8aea-475c-9fd9-a05578504c7d', 1),        -- 1 root = 1 piece
-- Splash to other units (approximate)
('6ee44a2e-daf0-44b9-9530-58fe4a248a62', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 0.5),      -- 1 splash ≈ 0.5 teaspoon
('6ee44a2e-daf0-44b9-9530-58fe4a248a62', '1adbb935-c116-4712-97c9-cb830c95ee33', 2.5),      -- 1 splash ≈ 2.5 milliliters
-- Dash to other units (approximate)
('2e2f14cd-0600-40b1-b767-e4dd809cb49d', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 0.125),    -- 1 dash ≈ 0.125 teaspoon
('2e2f14cd-0600-40b1-b767-e4dd809cb49d', '1adbb935-c116-4712-97c9-cb830c95ee33', 0.616),    -- 1 dash ≈ 0.616 milliliters
-- Pinch to other units (approximate)
('91a4bc6f-53b0-4263-b984-1a757eb720bd', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 0.0625),   -- 1 pinch ≈ 0.0625 teaspoon
('91a4bc6f-53b0-4263-b984-1a757eb720bd', '1adbb935-c116-4712-97c9-cb830c95ee33', 0.308),    -- 1 pinch ≈ 0.308 milliliters
-- Sprig to other units (assumed to be a piece)
('5f440eb7-b8d4-4020-ab08-b51adcdb2020', '45b7874e-8aea-475c-9fd9-a05578504c7d', 1),        -- 1 sprig = 1 piece
-- Clove to other units (assumed to be a piece)
('2fb77aea-32a9-4fcc-b9b8-406b98decbe3', '45b7874e-8aea-475c-9fd9-a05578504c7d', 1),        -- 1 clove = 1 piece
-- Grain to other units (mass, approximate)
('5dc59434-1312-4870-936f-2058b433f7a8', 'f185b324-5871-4ca9-a45d-b90f4bb7c867',
 64.7989),                                                                                  -- 1 grain = 64.7989 milligrams
('5dc59434-1312-4870-936f-2058b433f7a8', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 0.0648),   -- 1 grain = 0.0648 grams
-- Bar (of chocolate) to other units (mass, approximate)
('b1bfa99c-e555-406e-9391-3cafef907b9c', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4',
 100),                                                                                      -- 1 bar = 100 grams (common chocolate bar size)
-- Tablet to other units (mass, approximate)
('b423c3cf-de1c-4fb4-b6b7-a72ad350b321', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 1),        -- 1 tablet = 1 gram (approximate)
-- Tube to other units (volume, approximate)
('283c07e5-de59-4246-bbe6-971fba1c2cae', '1adbb935-c116-4712-97c9-cb830c95ee33',
 100),                                                                                      -- 1 tube = 100 milliliters (depends on tube size)
-- Pot to other units (volume, approximate)
('58f4777a-fd99-4508-ad78-39d84089c5de', '1adbb935-c116-4712-97c9-cb830c95ee33',
 125),                                                                                      -- 1 pot = 125 milliliters (common yogurt pot size)
-- Bowl to other units (volume, approximate)
('6447557d-8ecb-4a7b-b9f1-9a3804419b63', '1c7893cb-19a4-4253-90bf-8fa4efdff669',
 1),                                                                                        -- 1 bowl = 1 cup (depends on bowl size)
-- Small bowl to other units (volume, approximate)
('1f5054f7-f9cf-4f33-98d0-94a675bf777c', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 0.5),      -- 1 small bowl = 0.5 cup
-- Bag to other units (mass or volume, context-dependent)
('662a4f09-d8d0-4666-a53d-a49abc5cfcaa', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4',
 1000),                                                                                     -- 1 bag = 1000 grams (e.g., bag of flour)
-- Plate to other units (serving size)
('cfbe00fb-597c-43f2-adde-893b628d26a9', '97cd27a3-ac0a-4925-86ee-b2274ced68ef', 1),        -- 1 plate = 1 portion
-- Portion to other units (context-dependent)
('97cd27a3-ac0a-4925-86ee-b2274ced68ef', '45b7874e-8aea-475c-9fd9-a05578504c7d', 1),        -- 1 portion = 1 piece
-- Glass
('74b7f6d6-1c2e-4bc9-bfcd-d0f63ddf6bbb', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 1) -- 1 Glas = 1 cup
;
