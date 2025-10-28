-- Insert unit labels for English and German
INSERT INTO public.v3__unit_l10n (id, unit_ref, language, label_sg, label_sg_abrv, label_pl, label_pl_abrv)
VALUES
-- Units 1 to 91
-- 1. Teaspoon
('28d476a0-8286-4bf4-89e2-5520ac2c8699', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 'en', 'teaspoon', 'tsp', 'teaspoons',
 NULL),
('0b573ec1-b725-470f-a97a-b3eca1bd3d1f', '5804c120-43c5-4627-a808-01a6cb5ebbeb', 'de', 'Teelöffel', 'TL', NULL, NULL),
-- 2. Tablespoon
('5bc2b0b8-c129-47e5-9ebd-a7138a99411f', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20', 'en', 'tablespoon', 'tbsp',
 'tablespoons', NULL),
('128e6060-6c30-4681-bf5b-a20899a3a392', 'fe039e8e-cb9a-4b71-b52e-ca05596dda20', 'de', 'Esslöffel', 'EL', NULL, NULL),
-- 3. Cup
('666d19cd-3edd-4484-8c52-11434506b642', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 'en', 'cup', NULL, 'cups', NULL),
('fcb5a793-5ed9-46f6-9b4b-b420bdfea094', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 'de', 'Tasse', NULL, 'Tassen', NULL),
('17f38ac9-992b-439b-83b7-4e4c47fba92c', '1c7893cb-19a4-4253-90bf-8fa4efdff669', 'de', 'Becher', NULL, NULL, NULL),
-- 4. Pint
('f0680b12-e0fd-4084-ac99-3c472f922ec0', 'e8b16338-19dc-4c7a-afee-12fdbefded4b', 'en', 'pint', 'pt', 'pints', NULL),
('c4cb41da-d0e3-4ddc-ba7e-c269a899c20f', 'e8b16338-19dc-4c7a-afee-12fdbefded4b', 'de', 'Pint', NULL, NULL, NULL),
-- 5. Quart
('ac63cdef-5cfa-493f-8120-96ea91d79a5b', '419eb0f6-5846-4ef4-adef-24c9ac1291a2', 'en', 'quart', 'qt', 'quarts', NULL),
('cfa17637-414f-49aa-9049-d73c90c4f45b', '419eb0f6-5846-4ef4-adef-24c9ac1291a2', 'de', 'Quart', NULL, NULL, NULL),
-- 6. Gallon
('f236f067-a9e6-4247-ac7c-5807c06a9c22', '27f76825-b921-4d5b-bd7e-b4e05665ae63', 'en', 'gallon', 'gal', 'gallons',
 NULL),
('f2bb3987-7d00-468a-a521-ae1f2f1ca548', '27f76825-b921-4d5b-bd7e-b4e05665ae63', 'de', 'Gallone', NULL, 'Gallonen',
 NULL),
-- 7. Milliliter
('10050ee9-fcf7-4581-93fc-f1804ddadc06', '1adbb935-c116-4712-97c9-cb830c95ee33', 'en', 'milliliter', 'ml',
 'milliliters', NULL),
('12111ef2-b303-4a72-a4a3-f9cafbf02225', '1adbb935-c116-4712-97c9-cb830c95ee33', 'de', 'Milliliter', 'ml', NULL, NULL),
-- 8. Liter
('febf4ce0-7884-4257-b39c-be8f38c97128', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 'en', 'liter', 'l', 'liters', NULL),
('9d9d28ea-6dee-4d7f-96fb-e666b872600c', '54a4fbb1-cc8a-4686-8e4a-b4c371a3f1e7', 'de', 'Liter', 'l', NULL, NULL),
-- 9. Deciliter
('b8ec4cd6-a1fe-45e8-b5c3-89b6e3248038', '68cd5d5f-d369-464e-b0f6-945de5197e69', 'en', 'deciliter', 'dl', 'deciliters',
 NULL),
('8bd2be77-fa09-4a2d-b30b-f82e411e6592', '68cd5d5f-d369-464e-b0f6-945de5197e69', 'de', 'Deziliter', 'dl', NULL, NULL),
-- 10. Fluid Ounce
('83ac52a7-4dfe-4199-a468-d8dd6d48c96a', '9831a5a3-4284-4112-bcde-c478f81f6185', 'en', 'fluid ounce', 'fl oz',
 'fluid ounces', NULL),
('0f3dfc4b-917c-4a4b-ac89-3510a4d7e27d', '9831a5a3-4284-4112-bcde-c478f81f6185', 'de', 'Flüssigunze', NULL, NULL, NULL),
-- 11. Ounce
('2f1510d8-2f61-4b7d-84cd-0632c869e88b', '9a37f351-c84f-404a-83cf-8254ca031874', 'en', 'ounce', 'oz', 'ounces', NULL),
('eaaa0420-8776-433f-b7bc-4719cc34ef2e', '9a37f351-c84f-404a-83cf-8254ca031874', 'de', 'Unze', 'oz', 'Unzen', NULL),
-- 12. Pound
('8040d258-8bdb-4d8d-a3f1-a17b90574fe4', 'c752b40a-f68b-4f7e-b7b8-26dda2494b1d', 'en', 'pound', 'lb', 'pounds', 'lbs'),
('55aeb977-b41f-4d0c-b8c0-df0b66ac991a', 'c752b40a-f68b-4f7e-b7b8-26dda2494b1d', 'de', 'Pfund', 'lb', NULL, NULL),
-- 13. Gram
('c4e31f89-ee0f-4e36-a4f4-a884b1da655f', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 'en', 'gram', 'g', 'grams', NULL),
('139fd5d3-4f08-4b10-96cf-6d619555a3bd', 'a736bf0e-e8a4-4605-a173-c710bfa0a4e4', 'de', 'Gramm', 'g', NULL, NULL),
-- 14. Kilogram
('5e5bdde4-d4cd-4dd4-8803-ea800887edca', 'e0b04679-da06-4ff0-b8f4-c79964700b0f', 'en', 'kilogram', 'kg', 'kilograms',
 NULL),
