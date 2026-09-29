-- ============================================================
-- BASE DE DATOS: FreddyQuickBite
-- Proyecto: Freddy Fazbear's Quick Bite - Pantalla de autoservicio
-- Versión: ORGANIZADA (Fase 3 - categorías Hamburguesas/Pizzas)
-- ============================================================
-- Este script ya integra las migraciones anteriores directamente
-- en la definición de las tablas y los datos iniciales, en vez de
-- dejarlas como pasos separados. Al ejecutarlo de cero se obtiene
-- la base de datos ya en su versión final y correcta.
--
-- Fase 3 agrega sp_migrar_categorias_hamburguesas_pizzas (sección
-- 4), que corrige las categorías heredadas "Desayunos"/"Almuerzos
-- y Cenas" para que coincidan con lo que ya filtra el Autoservicio
-- ("Hamburguesas"/"Pizzas"). El seed data (sección 5) la llama al
-- final, así que un install limpio con este script ya queda
-- correcto de una vez. El procedimiento se deja en el esquema (no
-- se elimina) porque es idempotente y también sirve para aplicar
-- el mismo arreglo contra una base de datos EXISTENTE, con datos
-- reales, sin tener que recrearla desde cero (ver
-- migracion_categorias_hamburguesas_pizzas.sql, que solo crea y
-- llama este procedimiento, sin el DROP DATABASE de aquí abajo).
--
-- Estructura del archivo:
--   1. Creación de la base de datos
--   2. Definición de tablas (DDL final)
--   3. Índices
--   4. Procedimientos, triggers y vistas
--   5. Datos iniciales (seed data)
--   6. Consultas de verificación (comentadas, opcionales)


-- ============================================================
-- 1. CREACIÓN DE LA BASE DE DATOS
-- ============================================================
DROP DATABASE IF EXISTS FreddyQuickBite;
CREATE DATABASE IF NOT EXISTS FreddyQuickBite;
USE FreddyQuickBite;


-- ============================================================
-- 2. CREACION DE TABLAS 
-- ============================================================

