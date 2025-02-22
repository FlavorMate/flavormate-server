-- Insert unit labels for English and German
INSERT INTO public.unit_localizations (unit_ref, language, label_sg, label_sg_abrv, label_pl, label_pl_abrv)
VALUES
-- Units 1 to 91

-- 1. Teaspoon
(1, 'en', 'teaspoon', 'tsp', 'teaspoons', NULL),
(1, 'de', 'Teelöffel', 'TL', NULL, NULL),

-- 2. Tablespoon
(2, 'en', 'tablespoon', 'tbsp', 'tablespoons', NULL),
(2, 'de', 'Esslöffel', 'EL', NULL, NULL),

-- 3. Cup
(3, 'en', 'cup', NULL, 'cups', NULL),
(3, 'de', 'Tasse', NULL, 'Tassen', NULL),
(3, 'de', 'Becher', NULL, NULL, NULL),

-- 4. Pint
(4, 'en', 'pint', 'pt', 'pints', NULL),
(4, 'de', 'Pint', NULL, NULL, NULL),

-- 5. Quart
(5, 'en', 'quart', 'qt', 'quarts', NULL),
(5, 'de', 'Quart', NULL, NULL, NULL),

-- 6. Gallon
(6, 'en', 'gallon', 'gal', 'gallons', NULL),
(6, 'de', 'Gallone', NULL, 'Gallonen', NULL),

-- 7. Milliliter
(7, 'en', 'milliliter', 'ml', 'milliliters', NULL),
(7, 'de', 'Milliliter', 'ml', NULL, NULL),

-- 8. Liter
(8, 'en', 'liter', 'l', 'liters', NULL),
(8, 'de', 'Liter', 'l', NULL, NULL),

-- 9. Deciliter
(9, 'en', 'deciliter', 'dl', 'deciliters', NULL),
(9, 'de', 'Deziliter', 'dl', NULL, NULL),

-- 10. Fluid Ounce
(10, 'en', 'fluid ounce', 'fl oz', 'fluid ounces', NULL),
(10, 'de', 'Flüssigunze', NULL, NULL, NULL),

-- 11. Ounce
(11, 'en', 'ounce', 'oz', 'ounces', NULL),
(11, 'de', 'Unze', 'oz', 'Unzen', NULL),

-- 12. Pound
(12, 'en', 'pound', 'lb', 'pounds', 'lbs'),
(12, 'de', 'Pfund', 'lb', NULL, NULL),

-- 13. Gram
(13, 'en', 'gram', 'g', 'grams', NULL),
(13, 'de', 'Gramm', 'g', NULL, NULL),

-- 14. Kilogram
(14, 'en', 'kilogram', 'kg', 'kilograms', NULL),
(14, 'de', 'Kilogramm', 'kg', NULL, NULL),

-- 15. Milligram
(15, 'en', 'milligram', 'mg', 'milligrams', NULL),
(15, 'de', 'Milligramm', 'mg', NULL, NULL),

-- 16. Centimeter
(16, 'en', 'centimeter', 'cm', 'centimeters', NULL),
(16, 'de', 'Zentimeter', 'cm', NULL, NULL),

-- 17. Meter
(17, 'en', 'meter', 'm', 'meters', NULL),
(17, 'de', 'Meter', 'm', NULL, NULL),

-- 18. Inch
(18, 'en', 'inch', 'in', 'inches', NULL),
(18, 'de', 'Zoll', NULL, NULL, NULL),

-- 19. Foot
(19, 'en', 'foot', 'ft', 'feet', NULL),
(19, 'de', 'Fuß', NULL, 'Füße', NULL),

-- 20. Slice
(20, 'en', 'slice', NULL, 'slices', NULL),
(20, 'de', 'Scheibe', NULL, 'Scheiben', NULL),

-- 21. Pinch
(21, 'en', 'pinch', NULL, 'pinches', NULL),
(21, 'de', 'Prise', NULL, 'Prisen', NULL),

-- 22. Dash
(22, 'en', 'dash', NULL, 'dashes', NULL),
(22, 'de', 'Schuss', NULL, 'Schüsse', NULL),

-- 23. Clove
(23, 'en', 'clove', NULL, 'cloves', NULL),
(23, 'de', 'Zehe', NULL, 'Zehen', NULL),

-- 24. Stick
(24, 'en', 'stick', NULL, 'sticks', NULL),
(24, 'de', 'Stange', NULL, 'Stangen', NULL),