('eed6678c-39f7-4987-967a-b2e0a51007e1', 'e0b04679-da06-4ff0-b8f4-c79964700b0f', 'de', 'Kilogramm', 'kg', NULL, NULL),
-- 15. Milligram
('72176eef-596f-475d-ac2a-7b4226a40d59', 'f185b324-5871-4ca9-a45d-b90f4bb7c867', 'en', 'milligram', 'mg', 'milligrams',
 NULL),
('6316c805-9eb0-4001-a44b-99d3f5bd6d6a', 'f185b324-5871-4ca9-a45d-b90f4bb7c867', 'de', 'Milligramm', 'mg', NULL, NULL),
-- 16. Centimeter
('4633eec3-b7ee-4fb0-a0cc-f3f446651543', '9763afcc-3c16-4e92-a172-28dd26c1e555', 'en', 'centimeter', 'cm',
 'centimeters', NULL),
('35d5e2e7-02cb-45ff-b54c-59c7d05781c6', '9763afcc-3c16-4e92-a172-28dd26c1e555', 'de', 'Zentimeter', 'cm', NULL, NULL),
-- 17. Meter
('20bae5c5-23e1-45b2-b463-71dc87409740', '771280ac-2b4d-4bf1-99eb-4df945a90ce4', 'en', 'meter', 'm', 'meters', NULL),
('39958b29-5954-42bd-a533-575c9a798576', '771280ac-2b4d-4bf1-99eb-4df945a90ce4', 'de', 'Meter', 'm', NULL, NULL),
-- 18. Inch
('a5d1ee4a-bc93-4c18-806c-cf6f5f6046f7', 'ef519ac6-9dd5-4805-b5df-dc99ef179c76', 'en', 'inch', 'in', 'inches', NULL),
('9da52c98-d3c0-4e18-ba92-6dd20be83b5f', 'ef519ac6-9dd5-4805-b5df-dc99ef179c76', 'de', 'Zoll', NULL, NULL, NULL),
-- 19. Foot
('8e926139-c6ac-4e73-8092-cd7d2a30bd7d', '0fd704fe-6fe6-4b17-99c1-f85badedc59b', 'en', 'foot', 'ft', 'feet', NULL),
('52cb46d1-c321-43b6-87f7-7cd182ff5b25', '0fd704fe-6fe6-4b17-99c1-f85badedc59b', 'de', 'Fuß', NULL, 'Füße', NULL),
-- 20. Slice
('341e5866-6b29-43b7-add2-5a5f22a6f0d6', 'fe38d325-b2e2-403f-9656-7c977286766c', 'en', 'slice', NULL, 'slices', NULL),
('7c90cd18-469a-4815-af7e-181417363bd6', 'fe38d325-b2e2-403f-9656-7c977286766c', 'de', 'Scheibe', NULL, 'Scheiben',
 NULL),
-- 21. Pinch
('1bd998ca-d30c-45e6-8e88-66c828345e0a', '91a4bc6f-53b0-4263-b984-1a757eb720bd', 'en', 'pinch', NULL, 'pinches', NULL),
('749c66be-7f53-46fc-89b5-6fd0b308176a', '91a4bc6f-53b0-4263-b984-1a757eb720bd', 'de', 'Prise', NULL, 'Prisen', NULL),
-- 22. Dash
('0061c2ea-5794-4e95-aa15-54168e3f9ef6', '2e2f14cd-0600-40b1-b767-e4dd809cb49d', 'en', 'dash', NULL, 'dashes', NULL),
('1cac568d-f260-486b-aeee-83901177a135', '2e2f14cd-0600-40b1-b767-e4dd809cb49d', 'de', 'Schuss', NULL, 'Schüsse', NULL),
-- 23. Clove
('345bbba1-3383-4df6-87e3-aa09c06f3df3', '2fb77aea-32a9-4fcc-b9b8-406b98decbe3', 'en', 'clove', NULL, 'cloves', NULL),
('4d911870-aae3-4a34-b815-1bda5a2238cb', '2fb77aea-32a9-4fcc-b9b8-406b98decbe3', 'de', 'Zehe', NULL, 'Zehen', NULL),
-- 24. Stick
('52136b69-6162-48b6-a88a-43ea8b03bc4c', 'd4b3cec7-72bc-4cfd-9b09-f8e3f66f85b1', 'en', 'stick', NULL, 'sticks', NULL),
('6a21fc5a-1132-4afa-8d44-3d75e070d3f3', 'd4b3cec7-72bc-4cfd-9b09-f8e3f66f85b1', 'de', 'Stange', NULL, 'Stangen', NULL),
-- 25. Head
('d0bcd2d4-5174-4f75-9a58-fd85adc6ddf6', '03f0bae7-bb4b-40d1-9f91-e18869aa6b36', 'en', 'head', NULL, 'heads', NULL),
('4d99dabc-0aee-4571-a03a-f4339c3caa10', '03f0bae7-bb4b-40d1-9f91-e18869aa6b36', 'de', 'Kopf', NULL, 'Köpfe', NULL),
-- 26. Bunch
('121a79dd-7d2d-4694-bf0e-8b932194c06f', 'b8daf91b-99ac-41f1-9174-2647d7445d3d', 'en', 'bunch', NULL, 'bunches', NULL),
('ed6d43d0-8c0e-468d-9fea-700329c25dc9', 'b8daf91b-99ac-41f1-9174-2647d7445d3d', 'de', 'Bund', NULL, 'Bünde', NULL),
-- 27. Piece
('cd2d31f4-501e-4ec2-9902-aff794e1706e', '45b7874e-8aea-475c-9fd9-a05578504c7d', 'en', 'piece', NULL, 'pieces', NULL),
('5e0399b2-f8a6-44d0-a448-43a99d0c7f65', '45b7874e-8aea-475c-9fd9-a05578504c7d', 'de', 'Stück', NULL, 'Stücke', NULL),
-- 28. Sprig
('1c16662f-a476-4bde-b5b2-fbb73f090aae', '5f440eb7-b8d4-4020-ab08-b51adcdb2020', 'en', 'sprig', NULL, 'sprigs', NULL),
('ca8ec9f9-ace7-48e8-9b15-eeb400167c59', '5f440eb7-b8d4-4020-ab08-b51adcdb2020', 'de', 'Zweig', NULL, 'Zweige', NULL),
-- 29. Beet
('b075f5c3-2632-42ef-a07e-04351ab41633', '24f0f1c2-d108-4b18-a594-c78d151cd225', 'en', 'beet', NULL, 'beets', NULL),
('73ae3c52-3907-481f-a038-6b41d6e9cfd0', '24f0f1c2-d108-4b18-a594-c78d151cd225', 'de', 'Beete', NULL, NULL, NULL),
-- 30. Centiliter
('eb4c1ae4-7769-404b-8bf9-4c5e6fc60c41', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', 'en', 'centiliter', 'cl',
 'centiliters', NULL),