-- ------------------------------------------------------------
-- TABLA: rol
-- ------------------------------------------------------------
CREATE TABLE rol (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

-- ------------------------------------------------------------
-- TABLA: usuario
-- ------------------------------------------------------------
CREATE TABLE usuario (
    id_usuario          INT AUTO_INCREMENT PRIMARY KEY,
    id_rol               INT NOT NULL,
    nombre                VARCHAR(50) NOT NULL,
    apellido              VARCHAR(50) NOT NULL,
    correo                VARCHAR(100) NOT NULL UNIQUE,
    telefono              VARCHAR(20),
    turno                 VARCHAR(50),
    password              VARCHAR(255) NOT NULL,
    fecha_nacimiento     DATE,
    estado                BOOLEAN DEFAULT TRUE,
    fecha_registro       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- ------------------------------------------------------------
-- TABLA: categoria
-- ------------------------------------------------------------
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(50) NOT NULL UNIQUE,
    descripcion   VARCHAR(200),
    icono         VARCHAR(100),
    imagen        VARCHAR(255),
    estado        BOOLEAN DEFAULT TRUE
);

-- ------------------------------------------------------------
-- TABLA: promocion
-- ------------------------------------------------------------
CREATE TABLE promocion (
    id_promocion INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(80) NOT NULL,
    descripcion   VARCHAR(255),
    descuento     DECIMAL(5,2),
    fecha_inicio DATE,
    fecha_fin    DATE,
    estado        BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_promocion_descuento CHECK (descuento IS NULL OR (descuento >= 0 AND descuento <= 100)),
    CONSTRAINT chk_promocion_fechas CHECK (fecha_inicio IS NULL OR fecha_fin IS NULL OR fecha_inicio <= fecha_fin)
);

-- ------------------------------------------------------------
-- TABLA: producto
-- ------------------------------------------------------------
CREATE TABLE producto (
    id_producto  INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    id_promocion INT,
    nombre        VARCHAR(100) NOT NULL,
    descripcion   TEXT,
    precio        DECIMAL(10,2) NOT NULL,
    stock         INT NOT NULL DEFAULT 0,
    disponible    BOOLEAN DEFAULT TRUE,
    imagen        VARCHAR(255),
    estado        BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    FOREIGN KEY (id_promocion) REFERENCES promocion(id_promocion),
    CONSTRAINT chk_producto_precio CHECK (precio > 0),
    CONSTRAINT chk_producto_stock CHECK (stock >= 0)
);

-- ------------------------------------------------------------
-- TABLA: producto_categoria (N:M)
-- Permite que un producto aparezca en varias categorías a la
-- vez (ej. "Combo Buenos días" en Desayunos y en Combos), sin
-- perder su categoría principal en producto.id_categoria.
-- Integrada aquí desde el inicio (antes era una migración
-- aparte, ejecutada al final del archivo).
-- ------------------------------------------------------------
CREATE TABLE producto_categoria (
    id_producto  INT NOT NULL,
    id_categoria INT NOT NULL,
    PRIMARY KEY (id_producto, id_categoria),
    FOREIGN KEY (id_producto)  REFERENCES producto(id_producto)   ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- TABLA: carrito
-- ------------------------------------------------------------
CREATE TABLE carrito (
    id_carrito      INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado          ENUM('Activo','Finalizado','Cancelado') DEFAULT 'Activo',
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ------------------------------------------------------------
-- TABLA: carrito_detalle
-- ------------------------------------------------------------
CREATE TABLE carrito_detalle (
    id_carrito_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito          INT NOT NULL,
    id_producto         INT NOT NULL,
    cantidad             INT NOT NULL,
    observaciones        VARCHAR(255),
    FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    CONSTRAINT chk_carrito_detalle_cantidad CHECK (cantidad > 0)
);

-- ------------------------------------------------------------
-- TABLA: pedido
-- id_carrito ahora es UNIQUE: un carrito da origen, como máximo,
-- a un solo pedido.
--
-- PENDIENTE DE DECISIÓN EN EQUIPO: id_usuario sigue siendo un
-- solo campo. No queda explícito en el modelo si aquí se guarda
-- el Cliente que hizo el pedido o el Trabajador que lo cobró.
-- Si se necesitan ambos datos, la mejora sería separar este
-- campo en id_cliente (NOT NULL) e id_trabajador (NULL hasta
-- que se registre el pago) — no se aplicó aquí porque cambia
-- lo que ya espera el código Java (PedidoDAOImpl, etc.).
-- ------------------------------------------------------------
CREATE TABLE pedido (
    id_pedido     INT AUTO_INCREMENT PRIMARY KEY,
    numero_orden VARCHAR(20) UNIQUE,
    id_usuario    INT NOT NULL,
    id_carrito    INT NULL UNIQUE,
    fecha          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_entrega ENUM('Comer en restaurante','Para llevar','Domicilio'),
    estado         ENUM('Pendiente','Preparacion','Listo','Entregado','Cancelado') DEFAULT 'Pendiente',
    subtotal       DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento      DECIMAL(10,2) NOT NULL DEFAULT 0,
    total          DECIMAL(10,2) NOT NULL DEFAULT 0,
    costo_envio        DECIMAL(10,2) NOT NULL DEFAULT 0,
    direccion_entrega  VARCHAR(200) NULL,
    referencia_entrega VARCHAR(200) NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito),
    CONSTRAINT chk_pedido_montos CHECK (subtotal >= 0 AND descuento >= 0 AND total >= 0)
);
   ALTER TABLE pedido ADD COLUMN nombre_cliente VARCHAR(100) NULL;

-- ------------------------------------------------------------
-- TABLA: detalle_pedido
-- ------------------------------------------------------------
CREATE TABLE detalle_pedido (
    id_detalle   INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido    INT NOT NULL,
    id_producto  INT NOT NULL,
    id_promocion INT NULL,
    cantidad      INT NOT NULL,
    precio        DECIMAL(10,2) NOT NULL,
    subtotal      DECIMAL(10,2) DEFAULT 0,
    observaciones VARCHAR(255) NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_promocion) REFERENCES promocion(id_promocion),
    CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_detalle_precio CHECK (precio > 0),
    CONSTRAINT chk_detalle_subtotal CHECK (subtotal >= 0)
);

-- ------------------------------------------------------------
-- TABLA: pago
-- ------------------------------------------------------------
CREATE TABLE pago (
    id_pago      INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido    INT NOT NULL UNIQUE,
    metodo_pago ENUM('Efectivo','Tarjeta','Transferencia'),
    monto         DECIMAL(10,2),
    fecha_pago   TIMESTAMP NULL,
    estado        ENUM('Pendiente','Pagado','Rechazado') DEFAULT 'Pendiente',
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT chk_pago_monto CHECK (monto IS NULL OR monto >= 0)
);

-- ------------------------------------------------------------
-- TABLA: factura
-- ------------------------------------------------------------
CREATE TABLE factura (
    id_factura      INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido       INT NOT NULL UNIQUE,
    numero_factura VARCHAR(30) UNIQUE NOT NULL,
    fecha            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nit              VARCHAR(20),
    nombre_cliente  VARCHAR(100),
    direccion        VARCHAR(200),
    subtotal         DECIMAL(10,2) NOT NULL,
    descuento        DECIMAL(10,2) DEFAULT 0,
    iva              DECIMAL(10,2) NOT NULL,
    total            DECIMAL(10,2) NOT NULL,
    costo_envio      DECIMAL(10,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT chk_factura_montos CHECK (subtotal >= 0 AND descuento >= 0 AND iva >= 0 AND total >= 0)
);


-- ============================================================
-- 3. ÍNDICES
-- ============================================================
CREATE INDEX idx_usuario_correo ON usuario(correo);
CREATE INDEX idx_producto_categoria ON producto(id_categoria);
CREATE INDEX idx_pedido_usuario ON pedido(id_usuario);
CREATE INDEX idx_producto_nombre ON producto(nombre);
CREATE INDEX idx_pedido_estado ON pedido(estado);
CREATE INDEX idx_producto_stock ON producto(stock);
CREATE INDEX idx_detalle_pedido_pedido ON detalle_pedido(id_pedido);


-- ============================================================
-- 4. PROCEDIMIENTOS, TRIGGERS Y VISTAS
-- ============================================================

-- ------------------------------------------------------------
-- PROCEDIMIENTO: sp_insertar_producto
-- ------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE sp_insertar_producto(
    IN p_id_categoria INT,
    IN p_nombre        VARCHAR(100),
    IN p_descripcion   TEXT,
    IN p_precio        DECIMAL(10,2),
    IN p_stock         INT,
    IN p_imagen        VARCHAR(255)
)
BEGIN
    INSERT INTO producto
        (id_categoria, id_promocion, nombre, descripcion, precio, stock, disponible, imagen, estado)
    VALUES
        (p_id_categoria, NULL, p_nombre, p_descripcion, p_precio, p_stock, TRUE, p_imagen, TRUE);
END //

DELIMITER ;

-- ------------------------------------------------------------
-- TRIGGER: trg_descontar_stock
-- Descuenta el stock automáticamente al registrar el detalle
-- de un pedido.
-- TRIGGER: trg_restaurar_stock
-- Devuelve el stock si un pedido pasa a estado 'Cancelado'
-- después de haber sido creado (evita que el inventario quede
-- descontado por pedidos que nunca se concretaron).
-- ------------------------------------------------------------
DELIMITER //

CREATE TRIGGER trg_descontar_stock
AFTER INSERT ON detalle_pedido
FOR EACH ROW
BEGIN
    UPDATE producto
    SET stock = stock - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END //

CREATE TRIGGER trg_restaurar_stock
AFTER UPDATE ON pedido
FOR EACH ROW
BEGIN
    IF NEW.estado = 'Cancelado' AND OLD.estado <> 'Cancelado' THEN
        UPDATE producto p
        JOIN detalle_pedido dp ON dp.id_producto = p.id_producto
        SET p.stock = p.stock + dp.cantidad
        WHERE dp.id_pedido = NEW.id_pedido;
    END IF;
END //

DELIMITER ;

-- ------------------------------------------------------------
-- VISTA: vw_ventas_dia
-- Ventas agrupadas por día, para el módulo de Reportes.
-- ------------------------------------------------------------
CREATE VIEW vw_ventas_dia AS
SELECT
    DATE(p.fecha)               AS dia,
    COUNT(DISTINCT p.id_pedido) AS total_pedidos,
    SUM(p.subtotal)             AS total_subtotal,
    SUM(p.descuento)            AS total_descuento,
    SUM(p.total)                AS total_ventas
FROM pedido p
JOIN pago pg ON pg.id_pedido = p.id_pedido
WHERE pg.estado = 'Pagado'
GROUP BY DATE(p.fecha);

-- ------------------------------------------------------------
-- PROCEDIMIENTO: sp_migrar_categorias_hamburguesas_pizzas
-- ------------------------------------------------------------
-- El Autoservicio (PanelHamburguesas, PanelPizzas, OpcionesCliente,
-- FranjaHoraria, PanelInicio) filtra productos por
-- categoria.nombre = 'Hamburguesas' / 'Pizzas'. Los datos
-- heredados de este proyecto seguían llamando a esas categorías
-- "Desayunos" y "Almuerzos y Cenas", así que esos paneles cargaban
-- vacíos.
--
-- Revisando el CONTENIDO real (no el nombre) de cada categoría:
--   - "Almuerzos y Cenas": las 4 hamburguesas reales (Freddy Burger
--     Deluxe, Bonnie BBQ Burger, Chica Chicken Burger, Foxy Triple
--     Burger) + la única pizza (Pizza Party Personal) + otros 4
--     productos (Wrap Fazbear, Combo Fazbear Supremo, Chicken
--     Tenders Basket, Plato Fazbear Clásico).
--   - "Desayunos": solo comida de desayuno (pancakes, omelette,
--     waffles...), ninguna hamburguesa. No se toca: hoy no está
--     conectada a ningún panel activo del Autoservicio.
--
-- Este procedimiento:
--   1) Renombra "Almuerzos y Cenas" -> "Hamburguesas".
--   2) Crea "Pizzas" y mueve solo "Pizza Party Personal".
--   3) Sincroniza producto_categoria (N:M) con el mismo cambio,
--      para que perteneceACategoria() no siga contando ese
--      producto también como "Hamburguesas".
--
-- IDEMPOTENTE: revisa el estado actual antes de tocar cada cosa,
-- así que se puede volver a llamar las veces que haga falta sin
-- duplicar categorías ni fallar. Pensado para poder ejecutarse
-- también contra una base de datos EXISTENTE (con pedidos/usuarios
-- reales) sin necesidad de recrearla desde cero con este script.
-- ------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE sp_migrar_categorias_hamburguesas_pizzas()
BEGIN
    -- 1) Renombrar "Almuerzos y Cenas" -> "Hamburguesas" (si el
    --    nombre viejo todavía existe; si ya se migró, no hace nada)
    UPDATE categoria
    SET nombre = 'Hamburguesas',
        descripcion = 'Hamburguesas y platillos principales'
    WHERE nombre = 'Almuerzos y Cenas';

    -- 2) Crear "Pizzas" solo si todavía no existe
    INSERT INTO categoria (nombre, descripcion)
    SELECT 'Pizzas', 'Pizzas individuales'
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Pizzas');

    -- 3) Mover "Pizza Party Personal" a Pizzas (categoría principal)
    UPDATE producto p
    JOIN categoria c ON c.nombre = 'Pizzas'
    SET p.id_categoria = c.id_categoria
    WHERE p.nombre = 'Pizza Party Personal'
      AND p.id_categoria <> c.id_categoria;

    -- 4) Reflejar el mismo cambio en producto_categoria (N:M):
    --    quitar el rastro de "Hamburguesas" y dejar solo "Pizzas"
    --    para ese producto.
    DELETE pc FROM producto_categoria pc
    JOIN producto p  ON p.id_producto  = pc.id_producto
    JOIN categoria c ON c.id_categoria = pc.id_categoria
    WHERE p.nombre = 'Pizza Party Personal'
      AND c.nombre = 'Hamburguesas';

    INSERT INTO producto_categoria (id_producto, id_categoria)
    SELECT p.id_producto, c.id_categoria
    FROM producto p, categoria c
    WHERE p.nombre = 'Pizza Party Personal' AND c.nombre = 'Pizzas'
    ON DUPLICATE KEY UPDATE id_categoria = VALUES(id_categoria);

    -- 5) Crear "Platos Fuertes" solo si todavía no existe.
    --    BUG QUE ESTO CORRIGE: "Wrap Fazbear" y "Chicken Tenders
    --    Basket" quedaron en "Hamburguesas" tras el paso 1 (son
    --    parte del contenido heredado de "Almuerzos y Cenas") sin
    --    ser hamburguesas de verdad. Se les da su propia categoría
    --    de platos fuertes para que FranjaHoraria pueda ofrecerlos
    --    en el horario de Cena junto con Pizzas, en vez de quedar
    --    mezclados con las hamburguesas del horario de Desayuno.
    INSERT INTO categoria (nombre, descripcion)
    SELECT 'Platos Fuertes', 'Platos principales para la cena'
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Platos Fuertes');

    -- 6) Mover "Wrap Fazbear" y "Chicken Tenders Basket" a Platos Fuertes
    UPDATE producto p
    JOIN categoria c ON c.nombre = 'Platos Fuertes'
    SET p.id_categoria = c.id_categoria
    WHERE p.nombre IN ('Wrap Fazbear', 'Chicken Tenders Basket')
      AND p.id_categoria <> c.id_categoria;

    -- 7) Reflejar el cambio en producto_categoria (N:M): igual que en
    --    el paso 4, quitar el rastro de "Hamburguesas" y dejar solo
    --    "Platos Fuertes" para esos dos productos.
    DELETE pc FROM producto_categoria pc
    JOIN producto p  ON p.id_producto  = pc.id_producto
    JOIN categoria c ON c.id_categoria = pc.id_categoria
    WHERE p.nombre IN ('Wrap Fazbear', 'Chicken Tenders Basket')
      AND c.nombre = 'Hamburguesas';

    INSERT INTO producto_categoria (id_producto, id_categoria)
    SELECT p.id_producto, c.id_categoria
    FROM producto p, categoria c
    WHERE p.nombre IN ('Wrap Fazbear', 'Chicken Tenders Basket')
      AND c.nombre = 'Platos Fuertes'
    ON DUPLICATE KEY UPDATE id_categoria = VALUES(id_categoria);