-- 25. Head
(25, 'en', 'head', NULL, 'heads', NULL),
(25, 'de', 'Kopf', NULL, 'Köpfe', NULL),

-- 26. Bunch
(26, 'en', 'bunch', NULL, 'bunches', NULL),
(26, 'de', 'Bund', NULL, 'Bünde', NULL),

-- 27. Piece
(27, 'en', 'piece', NULL, 'pieces', NULL),
(27, 'de', 'Stück', NULL, 'Stücke', NULL),

-- 28. Sprig
(28, 'en', 'sprig', NULL, 'sprigs', NULL),
(28, 'de', 'Zweig', NULL, 'Zweige', NULL),

-- 29. Beet
(29, 'en', 'beet', NULL, 'beets', NULL),
(29, 'de', 'Beete', NULL, NULL, NULL),

-- 30. Centiliter
(30, 'en', 'centiliter', 'cl', 'centiliters', NULL),
(30, 'de', 'Zentiliter', 'cl', NULL, NULL),

-- 31. Thick Slice
(31, 'en', 'thick slice', NULL, 'thick slices', NULL),
(31, 'de', 'dicke Scheibe', NULL, 'dicke Scheiben', NULL),

-- 32. Thin Slice
(32, 'en', 'thin slice', NULL, 'thin slices', NULL),
(32, 'de', 'dünne Scheibe', NULL, 'dünne Scheiben', NULL),

-- 33. Corner
(33, 'en', 'corner', NULL, 'corners', NULL),
(33, 'de', 'Ecke', NULL, 'Ecken', NULL),

-- 34. Few
(34, 'en', 'few', NULL, NULL, NULL),
(34, 'de', 'einige', NULL, NULL, NULL),

-- 35. Few Stalks
(35, 'en', 'few stalks', NULL, NULL, NULL),
(35, 'de', 'einige Stiele', NULL, NULL, NULL),

-- 36. Level Tablespoon
(36, 'en', 'level tablespoon', NULL, 'level tablespoons', NULL),
(36, 'de', 'Esslöffel (gestrichen)', 'EL (gestrichen)', NULL, NULL),

-- 37. Heaped Tablespoon
(37, 'en', 'heaped tablespoon', NULL, 'heaped tablespoons', NULL),
(37, 'de', 'Esslöffel (gehäuft)', 'EL (gehäuft)', NULL, NULL),

-- 38. Some
(38, 'en', 'some', NULL, NULL, NULL),
(38, 'de', 'etwas', NULL, NULL, NULL),

-- 39. Possibly
(39, 'en', 'possibly', NULL, NULL, NULL),
(39, 'de', 'eventuell', 'evtl.', NULL, NULL),

-- 40. Extra
(40, 'en', 'extra', NULL, NULL, NULL),
(40, 'de', 'extra', NULL, NULL, NULL),

-- 41. Small Cask
(41, 'en', 'small cask', NULL, 'small casks', NULL),
(41, 'de', 'Fässchen', NULL, NULL, NULL),

-- 42. Small Bottle
(42, 'en', 'small bottle', NULL, 'small bottles', NULL),
(42, 'de', 'Fläschchen', NULL, NULL, NULL),

-- 43. Large Bunch
(43, 'en', 'large bunch', NULL, 'large bunches', NULL),
(43, 'de', 'großer Bund', NULL, 'große Bünde', NULL),

-- 44. Large Can
(44, 'en', 'large can', NULL, 'large cans', NULL),
(44, 'de', 'große Dose', NULL, 'große Dosen', NULL),

-- 45. Large Bottle
(45, 'en', 'large bottle', NULL, 'large bottles', NULL),
(45, 'de', 'große Flasche', NULL, 'große Flaschen', NULL),

-- 46. Large Glass
(46, 'en', 'large glass', NULL, 'large glasses', NULL),
(46, 'de', 'großes Glas', NULL, 'große Gläser', NULL),

-- 47. Large Head
(47, 'en', 'large head', NULL, 'large heads', NULL),
(47, 'de', 'großer Kopf', NULL, 'große Köpfe', NULL),

-- 48. Large Slice
(48, 'en', 'large slice', NULL, 'large slices', NULL),
(48, 'de', 'große Scheibe', NULL, 'große Scheiben', NULL),

-- 49. Large Piece
(49, 'en', 'large piece', NULL, 'large pieces', NULL),
(49, 'de', 'großes Stück', NULL, 'große Stücke', NULL),