('9c133d62-c426-41e4-bf5e-3e055b3cfa7d', '751cb65c-af4b-4dac-bb5d-a01ccf0dcf4d', 'de', 'Zentiliter', 'cl', NULL, NULL),
-- 31. Thick Slice
('df89f039-e449-4eb1-abd7-150b5b5b3f0a', '751b56db-d0fc-4c73-90a1-088b3c9147f7', 'en', 'thick slice', NULL,
 'thick slices', NULL),
('ed865717-c858-4c95-9ab5-fdadecd7e6da', '751b56db-d0fc-4c73-90a1-088b3c9147f7', 'de', 'dicke Scheibe', NULL,
 'dicke Scheiben', NULL),
-- 32. Thin Slice
('b8bf8c64-df7c-4ad9-84ad-7a8574b83d14', 'c26ddf2e-c762-4136-90f8-239b1a4d70d6', 'en', 'thin slice', NULL,
 'thin slices', NULL),
('b8c1ddbc-881b-4970-8253-1ea406e57802', 'c26ddf2e-c762-4136-90f8-239b1a4d70d6', 'de', 'dünne Scheibe', NULL,
 'dünne Scheiben', NULL),
-- 33. Corner
('67d745f2-e95c-40c1-bc76-241e759d9cf5', 'd33b6fdf-f71c-49e6-acb5-4a038cd260cb', 'en', 'corner', NULL, 'corners', NULL),
('7102ac72-1db5-44b0-ad7b-b0a50d4252b5', 'd33b6fdf-f71c-49e6-acb5-4a038cd260cb', 'de', 'Ecke', NULL, 'Ecken', NULL),
-- 34. Few
('92563e54-79c0-4771-9e7a-c76d5cf8f540', 'bb6a51ca-28d2-47ad-bb52-5d976d43cf40', 'en', 'few', NULL, NULL, NULL),
('309e9987-970b-407b-9c28-fb8bf96b0578', 'bb6a51ca-28d2-47ad-bb52-5d976d43cf40', 'de', 'einige', NULL, NULL, NULL),
-- 35. Few Stalks
('87491df2-9c24-45d1-9481-c53aecac02a6', '7eb9c0c7-8a5e-4d72-8e59-ac9d3511344c', 'en', 'few stalks', NULL, NULL, NULL),
('d3c273ed-ee50-4fd4-a234-704e9cedba5d', '7eb9c0c7-8a5e-4d72-8e59-ac9d3511344c', 'de', 'einige Stiele', NULL, NULL,
 NULL),
-- 36. Level Tablespoon
('d0735fcf-3c41-41b5-aa23-a6acc743b5b5', 'a1acca17-d836-430d-b41b-fd633881e967', 'en', 'level tablespoon', NULL,
 'level tablespoons', NULL),
('6d873f20-21fb-4473-a19e-15bc1fecc079', 'a1acca17-d836-430d-b41b-fd633881e967', 'de', 'Esslöffel (gestrichen)',
 'EL (gestrichen)', NULL, NULL),
-- 37. Heaped Tablespoon
('d8b918c1-41d4-4da7-8456-e944b34dfd18', 'e0202667-4523-4971-8e83-c22cab7a62ea', 'en', 'heaped tablespoon', NULL,
 'heaped tablespoons', NULL),
('69fa988e-496d-4927-a478-2a5af849e0ea', 'e0202667-4523-4971-8e83-c22cab7a62ea', 'de', 'Esslöffel (gehäuft)',
 'EL (gehäuft)', NULL, NULL),
-- 38. Some
('cb17c109-c35f-48f3-9602-1e7587accac9', '76881d1c-c5af-4118-acca-67ef952bbbcf', 'en', 'some', NULL, NULL, NULL),
('6e33ba8a-069d-4769-b8ae-3a3ff30f4b75', '76881d1c-c5af-4118-acca-67ef952bbbcf', 'de', 'etwas', NULL, NULL, NULL),
-- 39. Possibly
('1a997a05-7cfe-4f4b-9553-c126ff7b2afe', 'd1feeb8c-8c31-41ae-af9f-4a4dfe07eb87', 'en', 'possibly', NULL, NULL, NULL),
('0c6ef7b1-401a-40af-b9fc-d4f06307fb65', 'd1feeb8c-8c31-41ae-af9f-4a4dfe07eb87', 'de', 'eventuell', 'evtl.', NULL,
 NULL),