END //

DELIMITER ;


-- ============================================================
-- 5. DATOS INICIALES (SEED DATA)
-- ============================================================

-- ------------------------------------------------------------
-- rol
-- ------------------------------------------------------------
INSERT INTO rol (nombre, descripcion) VALUES
('Administrador', 'Control total'),
('Trabajador', 'Gestiona pedidos');

-- ------------------------------------------------------------
-- categoria
-- "Combos" ya viene incluida desde el inicio (antes se creaba
-- con una migración aparte, después de Promociones).
-- ------------------------------------------------------------
INSERT INTO categoria (nombre, descripcion) VALUES
('Desayunos', 'Menú de desayuno'),
('Almuerzos y Cenas', 'Comidas principales'),
('Postres', 'Postres'),
('McCafe', 'Café y bebidas calientes'),
('Bebidas', 'Bebidas frías'),
('Antojos', 'Snacks'),
('Cajita Feliz', 'Menú infantil'),
('Combos', 'Combos para toda la familia'),
('Promociones', 'Ofertas especiales');
-- id_categoria resultante: 1 Desayunos, 2 Almuerzos y Cenas,
-- 3 Postres, 4 McCafe, 5 Bebidas, 6 Antojos, 7 Cajita Feliz,
-- 8 Combos, 9 Promociones.