-- 50. Large
(50, 'en', 'large', NULL, NULL, NULL),
(50, 'de', 'groß', NULL, NULL, NULL),
(50, 'de', 'großer', NULL, NULL, NULL),
(50, 'de', 'große', NULL, NULL, NULL),
(50, 'de', 'großes', NULL, NULL, NULL),

-- 51. Half
(51, 'en', 'half', NULL, 'halves', NULL),
(51, 'de', 'halbe', NULL, NULL, NULL),

-- 52. Handful
(52, 'en', 'handful', NULL, 'handfuls', NULL),
(52, 'de', 'Handvoll', NULL, NULL, NULL),

-- 53. Straw
(53, 'en', 'straw', NULL, 'straws', NULL),
(53, 'de', 'Halm', NULL, 'Halme', NULL),

-- 54. Crate
(54, 'en', 'crate', NULL, 'crates', NULL),
(54, 'de', 'Kasten', NULL, 'Kästen', NULL),

-- 55. Small Bunch
(55, 'en', 'small bunch', NULL, 'small bunches', NULL),
(55, 'de', 'kleiner Bund', NULL, 'kleine Bünde', NULL),

-- 56. Small Can
(56, 'en', 'small can', NULL, 'small cans', NULL),
(56, 'de', 'kleine Dose', NULL, 'kleine Dosen', NULL),

-- 57. Small Bottle
(57, 'en', 'small bottle', NULL, 'small bottles', NULL),
(57, 'de', 'kleine Flasche', NULL, 'kleine Flaschen', NULL),

-- 58. Small Glass
(58, 'en', 'small glass', NULL, 'small glasses', NULL),
(58, 'de', 'kleines Glas', NULL, 'kleine Gläser', NULL),

-- 59. Small Head
(59, 'en', 'small head', NULL, 'small heads', NULL),
(59, 'de', 'kleiner Kopf', NULL, 'kleine Köpfe', NULL),

-- 60. Small Slice
(60, 'en', 'small slice', NULL, 'small slices', NULL),
(60, 'de', 'kleine Scheibe', NULL, 'kleine Scheiben', NULL),

-- 61. Small Piece
(61, 'en', 'small piece', NULL, 'small pieces', NULL),
(61, 'de', 'kleines Stück', NULL, 'kleine Stücke', NULL),

-- 62. Small
(62, 'en', 'small', NULL, NULL, NULL),
(62, 'de', 'klein', NULL, NULL, NULL),
(62, 'de', 'kleine', NULL, NULL, NULL),
(62, 'de', 'kleiner', NULL, NULL, NULL),
(62, 'de', 'kleines', NULL, NULL, NULL),

-- 63. Grain
(63, 'en', 'grain', NULL, 'grains', NULL),
(63, 'de', 'Korn', NULL, 'Körner', NULL),

-- 64. More
(64, 'en', 'more', NULL, NULL, NULL),
(64, 'de', 'mehr', NULL, NULL, NULL),

-- 65. As Desired
(65, 'en', 'as desired', NULL, NULL, NULL),
(65, 'de', 'nach Belieben', 'n.B.', NULL, NULL),

-- 66. Pair
(66, 'en', 'pair', NULL, 'pairs', NULL),
(66, 'de', 'Paar', NULL, 'Paare', NULL),

-- 67. Packet
(67, 'en', 'packet', NULL, 'packets', NULL),
(67, 'de', 'Paket', NULL, 'Pakete', NULL),

-- 68. Small Packet
(68, 'en', 'small packet', NULL, 'small packets', NULL),
(68, 'de', 'Päckchen', NULL, NULL, NULL),

-- 69. Plate
(69, 'en', 'plate', NULL, 'plates', NULL),
(69, 'de', 'Platte', NULL, 'Platten', NULL),

-- 70. Portion
(70, 'en', 'portion', NULL, 'portions', NULL),
(70, 'de', 'Portion', NULL, 'Portionen', NULL),

-- 71. Percent
(71, 'en', 'percent', '%', NULL, NULL),
(71, 'de', 'Prozent', '%', NULL, NULL),

-- 72. Bar
(72, 'en', 'bar', NULL, 'bars', NULL),
(72, 'de', 'Riegel', NULL, NULL, NULL),

-- 73. Small Bowl
(73, 'en', 'small bowl', NULL, 'small bowls', NULL),
(73, 'de', 'Schälchen', NULL, NULL, NULL),

-- 74. Bowl
(74, 'en', 'bowl', NULL, 'bowls', NULL),
(74, 'de', 'Schale', NULL, 'Schalen', NULL),