-- 40. Extra
('ca355772-f63d-4cdc-885b-3801e4e22b13', '61c8dcc4-3329-4dd4-b128-757fb29607d8', 'en', 'extra', NULL, NULL, NULL),
('5e35252b-66e3-4008-a359-3753c6eb02f4', '61c8dcc4-3329-4dd4-b128-757fb29607d8', 'de', 'extra', NULL, NULL, NULL),
-- 41. Small Cask
('66229b54-a5be-4b37-974a-9c07818df233', '0be85e8b-927f-4ba8-b4f4-d8f5324b863f', 'en', 'small cask', NULL,
 'small casks', NULL),
('6aba4796-a42b-4165-bb27-f8dd1fb2cee1', '0be85e8b-927f-4ba8-b4f4-d8f5324b863f', 'de', 'Fässchen', NULL, NULL, NULL),
-- 42. Small Bottle
('ffe76a55-69b1-4c36-b1a6-ad97d95e8f5b', '11d57813-1037-4a73-9568-46b68a9bd83f', 'en', 'small bottle', NULL,
 'small bottles', NULL),
('2c5d7d52-13b9-4a19-8761-279a6184a0ec', '11d57813-1037-4a73-9568-46b68a9bd83f', 'de', 'Fläschchen', NULL, NULL, NULL),
-- 43. Large Bunch
('dbc6f2e3-25e8-4ac2-81b3-2bb4373e419b', '62fc9632-d487-45bd-ae69-0dfc1bd969e4', 'en', 'large bunch', NULL,
 'large bunches', NULL),
('bbfb110e-4abe-49bb-9025-a8799d704146', '62fc9632-d487-45bd-ae69-0dfc1bd969e4', 'de', 'großer Bund', NULL,
 'große Bünde', NULL),
-- 44. Large Can
('b9a67f25-c0ac-4f37-bb9f-c6d200d63ccb', 'd53bae61-e70f-4d51-be1a-84fb4aa55123', 'en', 'large can', NULL, 'large cans',
 NULL),
('d9417537-eb51-41bd-95a7-3b2d68627b13', 'd53bae61-e70f-4d51-be1a-84fb4aa55123', 'de', 'große Dose', NULL,
 'große Dosen', NULL),
-- 45. Large Bottle
('db842cb1-7235-487d-9e4d-1099bbf5198e', 'faa57328-a6c4-478d-b1e3-1136be185182', 'en', 'large bottle', NULL,
 'large bottles', NULL),
('51e193ab-c4df-49a4-8343-b81351ee492a', 'faa57328-a6c4-478d-b1e3-1136be185182', 'de', 'große Flasche', NULL,
 'große Flaschen', NULL),
-- 46. Large Glass
('c363c7ea-f3ba-4010-a9bc-4a6c3645e132', '07f28ed5-5bfc-476c-ac16-15b9aac63971', 'en', 'large glass', NULL,
 'large glasses', NULL),
('915600de-f031-4f77-8847-53f65bf5c6c5', '07f28ed5-5bfc-476c-ac16-15b9aac63971', 'de', 'großes Glas', NULL,
 'große Gläser', NULL),
-- 47. Large Head
('ee90e0dd-6324-4676-a06c-cb1d1014538b', '45fb169a-9683-42ba-9aee-9c99d113f2d3', 'en', 'large head', NULL,
 'large heads', NULL),
('da42f854-8cf3-45d6-b936-154523fbe7eb', '45fb169a-9683-42ba-9aee-9c99d113f2d3', 'de', 'großer Kopf', NULL,
 'große Köpfe', NULL),
-- 48. Large Slice
('a3c6e5f9-2bca-4fd5-9433-819591301067', '0ee651c4-b2b9-4167-af88-735565fb0bc1', 'en', 'large slice', NULL,
 'large slices', NULL),
('2645da6a-8fb5-49b3-94d3-b61d0cf6ab1d', '0ee651c4-b2b9-4167-af88-735565fb0bc1', 'de', 'große Scheibe', NULL,
 'große Scheiben', NULL),
-- 49. Large Piece
('4109d5b6-0df9-4082-baeb-75b787974e50', 'e712f57f-af6f-42df-9328-85214efc630b', 'en', 'large piece', NULL,
 'large pieces', NULL),
('0c9d52c3-eb57-48ee-9d92-4bfd5bec98ea', 'e712f57f-af6f-42df-9328-85214efc630b', 'de', 'großes Stück', NULL,
 'große Stücke', NULL),
-- 50. Large
('bede04b9-6a3e-45dc-951b-52ddf23d2bed', '22ee4c8a-a7a3-46b1-ad14-7c421fd8e1df', 'en', 'large', NULL, NULL, NULL),
('d5c24f99-53c1-4731-a146-0f0d82178698', '22ee4c8a-a7a3-46b1-ad14-7c421fd8e1df', 'de', 'groß', NULL, NULL, NULL),
('e35d1010-f445-4e2a-805f-58795b34577a', '22ee4c8a-a7a3-46b1-ad14-7c421fd8e1df', 'de', 'großer', NULL, NULL, NULL),
('833c1849-6fe3-4efa-8468-aa30c6a4d649', '22ee4c8a-a7a3-46b1-ad14-7c421fd8e1df', 'de', 'große', NULL, NULL, NULL),
('9f3a6f2c-40b0-426c-8c54-68c34630a3ae', '22ee4c8a-a7a3-46b1-ad14-7c421fd8e1df', 'de', 'großes', NULL, NULL, NULL),
-- 51. Half
('d5aa9129-06f4-4981-b6c0-70a618e49968', '1e73ff7f-5f70-4b28-8b26-a132a9e33c77', 'en', 'half', NULL, 'halves', NULL),
('351f574e-b730-450e-a748-79627116ef59', '1e73ff7f-5f70-4b28-8b26-a132a9e33c77', 'de', 'halbe', NULL, NULL, NULL),
-- 52. Handful
('3d6233dd-ddb6-4ffd-8e44-677778230ca5', 'ee99d667-1498-4e64-ad46-cf50c9aaa541', 'en', 'handful', NULL, 'handfuls',
 NULL),