-- ------------------------------------------------------------
-- promocion
-- ------------------------------------------------------------
INSERT INTO promocion
(nombre, descripcion, descuento, fecha_inicio, fecha_fin, estado)
VALUES
('Combo Freddy 2x1', 'Todos los martes, compra un Combo Freddy Deluxe y recibe otro gratis.', NULL, '2026-01-01', '2026-12-31', TRUE),
('Hora Feliz', 'De 3:00 p.m. a 5:00 p.m., 25% de descuento en bebidas y postres.', 25.00, '2026-01-01', '2026-12-31', TRUE),
('Martes de Hamburguesas', 'Todas las hamburguesas con 20% de descuento.', 20.00, '2026-01-01', '2026-12-31', TRUE),
('Combo Familiar', 'Cuatro hamburguesas, cuatro papas y cuatro bebidas a precio especial.', NULL, '2026-01-01', '2026-12-31', TRUE),
('Desayuno Express', 'Café + Croissant + Jugo con precio reducido hasta las 10:00 a.m.', NULL, '2026-01-01', '2026-12-31', TRUE),
('Postre Gratis', 'En compras mayores a Q150 recibe un Sundae Fazbear gratis.', NULL, '2026-01-01', '2026-12-31', TRUE),
('Noche Fazbear', 'Después de las 7:00 p.m., segunda pizza personal al 50%.', 50.00, '2026-01-01', '2026-12-31', TRUE),
('Cumpleaños Fazbear', 'El cumpleañero recibe un pastel individual gratis al presentar su identificación.', NULL, '2026-01-01', '2026-12-31', TRUE);

-- ------------------------------------------------------------
-- producto (con imagen incluida en la misma llamada)
-- ------------------------------------------------------------
-- Desayunos (1)
CALL sp_insertar_producto(1, 'Desayuno Fazbear Clásico', 'Huevos revueltos, tocino crujiente, pan tostado y papas hash brown.', 48.00, 100, 'Desayuno Fazbear Clasico');
CALL sp_insertar_producto(1, 'Pancakes Freddy', 'Tres pancakes esponjosos con mantequilla y miel de maple.', 36.00, 100, 'Pancakes Freddy');
CALL sp_insertar_producto(1, 'Omelette Rockstar', 'Omelette relleno de jamón, queso cheddar y vegetales frescos.', 42.00, 100, 'Omelette Rockstar');
CALL sp_insertar_producto(1, 'Sándwich Morning Bite', 'Pan brioche con huevo, queso americano y salchicha artesanal.', 34.00, 100, 'Sandwich Morning Bite');
CALL sp_insertar_producto(1, 'Waffle golden bear', 'Waffle belga acompañado de frutas y crema batida.', 39.00, 100, 'Waffle Golden Bear');
CALL sp_insertar_producto(1, 'Burrito Despertador', 'Tortilla rellena de huevo, queso, salchicha y papas.', 41.00, 100, 'Burrito Despertador');
CALL sp_insertar_producto(1, 'Croissant Supremo', 'Croissant relleno de jamón ahumado y queso mozzarella.', 32.00, 100, 'Croissant Supremo');
CALL sp_insertar_producto(1, 'Combo Buenos días', 'Café, jugo de naranja y muffin de vainilla', 38.00, 100, 'Combo Buenos Dias');
CALL sp_insertar_producto(1, 'Burrito de Desayuno Grande', 'Tortilla rellena de huevo, queso, salchicha y papas, tamaño grande.', 42.00, 100, 'Burrito de Desayuno Grande');
CALL sp_insertar_producto(1, 'Pancakes Clásico', 'Tres pancakes esponjosos con mantequilla y miel.', 36.00, 100, 'Pancakes Clasico');
CALL sp_insertar_producto(1, 'Pancakes con Miel de Maple', 'Pancakes bañados en miel de maple auténtica.', 38.00, 100, 'Pancakes con Miel de Maple');

