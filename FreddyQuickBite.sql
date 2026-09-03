-- ============================================================
-- BASE DE DATOS: FreddyQuickBite
-- Proyecto: Freddy Fazbear's Quick Bite - Pantalla de autoservicio
-- Versión: ORGANIZADA (Fase 2)
-- ============================================================
-- Este script ya integra las migraciones anteriores directamente
-- en la definición de las tablas y los datos iniciales, en vez de
-- dejarlas como pasos separados. Al ejecutarlo de cero se obtiene
-- la base de datos ya en su versión final y correcta.
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
-- producto
-- Todos los productos se insertan directamente con su
-- categoría FINAL ya correcta (antes, tres de estos productos
-- ["Combo Golden Pizza-Burger", "Combo Bonnie-Nuggets" y
-- "Combo Freddy Fazbear"] se creaban en Cajita Feliz [7] y una
-- migración posterior los movía a Combos [8]. Aquí ya nacen
-- directamente en la categoría 8).
--
-- Los productos que antes se insertaban con
-- CALL sp_insertar_producto(...) quedaron integrados aquí mismo
-- como filas normales de INSERT, junto a los demás, para no
-- mezclar dos formas distintas de insertar productos.
-- ------------------------------------------------------------
INSERT INTO producto
(id_categoria, id_promocion, nombre, descripcion, precio, stock, disponible, estado)
VALUES
-- Desayunos (1)
(1, NULL, 'Desayuno Fazbear Clásico', 'Huevos revueltos, tocino crujiente, pan tostado y papas hash brown.', 48.00, 100, TRUE, TRUE),
(1, NULL, 'Pancakes Freddy', 'Tres pancakes esponjosos con mantequilla y miel de maple.', 36.00, 100, TRUE, TRUE),
(1, NULL, 'Omelette Rockstar', 'Omelette relleno de jamón, queso cheddar y vegetales frescos.', 42.00, 100, TRUE, TRUE),
(1, NULL, 'Sándwich Morning Bite', 'Pan brioche con huevo, queso americano y salchicha artesanal.', 34.00, 100, TRUE, TRUE),
(1, NULL, 'Waffle golden bear', 'Waffle belga acompañado de frutas y crema batida.', 39.00, 100, TRUE, TRUE),
(1, NULL, 'Burrito Despertador', 'Tortilla rellena de huevo, queso, salchicha y papas.', 41.00, 100, TRUE, TRUE),
(1, NULL, 'Croissant Supremo', 'Croissant relleno de jamón ahumado y queso mozzarella.', 32.00, 100, TRUE, TRUE),
(1, NULL, 'Combo Buenos días', 'Café, jugo de naranja y muffin de vainilla', 38.00, 100, TRUE, TRUE),
(1, NULL, 'Burrito de Desayuno Grande', 'Tortilla rellena de huevo, queso, salchicha y papas, tamaño grande.', 42.00, 100, TRUE, TRUE),
(1, NULL, 'Pancakes Clásico', 'Tres pancakes esponjosos con mantequilla y miel.', 36.00, 100, TRUE, TRUE),
(1, NULL, 'Pancakes con Miel de Maple', 'Pancakes bañados en miel de maple auténtica.', 38.00, 100, TRUE, TRUE),
-- Almuerzos y Cenas (2)
(2, NULL, 'Freddy Burger Deluxe', 'Carne 100% res, doble queso cheddar, lechuga, tomate y salsa especial Quick Bite.', 58.00, 100, TRUE, TRUE),
(2, NULL, 'Bonnie BBQ Burger', 'Hamburguesa con salsa BBQ, cebolla caramelizada y queso suizo.', 62.00, 100, TRUE, TRUE),
(2, NULL, 'Chica Chicken Burger', 'Pechuga de pollo empanizada, queso y salsa miel-mostaza.', 54.00, 100, TRUE, TRUE),
(2, NULL, 'Foxy Triple Burger', 'Triple carne, doble queso, tocino y pepinillos.', 72.00, 100, TRUE, TRUE),
(2, NULL, 'Pizza Party Personal', 'Pizza individual de pepperoni con queso mozzarella.', 48.00, 100, TRUE, TRUE),
(2, NULL, 'Wrap Fazbear', 'Tortilla de harina con pollo, vegetales y aderezo ranch.', 44.00, 100, TRUE, TRUE),
(2, NULL, 'Combo Fazbear Supremo', 'Hamburguesa Deluxe, papas grandes y bebida mediana', 79.00, 100, TRUE, TRUE),
(2, NULL, 'Chicken Tenders Basket', 'Seis tiras de pollo con papas fritas y salsa BBQ.', 59.00, 100, TRUE, TRUE),
(2, NULL, 'Plato Fazbear Clásico', 'Plato principal insignia de la casa.', 55.00, 100, TRUE, TRUE),
-- Postres (3)
(3, NULL, 'Brownie Freddy', 'Brownie de chocolate con helado de vainilla.', 28.00, 100, TRUE, TRUE),
(3, NULL, 'Sundae Fazbear', 'Helado de vainilla con chocolate, nueces y cereza.', 24.00, 100, TRUE, TRUE),
(3, NULL, 'Pastel Golden', 'Rebanada de pastel de vainilla con crema.', 27.00, 100, TRUE, TRUE),
(3, NULL, 'Cheesecake Puppet', 'Cheesecake con salsa de frutos rojos.', 30.00, 100, TRUE, TRUE),
(3, NULL, 'Galletas Animatronic', 'Cuatro galletas con chispas de chocolate.', 22.00, 100, TRUE, TRUE),
(3, NULL, 'Mini donuts', 'Seis mini donuts espolvoreadas con azúcar y canela.', 25.00, 100, TRUE, TRUE),
(3, NULL, 'Banana Split Freddy', 'Helado, frutas, crema batida y chocolate.', 36.00, 100, TRUE, TRUE),
(3, NULL, 'Volcán de chocolate', 'Pastel tibio con centro líquido de chocolate.', 34.00, 100, TRUE, TRUE),
(3, NULL, 'Bol de Acaí del Pirata', 'Bowl de acaí con fruta fresca y granola, estilo pirata.', 34.00, 100, TRUE, TRUE),
(3, NULL, 'Sundae de Helado', 'Copa de helado con toppings variados.', 26.00, 100, TRUE, TRUE),
(3, NULL, 'Root Beer Float', 'Root beer con una bola de helado de vainilla.', 28.00, 100, TRUE, TRUE),
(3, NULL, 'Waffles de Chocolate', 'Waffles bañados en chocolate.', 34.00, 100, TRUE, TRUE),
-- McCafé (4)
(4, NULL, 'Espresso Fazbear', 'Café espresso de grano seleccionado.', 18.00, 100, TRUE, TRUE),
(4, NULL, 'Cappuccino Freddy', 'Espresso con leche vaporizada y espuma cremosa.', 26.00, 100, TRUE, TRUE),
(4, NULL, 'Latte Vainilla', 'Café latte con un toque de vainilla.', 28.00, 100, TRUE, TRUE),
(4, NULL, 'Mocha Chica', 'Café con chocolate y crema batida.', 30.00, 100, TRUE, TRUE),
(4, NULL, 'Chocolate Caliente', 'Chocolate caliente con malvaviscos.', 25.00, 100, TRUE, TRUE),
(4, NULL, 'Frappé Cookies', 'Frappé de vainilla con galleta triturada.', 34.00, 100, TRUE, TRUE),
(4, NULL, 'Té Helado Limón', 'Té negro con limón natural.', 20.00, 100, TRUE, TRUE),
(4, NULL, 'Muffin Arándanos', 'Muffin recién horneado de arándanos.', 24.00, 100, TRUE, TRUE),
(4, NULL, 'Expresso Machiato', 'Espresso con un toque de espuma de leche.', 22.00, 100, TRUE, TRUE),
(4, NULL, 'Latte Clásico', 'Espresso con leche vaporizada.', 26.00, 100, TRUE, TRUE),
(4, NULL, 'Mocha Chocolate Iced', 'Café frío con chocolate.', 30.00, 100, TRUE, TRUE),
(4, NULL, 'Mocha Chocolate Iced (Frío)', 'Versión bien fría del mocha de chocolate.', 30.00, 100, TRUE, TRUE),
(4, NULL, 'Frappé de Caramelo (Frío)', 'Frappé de caramelo bien frío.', 32.00, 100, TRUE, TRUE),
(4, NULL, 'Frappé de Caramelo con Helado', 'Frappé de caramelo con una bola de helado encima.', 36.00, 100, TRUE, TRUE),
-- Bebidas (5)
(5, NULL, 'Refresco Mediano', 'Bebida gaseosa de 16 oz.', 15.00, 100, TRUE, TRUE),
(5, NULL, 'Refresco Grande', 'Bebida gaseosa de 22 oz.', 18.00, 100, TRUE, TRUE),
(5, NULL, 'Limonada natural', 'Limonada preparada con limón fresco.', 18.00, 100, TRUE, TRUE),
(5, NULL, 'Jugo de naranja', 'Jugo natural recién exprimido', 20.00, 100, TRUE, TRUE),
(5, NULL, 'Malteada Chocolate', 'Malteada cremosa de chocolate.', 32.00, 100, TRUE, TRUE),
(5, NULL, 'Malteada Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, TRUE, TRUE),
(5, NULL, 'Agua Embotellada', 'Agua purificada de 600 ml.', 10.00, 100, TRUE, TRUE),
(5, NULL, 'Smoothie Tropical', 'Mango, piña y naranja licuados con hielo.', 34.00, 100, TRUE, TRUE),
(5, NULL, 'Bebida de Fresa', 'Bebida refrescante sabor fresa.', 20.00, 100, TRUE, TRUE),
(5, NULL, 'Botín de Pirata de Foxy', 'Bebida servida en vaso temático estilo bota pirata.', 25.00, 100, TRUE, TRUE),
(5, NULL, 'Ponche de Frutas', 'Mezcla de frutas tropicales rojas y naranjas en capas, con un toque cítrico y banderas pirata.', 22.00, 100, TRUE, TRUE),
(5, NULL, 'Granizado de Arándano', 'Granizado frío sabor arándano.', 24.00, 100, TRUE, TRUE),
(5, NULL, 'Malteada de Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, TRUE, TRUE),
(5, NULL, 'Slushie de Lima', 'Bebida helada sabor lima.', 22.00, 100, TRUE, TRUE),
(5, NULL, 'Smoothie de Durazno', 'Smoothie natural de durazno.', 30.00, 100, TRUE, TRUE),
(5, NULL, 'Té Helado', 'Té negro servido helado.', 18.00, 100, TRUE, TRUE),
-- Antojos (6)
(6, NULL, 'Papas Clásicas', 'Papas fritas doradas y crujientes.', 18.00, 100, TRUE, TRUE),
(6, NULL, 'Papas con Queso', 'Papas bañadas en queso cheddar.', 28.00, 100, TRUE, TRUE),
(6, NULL, 'Aros de Cebolla', 'Aros empanizados y crujientes.', 26.00, 100, TRUE, TRUE),
(6, NULL, 'Nuggets (6 piezas)', 'Nuggets de pollo con salsa BBQ.', 32.00, 100, TRUE, TRUE),
(6, NULL, 'Mozzarella Sticks', 'Palitos de queso mozzarella empanizados.', 34.00, 100, TRUE, TRUE),
(6, NULL, 'Alitas BBQ', 'Seis alitas bañadas en salsa BBQ.', 42.00, 100, TRUE, TRUE),
(6, NULL, 'Nachos Supreme', 'Nachos con queso, carne y jalapeños.', 39.00, 100, TRUE, TRUE),
(6, NULL, 'Papas Fazbear', 'Papas con tocino, queso cheddar y cebollín.', 38.00, 100, TRUE, TRUE),
(6, NULL, 'Alitas de Foxy', 'Alitas bañadas en salsa, tema Foxy.', 40.00, 100, TRUE, TRUE),
(6, NULL, 'Bocados de Maíz', 'Bocados crujientes de maíz.', 22.00, 100, TRUE, TRUE),
(6, NULL, 'Sartén de Queso', 'Queso fundido servido en sartén individual.', 30.00, 100, TRUE, TRUE),
-- Cajita Feliz (7) -- solo menú infantil, sin productos "Combo%"
(7, NULL, 'Cajita Freddy Burger', 'Mini hamburguesa, papas pequeñas, jugo y juguete coleccionable.', 46.00, 100, TRUE, TRUE),
(7, NULL, 'Cajita Nuggets', 'Cuatro nuggets, papas, bebida y juguete sorpresa.', 45.00, 100, TRUE, TRUE),
(7, NULL, 'Cajita Mini Pizza', 'Mini pizza, jugo y juguete.', 48.00, 100, TRUE, TRUE),
(7, NULL, 'Copa de Pastel de Chica', 'Un pastel helado con capas de pastel de vainilla, bebida pequeña y juguete de Chica.', 35.00, 100, TRUE, TRUE),
(7, NULL, 'Festín de Tacos de Bonnie', 'Tres tacos de carne asada estilo Fazbear, bebida y juguete de colección.', 52.00, 100, TRUE, TRUE),
(7, NULL, 'Paquete de Papas Shadow', 'Papas fritas rizadas con salsa Fazbear, café y juguete de Shadow Freddy.', 38.00, 100, TRUE, TRUE),
(7, NULL, 'Cajita Fazbear Deluxe', 'Hamburguesa infantil, postre pequeño y juguete exclusivo.', 52.00, 100, TRUE, TRUE),
(7, NULL, 'Paquete de Pizza de Chica', 'Mini pizza, bebida y juguete de Chica.', 46.00, 100, TRUE, TRUE),
-- Combos (8) -- productos "Combo%" que antes vivían en Cajita Feliz
(8, NULL, 'Combo Golden Pizza-Burger', 'Un combo dorado: burger premium con sabor a pizza, bebida grande y juguete.', 55.00, 100, TRUE, TRUE),
(8, NULL, 'Combo Bonnie-Nuggets', 'Nuggets, papas, bebida y juguete temático de Bonnie.', 48.00, 100, TRUE, TRUE),
(8, NULL, 'Combo Freddy Fazbear', 'Combo insignia con juguete de colección de Freddy.', 55.00, 100, TRUE, TRUE);

-- ------------------------------------------------------------
-- imagen de producto
-- BUG QUE ESTO CORRIGE: el INSERT INTO producto de arriba nunca
-- llenaba la columna 'imagen', así que TODOS los productos
-- quedaban con imagen = NULL. UtilImagenes.producto(null,...)
-- siempre cae en la imagen genérica de respaldo
-- (Comidarealista.png), sin importar qué archivos existan en
-- Resources/Productos. Por eso el catálogo del Cliente mostraba
-- la misma imagen genérica en TODAS las tarjetas: no era un bug
-- de la vista ni del DAO (ProductoDAOImpl.mapear ya asigna bien
-- rs.getString("imagen")), sino que la base de datos nunca pedía
-- ninguna imagen en particular. Se actualiza aquí el nombre exacto
-- de archivo (sin extensión, UtilImagenes agrega '.png') para cada
-- producto que sí tiene una imagen disponible en Resources/Productos.
-- Los productos sin archivo correspondiente quedan con imagen NULL
-- y usan el respaldo genérico a propósito (no falta un archivo real).
UPDATE producto SET imagen = 'Desayuno Fazbear Clasico' WHERE nombre = 'Desayuno Fazbear Clásico';
UPDATE producto SET imagen = 'Pancakes Freddy' WHERE nombre = 'Pancakes Freddy';
UPDATE producto SET imagen = 'Omelette Rockstar' WHERE nombre = 'Omelette Rockstar';
UPDATE producto SET imagen = 'Sandwich Morning Bite' WHERE nombre = 'Sándwich Morning Bite';
UPDATE producto SET imagen = 'Waffle Golden Bear' WHERE nombre = 'Waffle golden bear';
UPDATE producto SET imagen = 'Burrito Despertador' WHERE nombre = 'Burrito Despertador';
UPDATE producto SET imagen = 'Croissant Supremo' WHERE nombre = 'Croissant Supremo';
UPDATE producto SET imagen = 'Combo Buenos Dias' WHERE nombre = 'Combo Buenos días';
UPDATE producto SET imagen = 'Freddy Burger Deluxe' WHERE nombre = 'Freddy Burger Deluxe';
UPDATE producto SET imagen = 'Bonnie BBQ Burger' WHERE nombre = 'Bonnie BBQ Burger';
UPDATE producto SET imagen = 'Chica Chicken Burger' WHERE nombre = 'Chica Chicken Burger';
UPDATE producto SET imagen = 'Foxy Triple Burger' WHERE nombre = 'Foxy Triple Burger';
UPDATE producto SET imagen = 'Pizza Party Personal' WHERE nombre = 'Pizza Party Personal';
UPDATE producto SET imagen = 'Wrap Fazbear' WHERE nombre = 'Wrap Fazbear';
UPDATE producto SET imagen = 'Combo Fazbear Supremo' WHERE nombre = 'Combo Fazbear Supremo';
UPDATE producto SET imagen = 'Chicken Tenders Basket' WHERE nombre = 'Chicken Tenders Basket';
UPDATE producto SET imagen = 'Brownie Freddy' WHERE nombre = 'Brownie Freddy';
UPDATE producto SET imagen = 'Sundae Fazbear' WHERE nombre = 'Sundae Fazbear';
UPDATE producto SET imagen = 'Pastel Golden' WHERE nombre = 'Pastel Golden';
UPDATE producto SET imagen = 'Cheesecake Puppet' WHERE nombre = 'Cheesecake Puppet';
UPDATE producto SET imagen = 'Galletas Animatronic' WHERE nombre = 'Galletas Animatronic';
UPDATE producto SET imagen = 'Mini Donuts' WHERE nombre = 'Mini donuts';
UPDATE producto SET imagen = 'Banana Split Freddy' WHERE nombre = 'Banana Split Freddy';
UPDATE producto SET imagen = 'Volcan de Chocolate' WHERE nombre = 'Volcán de chocolate';
UPDATE producto SET imagen = 'Espresso Fazbear' WHERE nombre = 'Espresso Fazbear';
UPDATE producto SET imagen = 'Cappuccino Freddy' WHERE nombre = 'Cappuccino Freddy';
UPDATE producto SET imagen = 'Latte Vanilla' WHERE nombre = 'Latte Vainilla';
UPDATE producto SET imagen = 'Mocha Chica' WHERE nombre = 'Mocha Chica';
UPDATE producto SET imagen = 'Chocolate Caliente' WHERE nombre = 'Chocolate Caliente';
UPDATE producto SET imagen = 'Frappe Cookies' WHERE nombre = 'Frappé Cookies';
UPDATE producto SET imagen = 'Te Helado Limon' WHERE nombre = 'Té Helado Limón';
UPDATE producto SET imagen = 'Muffin Arandanos' WHERE nombre = 'Muffin Arándanos';
UPDATE producto SET imagen = 'Refresco Mediano' WHERE nombre = 'Refresco Mediano';
UPDATE producto SET imagen = 'Refresco Grande' WHERE nombre = 'Refresco Grande';
UPDATE producto SET imagen = 'Limonada Natural' WHERE nombre = 'Limonada natural';
UPDATE producto SET imagen = 'Jugo de Naranja' WHERE nombre = 'Jugo de naranja';
UPDATE producto SET imagen = 'Malteada Chocolate' WHERE nombre = 'Malteada Chocolate';
UPDATE producto SET imagen = 'Malteada Fresa' WHERE nombre = 'Malteada Fresa';
UPDATE producto SET imagen = 'Agua Embotellada' WHERE nombre = 'Agua Embotellada';
UPDATE producto SET imagen = 'Smoothie Tropical' WHERE nombre = 'Smoothie Tropical';
UPDATE producto SET imagen = 'Malteada Fresa' WHERE nombre = 'Malteada de Fresa';
UPDATE producto SET imagen = 'Papas Clasicas' WHERE nombre = 'Papas Clásicas';
UPDATE producto SET imagen = 'Papas con Queso' WHERE nombre = 'Papas con Queso';
UPDATE producto SET imagen = 'Aros de cebolla' WHERE nombre = 'Aros de Cebolla';
UPDATE producto SET imagen = 'Nuggets (6 piezas)' WHERE nombre = 'Nuggets (6 piezas)';
UPDATE producto SET imagen = 'Mozzarella Sticks' WHERE nombre = 'Mozzarella Sticks';
UPDATE producto SET imagen = 'Alitas BBQ' WHERE nombre = 'Alitas BBQ';
UPDATE producto SET imagen = 'Nachos Supreme' WHERE nombre = 'Nachos Supreme';
UPDATE producto SET imagen = 'Papas Fazbear' WHERE nombre = 'Papas Fazbear';
UPDATE producto SET imagen = 'Cajita Freddy Burger' WHERE nombre = 'Cajita Freddy Burger';
UPDATE producto SET imagen = 'Cajita Nuggets' WHERE nombre = 'Cajita Nuggets';
UPDATE producto SET imagen = 'Cajita Mini Pizza' WHERE nombre = 'Cajita Mini Pizza';
UPDATE producto SET imagen = 'Cajita Fazbear Deluxe' WHERE nombre = 'Cajita Fazbear Deluxe';

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

SELECT u.id_usuario, u.correo, r.nombre AS rol
FROM usuario u JOIN rol r ON r.id_rol = u.id_rol
WHERE u.correo = 'trabajador.prueba@freddyquickbite.com';
ALTER TABLE detalle_pedido
    ADD COLUMN observaciones VARCHAR(255) NULL AFTER subtotal;