('d9b5f726-f385-4535-845d-84d7143e2d23', 'ee99d667-1498-4e64-ad46-cf50c9aaa541', 'de', 'Handvoll', NULL, NULL, NULL),
-- 53. Straw
('ac9dd198-d2d7-449a-bf7f-092d5bb26c68', '5584b693-56e3-4154-ae44-106c8736f5e1', 'en', 'straw', NULL, 'straws', NULL),
('74844aed-acc6-44c5-90b0-64eefff5fadb', '5584b693-56e3-4154-ae44-106c8736f5e1', 'de', 'Halm', NULL, 'Halme', NULL),
-- 54. Crate
('6c54cde7-dd6c-4261-87c4-d70e8078acf4', '9743006b-45a2-4731-a1fc-6b5eab4402ea', 'en', 'crate', NULL, 'crates', NULL),
('86ec1202-c22e-410b-86c0-bef30333c563', '9743006b-45a2-4731-a1fc-6b5eab4402ea', 'de', 'Kasten', NULL, 'Kästen', NULL),
-- 55. Small Bunch
('0a8cfef6-2c71-4304-8edc-57aa6280a69b', '5e40c7e7-6eee-45ca-bc1e-69571b307e50', 'en', 'small bunch', NULL,
 'small bunches', NULL),
('7d8a217b-c33b-4f63-9dee-07ad11255ccb', '5e40c7e7-6eee-45ca-bc1e-69571b307e50', 'de', 'kleiner Bund', NULL,
 'kleine Bünde', NULL),
-- 56. Small Can
('68e45a11-ade4-4634-a906-dcf135b22ca8', '4a7ef927-7790-4ef5-a7ce-1b5fdbe89aa1', 'en', 'small can', NULL, 'small cans',
 NULL),
('823a9321-6a95-4c6d-8dfd-40407e442b6e', '4a7ef927-7790-4ef5-a7ce-1b5fdbe89aa1', 'de', 'kleine Dose', NULL,
 'kleine Dosen', NULL),
-- 57. Small Bottle
('e27f4011-8b65-4a17-8f1a-3b7dd9da214f', 'a002f22c-fcad-4e41-99b3-5f0582bed8dc', 'en', 'small bottle', NULL,
 'small bottles', NULL),
('699750f5-05f4-4d4f-91d3-09a9d61edd64', 'a002f22c-fcad-4e41-99b3-5f0582bed8dc', 'de', 'kleine Flasche', NULL,
 'kleine Flaschen', NULL),
-- 58. Small Glass
('7f0e1056-473d-438d-bb80-44af56ab6188', '1f626583-b4bb-40ba-91c4-37c6776224fa', 'en', 'small glass', NULL,
 'small glasses', NULL),
('eecb1195-1e5a-4b5c-aa40-39914c3dec91', '1f626583-b4bb-40ba-91c4-37c6776224fa', 'de', 'kleines Glas', NULL,
 'kleine Gläser', NULL),
-- 59. Small Head
('e77962bf-9250-44e2-9b75-a4fa9f1a2e13', '9b477e50-86e6-4db1-82a0-d60ade975de3', 'en', 'small head', NULL,
 'small heads', NULL),
('a6bc151a-7f35-4ea0-9c84-704dde94030f', '9b477e50-86e6-4db1-82a0-d60ade975de3', 'de', 'kleiner Kopf', NULL,
 'kleine Köpfe', NULL),
-- 60. Small Slice
('d8996420-c529-4ffe-ac9f-bb5bb3819c46', '4905c0e6-ba90-401a-a8bb-3f3d9aa2eee8', 'en', 'small slice', NULL,
 'small slices', NULL),
('9b0031f0-43d9-4a84-bac2-8de256ae05fa', '4905c0e6-ba90-401a-a8bb-3f3d9aa2eee8', 'de', 'kleine Scheibe', NULL,
 'kleine Scheiben', NULL),
-- 61. Small Piece
('d00ae874-188e-4ddc-84bd-7a7863e01400', '864da7e8-b13b-441d-a938-764eb37e9514', 'en', 'small piece', NULL,
 'small pieces', NULL),
('b3739a60-faed-407a-ba03-b02841719177', '864da7e8-b13b-441d-a938-764eb37e9514', 'de', 'kleines Stück', NULL,
 'kleine Stücke', NULL),