-- Hamburguesas y Pizzas (Almuerzos y Cenas) (2)
CALL sp_insertar_producto(2, 'Freddy Burger Deluxe', 'Carne 100% res, doble queso cheddar, lechuga, tomate y salsa especial Quick Bite.', 58.00, 100, 'Freddy Burger Deluxe');
CALL sp_insertar_producto(2, 'Bonnie BBQ Burger', 'Hamburguesa con salsa BBQ, cebolla caramelizada y queso suizo.', 62.00, 100, 'Bonnie BBQ Burger');
CALL sp_insertar_producto(2, 'Chica Chicken Burger', 'Pechuga de pollo empanizada, queso y salsa miel-mostaza.', 54.00, 100, 'Chica Chicken Burger');
CALL sp_insertar_producto(2, 'Foxy Triple Burger', 'Triple carne, doble queso, tocino y pepinillos.', 72.00, 100, 'Foxy Triple Burger');
CALL sp_insertar_producto(2, 'Pizza Party Personal', 'Pizza individual de pepperoni con queso mozzarella.', 48.00, 100, 'Pizza Party Personal');
CALL sp_insertar_producto(2, 'Wrap Fazbear', 'Tortilla de harina con pollo, vegetales y aderezo ranch.', 44.00, 100, 'Wrap Fazbear');
CALL sp_insertar_producto(2, 'Combo Fazbear Supremo', 'Hamburguesa Deluxe, papas grandes y bebida mediana', 79.00, 100, 'Combo Fazbear Supremo');
CALL sp_insertar_producto(2, 'Chicken Tenders Basket', 'Seis tiras de pollo con papas fritas y salsa BBQ.', 59.00, 100, 'Chicken Tenders Basket');
CALL sp_insertar_producto(2, 'Plato Fazbear Clásico', 'Plato principal insignia de la casa.', 55.00, 100, 'Plato Fazbear Clasico');

-- Postres (3)
CALL sp_insertar_producto(3, 'Brownie Freddy', 'Brownie de chocolate con helado de vainilla.', 28.00, 100, 'Brownie Freddy');
CALL sp_insertar_producto(3, 'Sundae Fazbear', 'Helado de vainilla con chocolate, nueces y cereza.', 24.00, 100, 'Sundae Fazbear');
CALL sp_insertar_producto(3, 'Pastel Golden', 'Rebanada de pastel de vainilla con crema.', 27.00, 100, 'Pastel Golden');
CALL sp_insertar_producto(3, 'Cheesecake Puppet', 'Cheesecake con salsa de frutos rojos.', 30.00, 100, 'Cheesecake Puppet');
CALL sp_insertar_producto(3, 'Galletas Animatronic', 'Cuatro galletas con chispas de chocolate.', 22.00, 100, 'Galletas Animatronic');
CALL sp_insertar_producto(3, 'Mini donuts', 'Seis mini donuts espolvoreadas con azúcar y canela.', 25.00, 100, 'Mini Donuts');
CALL sp_insertar_producto(3, 'Banana Split Freddy', 'Helado, frutas, crema batida y chocolate.', 36.00, 100, 'Banana Split Freddy');
CALL sp_insertar_producto(3, 'Volcán de chocolate', 'Pastel tibio con centro líquido de chocolate.', 34.00, 100, 'Volcan de Chocolate');
CALL sp_insertar_producto(3, 'Bol de Acaí del Pirata', 'Bowl de acaí con fruta fresca y granola, estilo pirata.', 34.00, 100, 'Bol de Acai del Pirata');
CALL sp_insertar_producto(3, 'Sundae de Helado', 'Copa de helado con toppings variados.', 26.00, 100, 'Sundae de Helado');
CALL sp_insertar_producto(3, 'Root Beer Float', 'Root beer con una bola de helado de vainilla.', 28.00, 100, 'Root Beer Float');
CALL sp_insertar_producto(3, 'Waffles de Chocolate', 'Waffles bañados en chocolate.', 34.00, 100, 'Waffles de Chocolate');