-- 75. Splash
(75, 'en', 'splash', NULL, 'splashes', NULL),
(75, 'de', 'Spritzer', NULL, NULL, NULL),

-- 76. Stem
(76, 'en', 'stem', NULL, 'stems', NULL),
(76, 'de', 'Stängel', NULL, NULL, NULL),

-- 77. Stalk
(77, 'en', 'stalk', NULL, 'stalks', NULL),
(77, 'de', 'Stiel', NULL, 'Stiele', NULL),

-- 78. Strip
(78, 'en', 'strip', NULL, 'strips', NULL),
(78, 'de', 'Streifen', NULL, 'Streifen', NULL),

-- 79. Tablet
(79, 'en', 'tablet', NULL, 'tablets', NULL),
(79, 'de', 'Tablette', NULL, 'Tabletten', NULL),

-- 80. Bar (of chocolate)
(80, 'en', 'bar (of chocolate)', NULL, 'bars (of chocolate)', NULL),
(80, 'de', 'Tafel', NULL, 'Tafeln', NULL),

-- 81. Part
(81, 'en', 'part', NULL, 'parts', NULL),
(81, 'de', 'Teil', NULL, 'Teile', NULL),

-- 82. Heaped Teaspoon
(82, 'en', 'heaped teaspoon', NULL, 'heaped teaspoons', NULL),
(82, 'de', 'Teelöffel (gehäuft)', 'TL (gehäuft)', NULL, NULL),

-- 83. Level Teaspoon
(83, 'en', 'level teaspoon', NULL, 'level teaspoons', NULL),
(83, 'de', 'Teelöffel (gestrichen)', 'TL (gestrichen', NULL, NULL),

-- 84. Pot
(84, 'en', 'pot', NULL, 'pots', NULL),
(84, 'de', 'Topf', NULL, 'Töpfe', NULL),

-- 85. Tube
(85, 'en', 'tube', NULL, 'tubes', NULL),
(85, 'de', 'Tube', NULL, 'Tuben', NULL),

-- 86. Bag
(86, 'en', 'bag', NULL, 'bags', NULL),
(86, 'de', 'Tüte', NULL, 'Tüten', NULL),
(86, 'de', 'Beutel', NULL, NULL, NULL),

-- 87. Much
(87, 'en', 'much', NULL, NULL, NULL),
(87, 'de', 'viel', NULL, NULL, NULL),

-- 88. Little
(88, 'en', 'little', NULL, NULL, NULL),
(88, 'de', 'wenig', NULL, NULL, NULL),

-- 89. Root
(89, 'en', 'root', NULL, 'roots', NULL),
(89, 'de', 'Wurzel', NULL, 'Wurzeln', NULL),

-- 90. Whole
(90, 'en', 'whole', NULL, NULL, NULL),
(90, 'de', 'Ganze', NULL, NULL, NULL),

-- 91. Zest
(91, 'en', 'zest', NULL, 'zests', NULL),
(91, 'de', 'Abrieb', NULL, NULL, NULL),

-- 92. Knife point
(92, 'en', 'knife point', NULL, 'knife points', NULL),
(92, 'de', 'Messerspitze', NULL, 'Messerspitzen', NULL),

-- 93. Glass
(93, 'en', 'glass', NULL, 'glasses', NULL),
(93, 'de', 'Glas', NULL, 'Gläser', NULL),

-- 94. Medium size
(94, 'en', 'medium size', NULL, NULL, NULL),
(94, 'de', 'mittel groß', NULL, 'mittel große', NULL),
(94, 'de', 'mittel große', NULL, 'mittel große', NULL),
(94, 'de', 'mittel großer', NULL, 'mittel große', NULL),

-- 95. Drop
(95, 'en', 'drop', NULL, 'drops', NULL),
(95, 'de', 'Tropfen', NULL, 'Tropfen', NULL),

-- 96. Cube
(96, 'en', 'cube', NULL, 'cubes', NULL),
(96, 'de', 'Würfel', NULL, NULL, NULL),

-- 97. Cube
(97, 'en', 'sheet', NULL, 'sheets', NULL),
(97, 'de', 'Blatt', NULL, 'Blätter', NULL),

-- 98. Thick
(98, 'en', 'thick', NULL, NULL, NULL),
(98, 'de', 'dicke', NULL, '', NULL),
(98, 'de', 'dicker', NULL, 'dicke', NULL),
(98, 'de', 'dickes', NULL, 'dicke', NULL);