-- 62. Small
('67b4164a-8d23-4d88-b603-d79c9039aa69', '64211f2e-bd92-454b-beb4-778f7ec8aa3a', 'en', 'small', NULL, NULL, NULL),
('66c7e8cc-1ce5-4ed4-8164-4e4bdd684c9a', '64211f2e-bd92-454b-beb4-778f7ec8aa3a', 'de', 'klein', NULL, NULL, NULL),
('c0248325-d249-4df3-b455-e7e5f7f84399', '64211f2e-bd92-454b-beb4-778f7ec8aa3a', 'de', 'kleine', NULL, NULL, NULL),
('8cdfc527-96ce-457a-a263-25e66f962a30', '64211f2e-bd92-454b-beb4-778f7ec8aa3a', 'de', 'kleiner', NULL, NULL, NULL),
('9817bd8c-6978-49e6-a35e-456c8493deb7', '64211f2e-bd92-454b-beb4-778f7ec8aa3a', 'de', 'kleines', NULL, NULL, NULL),
-- 63. Grain
('804667f1-7519-4ae7-8e18-1fdfa5d129bf', '5dc59434-1312-4870-936f-2058b433f7a8', 'en', 'grain', NULL, 'grains', NULL),
('d4cc7f73-3ce9-4826-a3bc-ebba45eeec88', '5dc59434-1312-4870-936f-2058b433f7a8', 'de', 'Korn', NULL, 'Körner', NULL),
-- 64. More
('f7c0dbee-982b-4f71-97a7-e54571b7207d', 'a4083367-604c-456b-998c-91b7a063eb4e', 'en', 'more', NULL, NULL, NULL),
('b5fd3047-859b-4314-8e44-22968832607a', 'a4083367-604c-456b-998c-91b7a063eb4e', 'de', 'mehr', NULL, NULL, NULL),
-- 65. As Desired
('663b2ee9-565a-4817-9537-565d32d0c9f8', '3c51d367-17ea-490e-91c1-ac8d5bb295ac', 'en', 'as desired', NULL, NULL, NULL),
('c5357135-ae2a-4c4d-a5f6-06254e188cf3', '3c51d367-17ea-490e-91c1-ac8d5bb295ac', 'de', 'nach Belieben', 'n.B.', NULL,
 NULL),
-- 66. Pair
('e83107f3-dc00-40f4-8a9c-c1f7eafca4f2', '08c67101-060b-4675-b769-a714721d55af', 'en', 'pair', NULL, 'pairs', NULL),
('aa93bd94-f9b6-45f7-a6f9-8f4db328d050', '08c67101-060b-4675-b769-a714721d55af', 'de', 'Paar', NULL, 'Paare', NULL),
-- 67. Packet
('cf0fe9eb-b1b1-4c50-9dc8-9c2fe23820b4', 'ad6028be-97a2-41a1-84ba-5b06f75fed83', 'en', 'packet', NULL, 'packets', NULL),
('664adc0d-f0f6-450f-9953-ac16aca2fbb4', 'ad6028be-97a2-41a1-84ba-5b06f75fed83', 'de', 'Paket', NULL, 'Pakete', NULL),
-- 68. Small Packet
('0fe8645c-02ba-4072-93d4-0a050d937ec8', 'eb848f99-961a-4877-b4ab-b269c1bf9197', 'en', 'small packet', NULL,
 'small packets', NULL),
('0c43001c-3e8b-4634-8292-fe619fcdc1cc', 'eb848f99-961a-4877-b4ab-b269c1bf9197', 'de', 'Päckchen', NULL, NULL, NULL),
-- 69. Plate
('70d79b6f-d4bf-487b-af12-201450be12bd', 'cfbe00fb-597c-43f2-adde-893b628d26a9', 'en', 'plate', NULL, 'plates', NULL),
('a61da81d-502d-4d8f-9345-6eb72ece1a79', 'cfbe00fb-597c-43f2-adde-893b628d26a9', 'de', 'Platte', NULL, 'Platten', NULL),
-- 70. Portion
('b39a38ad-e096-45ca-bb34-6fea42a5393d', '97cd27a3-ac0a-4925-86ee-b2274ced68ef', 'en', 'portion', NULL, 'portions',
 NULL),
('49681051-377f-458b-b4c9-8c7710bfd6e1', '97cd27a3-ac0a-4925-86ee-b2274ced68ef', 'de', 'Portion', NULL, 'Portionen',
 NULL),
-- 71. Percent
('58ec9a4b-7de7-4240-a048-27aec18c94e2', '3c8ef745-bc79-4351-a4f3-c1b60048ec26', 'en', 'percent', '%', NULL, NULL),
('114b892a-69f2-484a-9dae-4c7a5fd24459', '3c8ef745-bc79-4351-a4f3-c1b60048ec26', 'de', 'Prozent', '%', NULL, NULL),
-- 72. Bar
('03057b96-2879-42e3-86fc-3239a05f07bc', '5a5191fc-16ee-432c-8eeb-12f8182892c8', 'en', 'bar', NULL, 'bars', NULL),
('595e0886-308d-4330-bb12-701c6638a317', '5a5191fc-16ee-432c-8eeb-12f8182892c8', 'de', 'Riegel', NULL, NULL, NULL),
-- 73. Small Bowl
('894ba34d-2c90-4ac5-821c-01b8f67d65e1', '1f5054f7-f9cf-4f33-98d0-94a675bf777c', 'en', 'small bowl', NULL,
 'small bowls', NULL),
('fbc2ed87-d847-4d4a-bdc5-2a28c72c1f35', '1f5054f7-f9cf-4f33-98d0-94a675bf777c', 'de', 'Schälchen', NULL, NULL, NULL),
-- 74. Bowl
('9e7ea52c-50cf-4a99-84d6-5ad8c614e9d9', '6447557d-8ecb-4a7b-b9f1-9a3804419b63', 'en', 'bowl', NULL, 'bowls', NULL),
('46e7bbac-9c8d-47b5-9628-3018459c37f9', '6447557d-8ecb-4a7b-b9f1-9a3804419b63', 'de', 'Schale', NULL, 'Schalen', NULL),
-- 75. Splash
('4001af78-5819-4cff-ba42-f32f0d65eeab', '6ee44a2e-daf0-44b9-9530-58fe4a248a62', 'en', 'splash', NULL, 'splashes',
 NULL),