-- McCafe (4)
CALL sp_insertar_producto(4, 'Espresso Fazbear', 'Café espresso de grano seleccionado.', 18.00, 100, 'Espresso Fazbear');
CALL sp_insertar_producto(4, 'Cappuccino Freddy', 'Espresso con leche vaporizada y espuma cremosa.', 26.00, 100, 'Cappuccino Freddy');
CALL sp_insertar_producto(4, 'Latte Vainilla', 'Café latte con un toque de vainilla.', 28.00, 100, 'Latte Vanilla');
CALL sp_insertar_producto(4, 'Mocha Chica', 'Café con chocolate y crema batida.', 30.00, 100, 'Mocha Chica');
CALL sp_insertar_producto(4, 'Chocolate Caliente', 'Chocolate caliente con malvaviscos.', 25.00, 100, 'Chocolate Caliente');
CALL sp_insertar_producto(4, 'Frappé Cookies', 'Frappé de vainilla con galleta triturada.', 34.00, 100, 'Frappe Cookies');
CALL sp_insertar_producto(4, 'Té Helado Limón', 'Té negro con limón natural.', 20.00, 100, 'Te Helado Limon');
CALL sp_insertar_producto(4, 'Muffin Arándanos', 'Muffin recién horneado de arándanos.', 24.00, 100, 'Muffin Arandanos');
CALL sp_insertar_producto(4, 'Expresso Machiato', 'Espresso con un toque de espuma de leche.', 22.00, 100, 'Expresso Machiato');
CALL sp_insertar_producto(4, 'Latte Clásico', 'Espresso con leche vaporizada.', 26.00, 100, 'Latte Clasico');
CALL sp_insertar_producto(4, 'Mocha Chocolate Iced', 'Café frío con chocolate.', 30.00, 100, 'Mocha Chocolate Iced');
CALL sp_insertar_producto(4, 'Mocha Chocolate Iced (Frío)', 'Versión bien fría del mocha de chocolate.', 30.00, 100, 'Mocha Chocolate Iced Frio');
CALL sp_insertar_producto(4, 'Frappé de Caramelo (Frío)', 'Frappé de caramelo bien frío.', 32.00, 100, 'Frappe de Caramelo Frio');
CALL sp_insertar_producto(4, 'Frappé de Caramelo con Helado', 'Frappé de caramelo con una bola de helado encima.', 36.00, 100, 'Frappe de Caramelo con Helado');

