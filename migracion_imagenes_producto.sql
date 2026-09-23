-- ============================================================
-- FREDDY-FAZBEAR'S QUICK BITE
-- ------------------------------------------------------------
-- Migración: completar imagen de producto faltante
-- ------------------------------------------------------------
-- USA ESTE SCRIPT SI: ya tienes una base de datos FreddyQuickBite
-- con datos reales y NO quieres recrearla. Solo actualiza la
-- columna `imagen` de 31 productos. No borra ni recrea nada más.
--
-- Si vas a instalar el proyecto desde cero, no hace falta este
-- archivo: FreddyQuickBite.sql ya trae estos 31 UPDATE incluidos
-- en su seed data (sección "imagen de producto (parte 2)").
--
-- POR QUÉ HACÍA FALTA:
-- Comparando cada producto contra los archivos reales de
-- Resources/Productos, 31 de los 84 productos (37%) nunca
-- recibieron su UPDATE de imagen aunque el archivo correcto SÍ
-- existe en esa carpeta (ej. 'Root Beer Float.png',
-- 'Combo Golden Pizza-Burger.png'). UtilImagenes.producto() cae en
-- el respaldo genérico (Comidarealista.png) cuando `imagen` es
-- NULL, así que esos 31 productos se veían todos con la misma
-- imagen genérica en el catálogo, sin necesidad: el archivo ya
-- estaba ahí.
--
-- Es IDEMPOTENTE: puedes ejecutar este archivo más de una vez sin
-- que falle ni cambie nada distinto la segunda vez.
-- ============================================================

USE FreddyQuickBite;

UPDATE producto SET imagen = 'Burrito de Desayuno Grande' WHERE nombre = 'Burrito de Desayuno Grande';
UPDATE producto SET imagen = 'Pancakes Clasico' WHERE nombre = 'Pancakes Clásico';
UPDATE producto SET imagen = 'Pancakes con Miel de Maple' WHERE nombre = 'Pancakes con Miel de Maple';
UPDATE producto SET imagen = 'Plato Fazbear Clasico' WHERE nombre = 'Plato Fazbear Clásico';
UPDATE producto SET imagen = 'Bol de Acai del Pirata' WHERE nombre = 'Bol de Acaí del Pirata';
UPDATE producto SET imagen = 'Sundae de Helado' WHERE nombre = 'Sundae de Helado';
UPDATE producto SET imagen = 'Root Beer Float' WHERE nombre = 'Root Beer Float';
UPDATE producto SET imagen = 'Waffles de Chocolate' WHERE nombre = 'Waffles de Chocolate';
UPDATE producto SET imagen = 'Expresso Machiato' WHERE nombre = 'Expresso Machiato';
UPDATE producto SET imagen = 'Latte Clasico' WHERE nombre = 'Latte Clásico';
UPDATE producto SET imagen = 'Mocha Chocolate Iced' WHERE nombre = 'Mocha Chocolate Iced';
UPDATE producto SET imagen = 'Mocha Chocolate Iced Frio' WHERE nombre = 'Mocha Chocolate Iced (Frío)';
UPDATE producto SET imagen = 'Frappe de Caramelo Frio' WHERE nombre = 'Frappé de Caramelo (Frío)';
UPDATE producto SET imagen = 'Frappe de Caramelo con Helado' WHERE nombre = 'Frappé de Caramelo con Helado';
UPDATE producto SET imagen = 'Bebida de Fresa' WHERE nombre = 'Bebida de Fresa';
UPDATE producto SET imagen = 'Botin de Pirata de Foxy' WHERE nombre = 'Botín de Pirata de Foxy';
UPDATE producto SET imagen = 'Ponche de Frutas' WHERE nombre = 'Ponche de Frutas';
UPDATE producto SET imagen = 'Granizado de Arandano' WHERE nombre = 'Granizado de Arándano';
UPDATE producto SET imagen = 'Slushie de Lima' WHERE nombre = 'Slushie de Lima';
UPDATE producto SET imagen = 'Smoothie de Durazno' WHERE nombre = 'Smoothie de Durazno';
UPDATE producto SET imagen = 'Te Helado' WHERE nombre = 'Té Helado';
UPDATE producto SET imagen = 'Alitas de Foxy' WHERE nombre = 'Alitas de Foxy';
UPDATE producto SET imagen = 'Bocados de Maiz' WHERE nombre = 'Bocados de Maíz';
UPDATE producto SET imagen = 'Sarten de Queso' WHERE nombre = 'Sartén de Queso';
UPDATE producto SET imagen = 'Copa de Pastel de Chica' WHERE nombre = 'Copa de Pastel de Chica';
UPDATE producto SET imagen = 'Festin de Tacos de Bonnie' WHERE nombre = 'Festín de Tacos de Bonnie';
UPDATE producto SET imagen = 'Paquete de Papas Shadow' WHERE nombre = 'Paquete de Papas Shadow';
UPDATE producto SET imagen = 'Paquete de Pizza de Chica' WHERE nombre = 'Paquete de Pizza de Chica';
UPDATE producto SET imagen = 'Combo Golden Pizza-Burger' WHERE nombre = 'Combo Golden Pizza-Burger';
UPDATE producto SET imagen = 'Combo Bonnie-Nuggets' WHERE nombre = 'Combo Bonnie-Nuggets';
UPDATE producto SET imagen = 'Combo Freddy Fazbear' WHERE nombre = 'Combo Freddy Fazbear';

-- ------------------------------------------------------------
-- Verificación rápida (opcional, solo lectura): productos que
-- todavía quedan sin imagen (deben ser 0 después de este script,
-- salvo que agregues productos nuevos sin foto todavía):
-- SELECT nombre FROM producto WHERE imagen IS NULL ORDER BY nombre;
-- ------------------------------------------------------------