('597e06fb-dc2c-43a8-8228-b45c578a1190', '6ee44a2e-daf0-44b9-9530-58fe4a248a62', 'de', 'Spritzer', NULL, NULL, NULL),
-- 76. Stem
('c7db87f6-ff4c-46af-930a-ce635a23b6fc', '12132062-06c2-4ee2-94b4-d1142f78e188', 'en', 'stem', NULL, 'stems', NULL),
('5dbff7dc-1628-4e73-bb58-5d39e8d0dfd7', '12132062-06c2-4ee2-94b4-d1142f78e188', 'de', 'Stängel', NULL, NULL, NULL),
-- 77. Stalk
('88d5531c-a6e6-4ba8-883f-cab06e394817', '18a88c97-18f0-491a-9cbb-107f6ae43952', 'en', 'stalk', NULL, 'stalks', NULL),
('aab4ab51-917e-478f-a493-ea848189b99d', '18a88c97-18f0-491a-9cbb-107f6ae43952', 'de', 'Stiel', NULL, 'Stiele', NULL),
-- 78. Strip
('91a947af-b8cc-4264-8158-e287a0bd4888', 'f358814d-ac8f-48f2-afde-7daa8c86e5ac', 'en', 'strip', NULL, 'strips', NULL),
('8faeab48-8ee6-45d3-a963-b9e5f32e8f39', 'f358814d-ac8f-48f2-afde-7daa8c86e5ac', 'de', 'Streifen', NULL, 'Streifen',
 NULL),
-- 79. Tablet
('4c845e02-54a9-47e9-9d74-eae43323aa6c', 'b423c3cf-de1c-4fb4-b6b7-a72ad350b321', 'en', 'tablet', NULL, 'tablets', NULL),
('64409bf3-c748-49f6-aecc-df76ebaea181', 'b423c3cf-de1c-4fb4-b6b7-a72ad350b321', 'de', 'Tablette', NULL, 'Tabletten',
 NULL),
-- 80. Bar (of chocolate)
('5d26324c-dfd6-4394-bb1e-e5857c2db1a0', 'b1bfa99c-e555-406e-9391-3cafef907b9c', 'en', 'bar (of chocolate)', NULL,
 'bars (of chocolate)', NULL),
('a9793f0b-86b8-4b69-86b6-09e69c755b57', 'b1bfa99c-e555-406e-9391-3cafef907b9c', 'de', 'Tafel', NULL, 'Tafeln', NULL),
-- 81. Part
('c3edb19a-6214-4d0f-8110-12550d2de32d', 'fd42cfd3-0237-43da-86bb-71a9b1f5027c', 'en', 'part', NULL, 'parts', NULL),
('01d67bdb-9084-4c87-b238-b04b692ffaf5', 'fd42cfd3-0237-43da-86bb-71a9b1f5027c', 'de', 'Teil', NULL, 'Teile', NULL),
-- 82. Heaped Teaspoon
('d7bae510-e529-4bbc-8b57-56a5e82e3eff', '18ff2ed2-44ba-4f7b-b52e-57b757887528', 'en', 'heaped teaspoon', NULL,
 'heaped teaspoons', NULL),
('82095df8-be31-4b7c-98bd-c7816c444ced', '18ff2ed2-44ba-4f7b-b52e-57b757887528', 'de', 'Teelöffel (gehäuft)',
 'TL (gehäuft)', NULL, NULL),
-- 83. Level Teaspoon
('70c6d8b7-d5b7-485a-bd50-80cc94336d07', '638c3e65-0be4-46d0-942a-d590abb70e24', 'en', 'level teaspoon', NULL,
 'level teaspoons', NULL),
('12107dec-e983-4c4b-b457-10aa063cbc8f', '638c3e65-0be4-46d0-942a-d590abb70e24', 'de', 'Teelöffel (gestrichen)',
 'TL (gestrichen', NULL, NULL),