-- Bebidas (5)
CALL sp_insertar_producto(5, 'Refresco Mediano', 'Bebida gaseosa de 16 oz.', 15.00, 100, 'Refresco Mediano');
CALL sp_insertar_producto(5, 'Refresco Grande', 'Bebida gaseosa de 22 oz.', 18.00, 100, 'Refresco Grande');
CALL sp_insertar_producto(5, 'Limonada natural', 'Limonada preparada con limón fresco.', 18.00, 100, 'Limonada Natural');
CALL sp_insertar_producto(5, 'Jugo de naranja', 'Jugo natural recién exprimido', 20.00, 100, 'Jugo de Naranja');
CALL sp_insertar_producto(5, 'Malteada Chocolate', 'Malteada cremosa de chocolate.', 32.00, 100, 'Malteada Chocolate');
CALL sp_insertar_producto(5, 'Malteada Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, 'Malteada Fresa');
CALL sp_insertar_producto(5, 'Agua Embotellada', 'Agua purificada de 600 ml.', 10.00, 100, 'Agua Embotellada');
CALL sp_insertar_producto(5, 'Smoothie Tropical', 'Mango, piña y naranja licuados con hielo.', 34.00, 100, 'Smoothie Tropical');
CALL sp_insertar_producto(5, 'Bebida de Fresa', 'Bebida refrescante sabor fresa.', 20.00, 100, 'Bebida de Fresa');
CALL sp_insertar_producto(5, 'Botín de Pirata de Foxy', 'Bebida servida en vaso temático estilo bota pirata.', 25.00, 100, 'Botin de Pirata de Foxy');
CALL sp_insertar_producto(5, 'Ponche de Frutas', 'Mezcla de frutas tropicales rojas y naranjas en capas, con un toque cítrico y banderas pirata.', 22.00, 100, 'Ponche de Frutas');
CALL sp_insertar_producto(5, 'Granizado de Arándano', 'Granizado frío sabor arándano.', 24.00, 100, 'Granizado de Arandano');
CALL sp_insertar_producto(5, 'Malteada de Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, 'Malteada Fresa');
CALL sp_insertar_producto(5, 'Slushie de Lima', 'Bebida helada sabor lima.', 22.00, 100, 'Slushie de Lima');
CALL sp_insertar_producto(5, 'Smoothie de Durazno', 'Smoothie natural de durazno.', 30.00, 100, 'Smoothie de Durazno');
CALL sp_insertar_producto(5, 'Té Helado', 'Té negro servido helado.', 18.00, 100, 'Te Helado');

-- Antojos (6)
CALL sp_insertar_producto(6, 'Papas Clásicas', 'Papas fritas doradas y crujientes.', 18.00, 100, 'Papas Clasicas');
CALL sp_insertar_producto(6, 'Papas con Queso', 'Papas bañadas en queso cheddar.', 28.00, 100, 'Papas con Queso');
CALL sp_insertar_producto(6, 'Aros de Cebolla', 'Aros empanizados y crujientes.', 26.00, 100, 'Aros de cebolla');
CALL sp_insertar_producto(6, 'Nuggets (6 piezas)', 'Nuggets de pollo con salsa BBQ.', 32.00, 100, 'Nuggets (6 piezas)');
CALL sp_insertar_producto(6, 'Mozzarella Sticks', 'Palitos de queso mozzarella empanizados.', 34.00, 100, 'Mozzarella Sticks');
CALL sp_insertar_producto(6, 'Alitas BBQ', 'Seis alitas bañadas en salsa BBQ.', 42.00, 100, 'Alitas BBQ');
CALL sp_insertar_producto(6, 'Nachos Supreme', 'Nachos con queso, carne y jalapeños.', 39.00, 100, 'Nachos Supreme');
CALL sp_insertar_producto(6, 'Papas Fazbear', 'Papas con tocino, queso cheddar y cebollín.', 38.00, 100, 'Papas Fazbear');
CALL sp_insertar_producto(6, 'Alitas de Foxy', 'Alitas bañadas en salsa, tema Foxy.', 40.00, 100, 'Alitas de Foxy');
CALL sp_insertar_producto(6, 'Bocados de Maíz', 'Bocados crujientes de maíz.', 22.00, 100, 'Bocados de Maiz');
CALL sp_insertar_producto(6, 'Sartén de Queso', 'Queso fundido servido en sartén individual.', 30.00, 100, 'Sarten de Queso');

-- Cajita Feliz (7)
CALL sp_insertar_producto(7, 'Cajita Freddy Burger', 'Mini hamburguesa, papas pequeñas, jugo y juguete coleccionable.', 46.00, 100, 'Cajita Freddy Burger');
CALL sp_insertar_producto(7, 'Cajita Nuggets', 'Cuatro nuggets, papas, bebida y juguete sorpresa.', 45.00, 100, 'Cajita Nuggets');
CALL sp_insertar_producto(7, 'Cajita Mini Pizza', 'Mini pizza, jugo y juguete.', 48.00, 100, 'Cajita Mini Pizza');
CALL sp_insertar_producto(7, 'Copa de Pastel de Chica', 'Un pastel helado con capas de pastel de vainilla, bebida pequeña y juguete de Chica.', 35.00, 100, 'Copa de Pastel de Chica');
CALL sp_insertar_producto(7, 'Festín de Tacos de Bonnie', 'Tres tacos de carne asada estilo Fazbear, bebida y juguete de colección.', 52.00, 100, 'Festin de Tacos de Bonnie');
CALL sp_insertar_producto(7, 'Paquete de Papas Shadow', 'Papas fritas rizadas con salsa Fazbear, café y juguete de Shadow Freddy.', 38.00, 100, 'Paquete de Papas Shadow');
CALL sp_insertar_producto(7, 'Cajita Fazbear Deluxe', 'Hamburguesa infantil, postre pequeño y juguete exclusivo.', 52.00, 100, 'Cajita Fazbear Deluxe');
CALL sp_insertar_producto(7, 'Paquete de Pizza de Chica', 'Mini pizza, bebida y juguete de Chica.', 46.00, 100, 'Paquete de Pizza de Chica');
CALL sp_insertar_producto(7, 'Cajita Chicken Wrap', 'Mini wrap de pollo, papas pequeñas, jugo y juguete sorpresa.', 45.00, 100, 'Cajita Chicken Wrap');
CALL sp_insertar_producto(7, 'Cajita Hot Dog', 'Hot dog clásico, papas pequeñas, jugo y juguete sorpresa.', 42.00, 100, 'Cajita Hot Dog');
CALL sp_insertar_producto(7, 'Cajita Pancake Kids', 'Mini pancakes con miel, jugo y juguete sorpresa.', 40.00, 100, 'Cajita Pancake Kids');
CALL sp_insertar_producto(7, 'Cajita Quesadilla', 'Mini quesadilla de queso, papas pequeñas, jugo y juguete sorpresa.', 43.00, 100, 'Cajita Quesadilla');

-- Combos (8)
CALL sp_insertar_producto(8, 'Combo Golden Pizza-Burger', 'Un combo dorado: burger premium con sabor a pizza, bebida grande y juguete.', 55.00, 100, 'Combo Golden Pizza-Burger');
CALL sp_insertar_producto(8, 'Combo Bonnie-Nuggets', 'Nuggets, papas, bebida y juguete temático de Bonnie.', 48.00, 100, 'Combo Bonnie-Nuggets');
CALL sp_insertar_producto(8, 'Combo Freddy Fazbear', 'Combo insignia con juguete de colección de Freddy.', 55.00, 100, 'Combo Freddy Fazbear');

-- ------------------------------------------------------------
-- producto_categoria
-- Cada producto conserva, como mínimo, su categoría principal
-- también dentro de la tabla intermedia N:M, para que las
-- consultas que solo miran producto_categoria no pierdan
-- ningún producto.
--
-- Ejemplo de cómo agregar una categoría ADICIONAL a un producto
-- que ya tiene su categoría principal (déjalo comentado y
-- ajústalo si lo necesitan):
--   INSERT INTO producto_categoria (id_producto, id_categoria)
--   SELECT p.id_producto, c.id_categoria
--   FROM producto p, categoria c
--   WHERE p.nombre = 'Combo Buenos días' AND c.nombre = 'Combos'
--   ON DUPLICATE KEY UPDATE id_producto = id_producto;
-- ------------------------------------------------------------
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT id_producto, id_categoria FROM producto;

-- ------------------------------------------------------------
-- Aplicar la migración de categorías (ver sp_migrar_categorias_
-- hamburguesas_pizzas en la sección 4) para que, al ejecutar este
-- script de cero, la base de datos quede directamente con
-- "Hamburguesas" y "Pizzas" en vez de los nombres heredados
-- "Desayunos"/"Almuerzos y Cenas". El procedimiento se deja creado
-- en el esquema (no se elimina) para poder volver a llamarlo contra
-- una base de datos existente sin recrearla.
-- ------------------------------------------------------------
CALL sp_migrar_categorias_hamburguesas_pizzas();

-- ------------------------------------------------------------
-- CORRECCIÓN DE DATOS: carritos que quedaron "Activo" para
-- siempre después de pagar (ver bug corregido en
-- PedidoController.confirmarPedido(): antes no marcaba el
-- carrito como Finalizado, así que obtenerOCrearCarritoActivo()
-- seguía devolviendo ese mismo carrito, y como ya tenía un
-- pedido asociado (id_carrito es UNIQUE en pedido), el cliente
-- quedaba bloqueado con "Ya se registró un pedido para este
-- carrito" en TODAS sus compras siguientes. Se corrige el
-- histórico: cualquier carrito Activo que ya tenga un pedido
-- registrado se marca Finalizado. Es seguro volver a correr
-- esto las veces que haga falta.
--
-- BUG QUE ESTO CORRIGE (Error 1175, "safe update mode"): MySQL
-- Workbench rechaza cualquier UPDATE/DELETE cuyo WHERE no
-- incluya una columna con índice (clave). El WHERE original
-- solo usaba c.estado (sin índice), así que Workbench lo
-- bloqueaba aunque la sentencia era correcta. Se agrega
-- "c.id_carrito > 0" (la clave primaria, siempre cierto) para
-- cumplir la regla sin cambiar el resultado.
-- ------------------------------------------------------------
UPDATE carrito c
JOIN pedido p ON p.id_carrito = c.id_carrito
SET c.estado = 'Finalizado'
WHERE c.id_carrito > 0
  AND c.estado = 'Activo';


-- ============================================================
-- 6. CONSULTAS DE VERIFICACIÓN (comentadas, opcionales)
-- Déjalas comentadas en la entrega final; descoméntalas solo
-- si necesitas comprobar algo mientras desarrollas.
-- ============================================================

-- Productos con más de una categoría asociada:
-- SELECT
--     p.nombre AS producto,
--     GROUP_CONCAT(c.nombre ORDER BY c.nombre SEPARATOR ', ') AS categorias
-- FROM producto_categoria pc
-- JOIN producto p  ON p.id_producto  = pc.id_producto
-- JOIN categoria c ON c.id_categoria = pc.id_categoria
-- GROUP BY p.id_producto
-- HAVING COUNT(*) > 1;

-- Cuántos productos "vendibles" hay por categoría (estado=1,
-- disponible=1, stock>0). listarProductosDisponibles() en
-- ProductoServiceImpl exige esas tres condiciones; si alguna
-- categoría sale en 0, ese panel se verá vacío en la app aunque
-- el filtro Java esté correcto:
-- SELECT
--     c.nombre AS categoria,
--     COUNT(*) AS productos_visibles_en_panel
-- FROM producto p
-- JOIN categoria c ON c.id_categoria = p.id_categoria
-- WHERE p.estado = 1
--   AND p.disponible = 1
--   AND p.stock > 0
-- GROUP BY c.nombre
-- ORDER BY c.nombre;

-- ===============================================================
-- FREDDY-FAZBEAR'S QUICK BITE
-- ---------------------------------------------------------------
-- Crea las cuentas de Administrador de los 4 contribuidores del
-- repositorio (identificados por su historial de commits en git):
--
--   Axel Ramirez     - axelramirez@emilianisomascos.edu.gt
--   Melany Mejía     - melanymejia@emilianisomascos.edu.gt
--   Diego Quisqué    - diegoquisque@emilianisomascos.edu.gt
--   Cristhian Pocon  - cristhianpocon@emilianisomascos.edu.gt
--
-- Contraseña para las 4 cuentas: admin8181920
--
-- La contraseña NO se guarda en texto plano: cada hash de abajo se
-- generó con el mismo algoritmo que usa la app (BCrypt.hashpw con
-- salt aleatorio, ver Utils/Encriptador.java) y ya se verificó con
-- BCrypt.checkpw() que "admin8181920" abre cada uno de los 4
-- hashes. Cada hash es distinto entre sí (salt aleatorio distinto)
-- aunque la contraseña real sea la misma para las 4 cuentas.
--
-- Requiere que ya se haya corrido FreddyQuickBite.sql (usa el
-- id_rol de 'Administrador' por nombre, no un número fijo, para
-- no depender del orden en que se insertó la tabla rol).
--
-- Usa INSERT IGNORE: si vuelves a correr este script no falla por
-- el UNIQUE de correo, simplemente no duplica las cuentas que ya
-- existan.
-- ===============================================================

USE FreddyQuickBite;

INSERT IGNORE INTO usuario
    (id_rol, nombre, apellido, correo, telefono, password, estado)
VALUES
    (
        (SELECT id_rol FROM rol WHERE nombre = 'Administrador'),
        'Axel', 'Ramirez', 'axelramirez@emilianisomascos.edu.gt', NULL,
        '$2a$10$EhVVoNTKyHAOGM2.ksv9meisk26REoVO2BaX0XZNO2Ue9Q1LGZC4u',
        TRUE
    ),
    (
        (SELECT id_rol FROM rol WHERE nombre = 'Administrador'),
        'Melany', 'Mejía', 'melanymejia@emilianisomascos.edu.gt', NULL,
        '$2a$10$Sa7iNXPxRaaCAMFIo2JZGuMc2JOWbikuxRSP.7dH3Q5zks1UTHPP2',
        TRUE
    ),
    (
        (SELECT id_rol FROM rol WHERE nombre = 'Administrador'),
        'Diego', 'Quisqué', 'diegoquisque@emilianisomascos.edu.gt', NULL,
        '$2a$10$h2Xg0TSROZJ9rgMs8dDPhepehLIOaniTmU74OQqTFS.kzWhxr8uSy',
        TRUE
    ),
    (
        (SELECT id_rol FROM rol WHERE nombre = 'Administrador'),
        'Cristhian', 'Pocon', 'cristhianpocon@emilianisomascos.edu.gt', NULL,
        '$2a$10$McDF0IbbjhCblMB9UQZXW.96ezbv0B0y1j0Z24jn22bJv4hnUyZd6',
        TRUE
    );
    
INSERT INTO usuario (id_rol, nombre, apellido, correo, telefono, turno, password, fecha_nacimiento, estado)
VALUES (
  (SELECT id_rol FROM rol WHERE nombre = 'Trabajador'),
  'Prueba', 'Trabajador',
  'trabajador.prueba@freddyquickbite.com',
  '00000000',
  'Mañana',
  '$2a$12$..ycxI7W0J6T1Ym3ogkcKuZ4vD/NeoCGtbwGH.mNGc2tKEOZ.XYtG',
  '2000-01-01',
  TRUE
);

-- Verificación rápida: debe mostrar las 4 cuentas con rol Administrador.
SELECT u.id_usuario, u.nombre, u.apellido, u.correo, r.nombre AS rol
FROM usuario u
JOIN rol r ON r.id_rol = u.id_rol
WHERE r.nombre = 'Administrador';

-- Verificacion de la cuenta de trabajador
SELECT u.id_usuario, u.correo, r.nombre AS rol
FROM usuario u JOIN rol r ON r.id_rol = u.id_rol
WHERE u.correo = 'trabajador.prueba@freddyquickbite.com';