-- 84. Pot
('3e082a51-a792-43f6-afd0-e90f29eedc48', '58f4777a-fd99-4508-ad78-39d84089c5de', 'en', 'pot', NULL, 'pots', NULL),
('ced0ffb2-55fe-4672-ac25-74514a3a5374', '58f4777a-fd99-4508-ad78-39d84089c5de', 'de', 'Topf', NULL, 'Töpfe', NULL),
-- 85. Tube
('9544c186-0d8a-4b81-939e-6fb88cd8031b', '283c07e5-de59-4246-bbe6-971fba1c2cae', 'en', 'tube', NULL, 'tubes', NULL),
('1e8ada3e-f723-440e-b683-a75503783edc', '283c07e5-de59-4246-bbe6-971fba1c2cae', 'de', 'Tube', NULL, 'Tuben', NULL),
-- 86. Bag
('fa8c021d-cf30-41eb-b400-e0db10e0e4ec', '662a4f09-d8d0-4666-a53d-a49abc5cfcaa', 'en', 'bag', NULL, 'bags', NULL),
('07190c6f-4d59-4f2d-9f02-b787ff0c655a', '662a4f09-d8d0-4666-a53d-a49abc5cfcaa', 'de', 'Tüte', NULL, 'Tüten', NULL),
('5d0727d4-2ba6-4b84-b27e-572407e90e4a', '662a4f09-d8d0-4666-a53d-a49abc5cfcaa', 'de', 'Beutel', NULL, NULL, NULL),
-- 87. Much
('2d41f1dd-4b04-4df4-9a04-491c8c7436b9', '47cf4fff-b577-4af2-a292-1b37233f6fd8', 'en', 'much', NULL, NULL, NULL),
('4a74ede1-3af1-4342-b01f-ae9aff78eece', '47cf4fff-b577-4af2-a292-1b37233f6fd8', 'de', 'viel', NULL, NULL, NULL),
-- 88. Little
('ae6358ce-15ee-491f-8c58-3d50cc1d87ed', 'a52e7849-f5f5-4065-aaff-275d8f898659', 'en', 'little', NULL, NULL, NULL),
('96875217-8dbe-4636-82dd-1a47dcf39e1b', 'a52e7849-f5f5-4065-aaff-275d8f898659', 'de', 'wenig', NULL, NULL, NULL),
-- 89. Root
('cd6c2e8b-aa92-4580-8052-0584b6f7a30b', '6c00d1ee-1b35-494f-81f1-934cd839c1d7', 'en', 'root', NULL, 'roots', NULL),
('d88ad8dc-c992-4a7d-bcb1-280ec1cc4293', '6c00d1ee-1b35-494f-81f1-934cd839c1d7', 'de', 'Wurzel', NULL, 'Wurzeln', NULL),
-- 90. Whole
('e7bff45d-b33d-4a78-bef1-3dc9b950e978', '9dc5dea0-b234-4a25-8f09-0e0aea4e5e8c', 'en', 'whole', NULL, NULL, NULL),
('9d9bdc98-a3d0-4bef-a891-1f48ace9a63e', '9dc5dea0-b234-4a25-8f09-0e0aea4e5e8c', 'de', 'Ganze', NULL, NULL, NULL),
-- 91. Zest
('218f52b9-997f-4f62-a4e5-7c2f0f553ccb', 'aa21319e-3189-4adb-b8a1-d868bb482844', 'en', 'zest', NULL, 'zests', NULL),
('2faf1149-7097-4020-bf88-6880abdc1946', 'aa21319e-3189-4adb-b8a1-d868bb482844', 'de', 'Abrieb', NULL, NULL, NULL),
-- 92. Knife point
('86b2985d-d9bb-4367-b10b-a53a12a38766', '28835916-427b-4dd2-a3fd-a5ae7c4fb70a', 'en', 'knife point', NULL,
 'knife points', NULL),
('7cc4c5db-99a1-4dd2-adc6-8ee7816b77f8', '28835916-427b-4dd2-a3fd-a5ae7c4fb70a', 'de', 'Messerspitze', NULL,
 'Messerspitzen', NULL),
-- 93. Glass
('73bb83e7-80ad-48f7-b61d-485290c0f802', '4669e908-f2f1-425a-b3e4-474c69c142b7', 'en', 'glass', NULL, 'glasses', NULL),
('e9d3bf91-9111-41cb-862e-c6a4e5bee105', '4669e908-f2f1-425a-b3e4-474c69c142b7', 'de', 'Glas', NULL, 'Gläser', NULL),
-- 94. Medium size
('80dc92eb-fd96-4769-a5f2-82dcd6bcc137', '74b7f6d6-1c2e-4bc9-bfcd-d0f63ddf6bbb', 'en', 'medium size', NULL, NULL, NULL),
('052ffb0e-e928-4030-8f57-9037a894fb55', '74b7f6d6-1c2e-4bc9-bfcd-d0f63ddf6bbb', 'de', 'mittel groß', NULL,
 'mittel große', NULL),
('c0638e81-d453-44a8-ab1b-91508fc30ba5', '74b7f6d6-1c2e-4bc9-bfcd-d0f63ddf6bbb', 'de', 'mittel große', NULL,
 'mittel große', NULL),
('a17a67cc-3e63-4db8-8344-380ae06769a5', '74b7f6d6-1c2e-4bc9-bfcd-d0f63ddf6bbb', 'de', 'mittel großer', NULL,
 'mittel große', NULL),
-- 95. Drop
('b9847ff9-fca3-42ac-94a5-352f2ff0d181', '83499a86-8cc1-4b8a-aac1-f15546c0cb6d', 'en', 'drop', NULL, 'drops', NULL),
('54bfac9d-77fc-4ce6-87ff-eddc5ca84a91', '83499a86-8cc1-4b8a-aac1-f15546c0cb6d', 'de', 'Tropfen', NULL, 'Tropfen',
 NULL),
-- 96. Cube
('76c7ca13-6937-4f42-888d-e427c269c819', '4773a2fe-7f3a-42f3-93d1-a7a8ce9578bf', 'en', 'cube', NULL, 'cubes', NULL),
('0a0d932f-ef5a-4aa5-be4f-5083283960d0', '4773a2fe-7f3a-42f3-93d1-a7a8ce9578bf', 'de', 'Würfel', NULL, NULL, NULL),
-- 97. Cube
('5ee84582-7d8c-4b0e-8654-b9174bf95b31', 'e89a1031-2a01-4d22-9069-81aef2129406', 'en', 'sheet', NULL, 'sheets', NULL),
('0ff936f9-6dbe-4970-afbd-0b436f80adf4', 'e89a1031-2a01-4d22-9069-81aef2129406', 'de', 'Blatt', NULL, 'Blätter', NULL),
-- 98. Thick
('c24feb67-2652-4350-8114-3e4ae89cc092', '393ec015-d36c-4870-8311-de7271ce5a5b', 'en', 'thick', NULL, NULL, NULL),
('42048179-4a26-4fb3-8798-0032b6e3ee20', '393ec015-d36c-4870-8311-de7271ce5a5b', 'de', 'dicke', NULL, '', NULL),
('ee77a814-7598-4de9-bcdc-459f7fcf8f35', '393ec015-d36c-4870-8311-de7271ce5a5b', 'de', 'dicker', NULL, 'dicke', NULL),
('4fb09575-8b52-4088-b74a-daeef983612d', '393ec015-d36c-4870-8311-de7271ce5a5b', 'de', 'dickes', NULL, 'dicke', NULL);
