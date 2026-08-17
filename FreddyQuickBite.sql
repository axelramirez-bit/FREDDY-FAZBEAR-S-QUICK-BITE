-- ============================================================
-- BASE DE DATOS: FreddyQuickBite
-- Proyecto: Freddy Fazbear's Quick Bite - Pantalla de autoservicio
-- Versión: CORREGIDA Y MEJORADA
-- ============================================================
-- Cambios respecto a la versión original:
--   1. pedido.subtotal / pedido.descuento ahora son NOT NULL DEFAULT 0
--      (antes permitían NULL y rompían los cálculos de total).
--   2. pedido ahora tiene id_carrito (FK opcional) para dejar
--      trazabilidad de qué carrito originó el pedido.
--   3. detalle_pedido ahora tiene id_promocion (FK opcional) para
--      saber qué promoción se aplicó a cada línea del pedido.
--   4. Se agregaron CHECK constraints para evitar precios,
--      descuentos y cantidades inválidas.
--   5. Se agregó un TRIGGER que descuenta el stock automáticamente
--      al insertar un detalle_pedido.
--   6. Se normalizó el ENUM de metodo_pago ('TRANSFERENCIA' -> 'Transferencia').
--   7. Se corrigió el precio faltante de "Muffin Arándanos" (.00 -> 24.00).
--   8. Se agregó una VISTA (vw_ventas_dia) para el módulo de Reportes.
--   9. Se agregaron índices adicionales documentados explícitamente.
-- ============================================================

DROP DATABASE IF EXISTS FreddyQuickBite;
CREATE DATABASE IF NOT EXISTS FreddyQuickBite;
USE FreddyQuickBite;

-- ============================================================
-- TABLA: rol
-- ============================================================
CREATE TABLE rol (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

-- ============================================================
-- TABLA: usuario
-- ============================================================
CREATE TABLE usuario (
    id_usuario          INT AUTO_INCREMENT PRIMARY KEY,
    id_rol               INT NOT NULL,
    nombre                VARCHAR(50) NOT NULL,
    apellido              VARCHAR(50) NOT NULL,
    correo                VARCHAR(100) NOT NULL UNIQUE,
    telefono              VARCHAR(20),
    password              VARCHAR(255) NOT NULL,
    fecha_nacimiento     DATE,
    estado                BOOLEAN DEFAULT TRUE,
    fecha_registro       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- ============================================================
-- TABLA: categoria
-- ============================================================
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(50) NOT NULL UNIQUE,
    descripcion   VARCHAR(200),
    icono         VARCHAR(100),
    imagen        VARCHAR(255),
    estado        BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- TABLA: promocion
-- ============================================================
CREATE TABLE promocion (
    id_promocion INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(80),
    descripcion   VARCHAR(255),
    descuento     DECIMAL(5,2),
    fecha_inicio DATE,
    fecha_fin    DATE,
    estado        BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_promocion_descuento CHECK (descuento IS NULL OR (descuento >= 0 AND descuento <= 100)),
    CONSTRAINT chk_promocion_fechas CHECK (fecha_inicio IS NULL OR fecha_fin IS NULL OR fecha_inicio <= fecha_fin)
);

-- ============================================================
-- TABLA: producto
-- ============================================================
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

-- ============================================================
-- TABLA: carrito
-- ============================================================
CREATE TABLE carrito (
    id_carrito      INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado          ENUM('Activo','Finalizado','Cancelado') DEFAULT 'Activo',
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ============================================================
-- TABLA: carrito_detalle
-- ============================================================
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

-- ============================================================
-- TABLA: pedido
-- MEJORA: se agregó id_carrito (FK opcional) para dejar
-- trazabilidad de qué carrito dio origen al pedido.
-- MEJORA: subtotal y descuento ahora son NOT NULL DEFAULT 0.
-- ============================================================
CREATE TABLE pedido (
    id_pedido     INT AUTO_INCREMENT PRIMARY KEY,
    numero_orden VARCHAR(20) UNIQUE,
    id_usuario    INT NOT NULL,
    id_carrito    INT NULL,
    fecha          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_entrega ENUM('Comer en restaurante','Para llevar'),
    estado         ENUM('Pendiente','Preparacion','Listo','Entregado','Cancelado') DEFAULT 'Pendiente',
    subtotal       DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento      DECIMAL(10,2) NOT NULL DEFAULT 0,
    total          DECIMAL(10,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito),
    CONSTRAINT chk_pedido_montos CHECK (subtotal >= 0 AND descuento >= 0 AND total >= 0)
);

-- ============================================================
-- TABLA: detalle_pedido
-- MEJORA: se agregó id_promocion (FK opcional) para saber qué
-- promoción se aplicó a esa línea específica del pedido.
-- ============================================================
CREATE TABLE detalle_pedido (
    id_detalle   INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido    INT NOT NULL,
    id_producto  INT NOT NULL,
    id_promocion INT NULL,
    cantidad      INT NOT NULL,
    precio        DECIMAL(10,2) NOT NULL,
    subtotal      DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_promocion) REFERENCES promocion(id_promocion),
    CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_detalle_precio CHECK (precio > 0)
);

-- ============================================================
-- TABLA: pago
-- MEJORA: ENUM normalizado ('TRANSFERENCIA' -> 'Transferencia')
-- ============================================================
CREATE TABLE pago (
    id_pago      INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido    INT UNIQUE,
    metodo_pago ENUM('Efectivo','Tarjeta','Transferencia'),
    monto         DECIMAL(10,2),
    fecha_pago   TIMESTAMP NULL,
    estado        ENUM('Pendiente','Pagado','Rechazado') DEFAULT 'Pendiente',
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT chk_pago_monto CHECK (monto IS NULL OR monto >= 0)
);

-- ============================================================
-- TABLA: factura
-- ============================================================
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
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT chk_factura_montos CHECK (subtotal >= 0 AND descuento >= 0 AND iva >= 0 AND total >= 0)
);

-- ============================================================
-- DATOS INICIALES: rol
-- ============================================================
INSERT INTO rol (nombre, descripcion) VALUES
('Administrador', 'Control total'),
('Trabajador', 'Gestiona pedidos'),
('Cliente', 'Realiza compras');

-- ============================================================
-- DATOS INICIALES: categoria
-- ============================================================
INSERT INTO categoria (nombre, descripcion) VALUES
('Desayunos', 'Menú de desayuno'),
('Almuerzos y Cenas', 'Comidas principales'),
('Postres', 'Postres'),
('McCafe', 'Café y bebidas calientes'),
('Bebidas', 'Bebidas frías'),
('Antojos', 'Snacks'),
('Cajita Feliz', 'Menú infantil'),
('Promociones', 'Ofertas especiales');

-- ============================================================
-- DATOS INICIALES: producto
-- MEJORA: se corrigió el precio de "Muffin Arándanos" (.00 -> 24.00)
-- ============================================================
INSERT INTO producto
(id_categoria, id_promocion, nombre, descripcion, precio, stock, disponible, estado)
VALUES
-- Desayunos
(1, NULL, 'Desayuno Fazbear Clásico', 'Huevos revueltos, tocino crujiente, pan tostado y papas hash brown.', 48.00, 100, TRUE, TRUE),
(1, NULL, 'Pancakes Freddy', 'Tres pancakes esponjosos con mantequilla y miel de maple.', 36.00, 100, TRUE, TRUE),
(1, NULL, 'Omelette Rockstar', 'Omelette relleno de jamón, queso cheddar y vegetales frescos.', 42.00, 100, TRUE, TRUE),
(1, NULL, 'Sándwich Morning Bite', 'Pan brioche con huevo, queso americano y salchicha artesanal.', 34.00, 100, TRUE, TRUE),
(1, NULL, 'Waffle golden bear', 'Waffle belga acompañado de frutas y crema batida.', 39.00, 100, TRUE, TRUE),
(1, NULL, 'Burrito Despertador', 'Tortilla rellena de huevo, queso, salchicha y papas.', 41.00, 100, TRUE, TRUE),
(1, NULL, 'Croissant Supremo', 'Croissant relleno de jamón ahumado y queso mozzarella.', 32.00, 100, TRUE, TRUE),
(1, NULL, 'Combo Buenos días', 'Café, jugo de naranja y muffin de vainilla', 38.00, 100, TRUE, TRUE),
-- Almuerzos
(2, NULL, 'Freddy Burger Deluxe', 'Carne 100% res, doble queso cheddar, lechuga, tomate y salsa especial Quick Bite.', 58.00, 100, TRUE, TRUE),
(2, NULL, 'Bonnie BBQ Burger', 'Hamburguesa con salsa BBQ, cebolla caramelizada y queso suizo.', 62.00, 100, TRUE, TRUE),
(2, NULL, 'Chica Chicken Burger', 'Pechuga de pollo empanizada, queso y salsa miel-mostaza.', 54.00, 100, TRUE, TRUE),
(2, NULL, 'Foxy Triple Burger', 'Triple carne, doble queso, tocino y pepinillos.', 72.00, 100, TRUE, TRUE),
(2, NULL, 'Pizza Party Personal', 'Pizza individual de pepperoni con queso mozzarella.', 48.00, 100, TRUE, TRUE),
(2, NULL, 'Wrap Fazbear', 'Tortilla de harina con pollo, vegetales y aderezo ranch.', 44.00, 100, TRUE, TRUE),
(2, NULL, 'Combo Fazbear Supremo', 'Hamburguesa Deluxe, papas grandes y bebida mediana', 79.00, 100, TRUE, TRUE),
(2, NULL, 'Chicken Tenders Basket', 'Seis tiras de pollo con papas fritas y salsa BBQ.', 59.00, 100, TRUE, TRUE),
-- Postres
(3, NULL, 'Brownie Freddy', 'Brownie de chocolate con helado de vainilla.', 28.00, 100, TRUE, TRUE),
(3, NULL, 'Sundae Fazbear', 'Helado de vainilla con chocolate, nueces y cereza.', 24.00, 100, TRUE, TRUE),
(3, NULL, 'Pastel Golden', 'Rebanada de pastel de vainilla con crema.', 27.00, 100, TRUE, TRUE),
(3, NULL, 'Cheesecake Puppet', 'Cheesecake con salsa de frutos rojos.', 30.00, 100, TRUE, TRUE),
(3, NULL, 'Galletas Animatronic', 'Cuatro galletas con chispas de chocolate.', 22.00, 100, TRUE, TRUE),
(3, NULL, 'Mini donuts', 'Seis mini donuts espolvoreadas con azúcar y canela.', 25.00, 100, TRUE, TRUE),
(3, NULL, 'Banana Split Freddy', 'Helado, frutas, crema batida y chocolate.', 36.00, 100, TRUE, TRUE),
(3, NULL, 'Volcán de chocolate', 'Pastel tibio con centro líquido de chocolate.', 34.00, 100, TRUE, TRUE),
-- McCafé
(4, NULL, 'Espresso Fazbear', 'Café espresso de grano seleccionado.', 18.00, 100, TRUE, TRUE),
(4, NULL, 'Cappuccino Freddy', 'Espresso con leche vaporizada y espuma cremosa.', 26.00, 100, TRUE, TRUE),
(4, NULL, 'Latte Vainilla', 'Café latte con un toque de vainilla.', 28.00, 100, TRUE, TRUE),
(4, NULL, 'Mocha Chica', 'Café con chocolate y crema batida.', 30.00, 100, TRUE, TRUE),
(4, NULL, 'Chocolate Caliente', 'Chocolate caliente con malvaviscos.', 25.00, 100, TRUE, TRUE),
(4, NULL, 'Frappé Cookies', 'Frappé de vainilla con galleta triturada.', 34.00, 100, TRUE, TRUE),
(4, NULL, 'Té Helado Limón', 'Té negro con limón natural.', 20.00, 100, TRUE, TRUE),
(4, NULL, 'Muffin Arándanos', 'Muffin recién horneado de arándanos.', 24.00, 100, TRUE, TRUE),
-- Bebidas
(5, NULL, 'Refresco Mediano', 'Bebida gaseosa de 16 oz.', 15.00, 100, TRUE, TRUE),
(5, NULL, 'Refresco Grande', 'Bebida gaseosa de 22 oz.', 18.00, 100, TRUE, TRUE),
(5, NULL, 'Limonada natural', 'Limonada preparada con limón fresco.', 18.00, 100, TRUE, TRUE),
(5, NULL, 'Jugo de naranja', 'Jugo natural recién exprimido', 20.00, 100, TRUE, TRUE),
(5, NULL, 'Malteada Chocolate', 'Malteada cremosa de chocolate.', 32.00, 100, TRUE, TRUE),
(5, NULL, 'Malteada Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, TRUE, TRUE),
(5, NULL, 'Agua Embotellada', 'Agua purificada de 600 ml.', 10.00, 100, TRUE, TRUE),
(5, NULL, 'Smoothie Tropical', 'Mango, piña y naranja licuados con hielo.', 34.00, 100, TRUE, TRUE),
-- Antojos
(6, NULL, 'Papas Clásicas', 'Papas fritas doradas y crujientes.', 18.00, 100, TRUE, TRUE),
(6, NULL, 'Papas con Queso', 'Papas bañadas en queso cheddar.', 28.00, 100, TRUE, TRUE),
(6, NULL, 'Aros de Cebolla', 'Aros empanizados y crujientes.', 26.00, 100, TRUE, TRUE),
(6, NULL, 'Nuggets (6 piezas)', 'Nuggets de pollo con salsa BBQ.', 32.00, 100, TRUE, TRUE),
(6, NULL, 'Mozzarella Sticks', 'Palitos de queso mozzarella empanizados.', 34.00, 100, TRUE, TRUE),
(6, NULL, 'Alitas BBQ', 'Seis alitas bañadas en salsa BBQ.', 42.00, 100, TRUE, TRUE),
(6, NULL, 'Nachos Supreme', 'Nachos con queso, carne y jalapeños.', 39.00, 100, TRUE, TRUE),
(6, NULL, 'Papas Fazbear', 'Papas con tocino, queso cheddar y cebollín.', 38.00, 100, TRUE, TRUE),
-- Combos
(7, NULL, 'Cajita Freddy Burger', 'Mini hamburguesa, papas pequeñas, jugo y juguete coleccionable.', 46.00, 100, TRUE, TRUE),
(7, NULL, 'Cajita Nuggets', 'Cuatro nuggets, papas, bebida y juguete sorpresa.', 45.00, 100, TRUE, TRUE),
(7, NULL, 'Cajita Mini Pizza', 'Mini pizza, jugo y juguete.', 48.00, 100, TRUE, TRUE),
(7, NULL, 'Copa de Pastel de Chica', 'Un pastel helado con capas de pastel de vainilla, bebida pequeña y juguete de Chica.', 35.00, 100, TRUE, TRUE),
(7, NULL, 'Festín de Tacos de Bonnie', 'Tres tacos de carne asada estilo Fazbear, bebida y juguete de colección.', 52.00, 100, TRUE, TRUE),
(7, NULL, 'Paquete de Papas Shadow', 'Papas fritas rizadas con salsa Fazbear, café y juguete de Shadow Freddy.', 38.00, 100, TRUE, TRUE),
(7, NULL, 'Combo Golden Pizza-Burger', 'Un combo dorado: burger premium con sabor a pizza, bebida grande y juguete.', 55.00, 100, TRUE, TRUE),
(7, NULL, 'Cajita Fazbear Deluxe', 'Hamburguesa infantil, postre pequeño y juguete exclusivo.', 52.00, 100, TRUE, TRUE);

-- ============================================================
-- DATOS INICIALES: promocion
-- ============================================================
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

-- ============================================================
-- ÍNDICES
-- MEJORA: se agregó idx_detalle_pedido_pedido explícito
-- (documenta la consulta más frecuente: "dame el detalle de este pedido").
-- ============================================================
CREATE INDEX idx_usuario_correo ON usuario(correo);
CREATE INDEX idx_producto_categoria ON producto(id_categoria);
CREATE INDEX idx_pedido_usuario ON pedido(id_usuario);
CREATE INDEX idx_producto_nombre ON producto(nombre);
CREATE INDEX idx_pedido_estado ON pedido(estado);
CREATE INDEX idx_producto_stock ON producto(stock);
CREATE INDEX idx_detalle_pedido_pedido ON detalle_pedido(id_pedido);

-- ============================================================
-- PROCEDIMIENTO: sp_insertar_producto
-- ============================================================
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

-- ============================================================
-- MEJORA: TRIGGER que descuenta el stock automáticamente al
-- registrar el detalle de un pedido. Así el stock siempre queda
-- sincronizado sin depender de que el DAO en Java lo recuerde.
-- ============================================================
DELIMITER //

CREATE TRIGGER trg_descontar_stock
AFTER INSERT ON detalle_pedido
FOR EACH ROW
BEGIN
    UPDATE producto
    SET stock = stock - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END //

DELIMITER ;

-- ============================================================
-- MEJORA: VISTA para el módulo de Reportes (ventas por día).
-- Evita reescribir el mismo JOIN cada vez desde Java.
-- ============================================================
CREATE VIEW vw_ventas_dia AS
SELECT
    DATE(p.fecha)      AS dia,
    COUNT(DISTINCT p.id_pedido) AS total_pedidos,
    SUM(p.subtotal)    AS total_subtotal,
    SUM(p.descuento)   AS total_descuento,
    SUM(p.total)       AS total_ventas
FROM pedido p
WHERE p.estado <> 'Cancelado'
GROUP BY DATE(p.fecha);

-- ---------------------------------------------------------------
-- MENÚ ADICIONAL, usando el procedimiento sp_insertar_producto
-- ---------------------------------------------------------------
-- id_categoria: 1 Desayunos, 2 Almuerzos y Cenas, 3 Postres,
--               4 McCafe, 5 Bebidas, 6 Antojos, 7 Combos
--
-- IMPORTANTE: precio y descripcion son valores de referencia,
-- no datos reales del negocio — ajústalos tú. El único precio
-- confirmado es Ponche de Frutas (Q22.00), porque venía en el
-- mockup de diseño que ya revisamos.

-- Desayunos
CALL sp_insertar_producto(1, 'Burrito de Desayuno Grande', 'Tortilla rellena de huevo, queso, salchicha y papas, tamaño grande.', 42.00, 100, 'Burrito de desayuno Grande.png');
CALL sp_insertar_producto(1, 'Pancakes Clásico', 'Tres pancakes esponjosos con mantequilla y miel.', 36.00, 100, 'Pancakes Clásico.png');
CALL sp_insertar_producto(1, 'Pancakes con Miel de Maple', 'Pancakes bañados en miel de maple auténtica.', 38.00, 100, 'Pancakes con Miel de Maple.png');

-- Almuerzos y Cenas
CALL sp_insertar_producto(2, 'Plato Fazbear Clásico', 'Plato principal insignia de la casa.', 55.00, 100, 'Plato Fazbear Clásico.png');

-- Postres
CALL sp_insertar_producto(3, 'Bol de Acaí del Pirata', 'Bowl de acaí con fruta fresca y granola, estilo pirata.', 34.00, 100, 'Bol de Acaí del pirata.png');
CALL sp_insertar_producto(3, 'Sundae de Helado', 'Copa de helado con toppings variados.', 26.00, 100, 'Sundae de Helado.png');
CALL sp_insertar_producto(3, 'Root Beer Float', 'Root beer con una bola de helado de vainilla.', 28.00, 100, 'Root Beer Float.png');
CALL sp_insertar_producto(3, 'Waffles de Chocolate', 'Waffles bañados en chocolate.', 34.00, 100, 'Waffles de chocolate.png');

-- McCafé
CALL sp_insertar_producto(4, 'Expresso Machiato', 'Espresso con un toque de espuma de leche.', 22.00, 100, 'expresso Machiado.png');
CALL sp_insertar_producto(4, 'Latte Clásico', 'Espresso con leche vaporizada.', 26.00, 100, 'Latte Clasico.png');
CALL sp_insertar_producto(4, 'Mocha Chocolate Iced', 'Café frío con chocolate.', 30.00, 100, 'Macha de chocolate Iced.png');
CALL sp_insertar_producto(4, 'Mocha Chocolate Iced (Frío)', 'Versión bien fría del mocha de chocolate.', 30.00, 100, 'Macha de Chocolate Iced(Frio).png');
CALL sp_insertar_producto(4, 'Frappé de Caramelo (Frío)', 'Frappé de caramelo bien frío.', 32.00, 100, 'Frappé de Caramelo (Frio).png');
CALL sp_insertar_producto(4, 'Frappé de Caramelo con Helado', 'Frappé de caramelo con una bola de helado encima.', 36.00, 100, 'frappe de caramelo con bola de helado.png');

-- Bebidas
CALL sp_insertar_producto(5, 'Bebida de Fresa', 'Bebida refrescante sabor fresa.', 20.00, 100, 'bebida de fresa.png');
CALL sp_insertar_producto(5, 'Botín de Pirata de Foxy', 'Bebida servida en vaso temático estilo bota pirata.', 25.00, 100, 'Botín de pirata de Foxy.png');
CALL sp_insertar_producto(5, 'Ponche de Frutas', 'Mezcla de frutas tropicales rojas y naranjas en capas, con un toque cítrico y banderas pirata.', 22.00, 100, 'Ponche de Frutas.png');
CALL sp_insertar_producto(5, 'Granizado de Arándano', 'Granizado frío sabor arándano.', 24.00, 100, 'Granizado de Arándano.png');
CALL sp_insertar_producto(5, 'Malteada de Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, 'Malteada de fresa.png');
CALL sp_insertar_producto(5, 'Slushie de Lima', 'Bebida helada sabor lima.', 22.00, 100, 'Slushie de Lima.png');
CALL sp_insertar_producto(5, 'Smoothie de Durazno', 'Smoothie natural de durazno.', 30.00, 100, 'Smoothie de Durazno.png');
CALL sp_insertar_producto(5, 'Té Helado', 'Té negro servido helado.', 18.00, 100, 'Té helada.png');

-- Antojos
CALL sp_insertar_producto(6, 'Alitas de Foxy', 'Alitas bañadas en salsa, tema Foxy.', 40.00, 100, 'Alitas de Foxy.png');
CALL sp_insertar_producto(6, 'Aros de Cebolla', 'Aros empanizados y crujientes.', 26.00, 100, 'Aros de cebolla.png');
CALL sp_insertar_producto(6, 'Bocados de Maíz', 'Bocados crujientes de maíz.', 22.00, 100, 'Bocados de Maiz.png');
CALL sp_insertar_producto(6, 'Sartén de Queso', 'Queso fundido servido en sartén individual.', 30.00, 100, 'Sartén de Queso.png');

-- Combos
CALL sp_insertar_producto(7, 'Combo Bonnie-Nuggets', 'Nuggets, papas, bebida y juguete temático de Bonnie.', 48.00, 100, 'Combo Bonnie-Nuggets.png');
CALL sp_insertar_producto(7, 'Combo Freddy Fazbear', 'Combo insignia con juguete de colección de Freddy.', 55.00, 100, 'Combo Freddy Fazbear.png');
CALL sp_insertar_producto(7, 'Paquete de Pizza de Chica', 'Mini pizza, bebida y juguete de Chica.', 46.00, 100, 'Paquete de pizza de Chica.png');

-- ============================================================
-- MIGRACIÓN: agregar categoría "Combos" y reasignar productos
-- ============================================================
-- Contexto: PanelCombos.java filtra por categoria.getNombre()
-- == "Combos", pero esa categoría nunca existió en la base de
-- datos. Los productos tipo combo estaban mezclados dentro de
-- "Cajita Feliz" junto con los menús infantiles.
-- Ejecutar sobre la base de datos FreddyQuickBite ya creada.
-- ============================================================

USE FreddyQuickBite;

-- 1. Crear la categoría que falta
INSERT INTO categoria (nombre, descripcion, estado)
VALUES ('Combos', 'Combos para toda la familia', TRUE);

-- 2. Mover a "Combos" los productos que hoy están en "Cajita Feliz"
--    pero su nombre empieza con "Combo" (combos de adultos, no menú
--    infantil). Los que sí son menú infantil (ej. "Cajita Freddy
--    Burger", "Paquete de pizza de Chica") se quedan en Cajita Feliz.
UPDATE producto p
JOIN categoria origen ON origen.id_categoria = p.id_categoria
                      AND origen.nombre = 'Cajita Feliz'
JOIN categoria destino ON destino.nombre = 'Combos'
SET p.id_categoria = destino.id_categoria
WHERE p.nombre LIKE 'Combo%';

-- 3. Verificar el resultado de la migración
SELECT c.nombre AS categoria, p.nombre AS producto
FROM producto p
JOIN categoria c ON c.id_categoria = p.id_categoria
WHERE c.nombre IN ('Cajita Feliz', 'Combos')
ORDER BY c.nombre, p.nombre;

-- ============================================================
-- VERIFICACIÓN DE DATOS DE PRUEBA (según lo que se pidió)
-- ============================================================
-- listarProductosDisponibles() en ProductoServiceImpl exige
-- estado = 1 AND disponible = 1 AND stock > 0. Esta consulta
-- muestra cuántos productos "vendibles" hay por categoría; si
-- alguna categoría sale en 0, ese panel se verá vacío aunque el
-- filtro Java esté correcto.
SELECT
    c.nombre AS categoria,
    COUNT(*) AS productos_visibles_en_panel
FROM producto p
JOIN categoria c ON c.id_categoria = p.id_categoria
WHERE p.estado = 1
  AND p.disponible = 1
  AND p.stock > 0
GROUP BY c.nombre
ORDER BY c.nombre;

-- ============================================================
-- CORRECCIÓN DE IMÁGENES DE PRODUCTO
-- ============================================================
-- Causa raíz de "todos los productos muestran la misma imagen
-- genérica (Comidarealista.png)":
--
--  1. ProductoDAOImpl nunca llamaba producto.setImagenPrincipal(...)
--     al leer de la base de datos -> ya corregido en el código Java
--     (ver fix_imagenes_y_busqueda.zip).
--
--  2. La mayoría de productos del catálogo (los 56 del INSERT
--     grande) se insertaron con imagen = NULL, aunque SÍ existe un
--     archivo .png real para 52 de ellos en Resources/Productos.
--     Este script asigna el nombre correcto.
--
--  3. Los archivos .png reales tenían una codificación rota en su
--     nombre (ej. "Cl#U00e1sico.png" en vez de "Clásico.png"). Se
--     renombraron a ASCII sin acentos (ver
--     Resources_Productos_renombradas.zip) y este script usa esos
--     nombres ya limpios, SIN extensión ".png" (UtilImagenes la
--     agrega sola: usarla con extensión de nuevo genera
--     "archivo.png.png" y también cae al genérico).
--
-- IMPORTANTE: reemplaza tu carpeta Resources/Productos completa
-- con el contenido de Resources_Productos_renombradas.zip ANTES
-- de correr este script, o los nombres no van a coincidir con
-- ningún archivo real.
-- ============================================================

USE FreddyQuickBite;

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
-- Productos que se quedan SIN imagen real (no existe archivo
-- .png para ellos en Resources/Productos): mostrarán el genérico
-- de respaldo hasta que se les cree una imagen real.
--   - Copa de Pastel de Chica
--   - Festín de Tacos de Bonnie
--   - Paquete de Papas Shadow
--   - Combo Golden Pizza-Burger
--   - Todos los productos agregados con CALL sp_insertar_producto
--     (Burrito de Desayuno Grande, Plato Fazbear Clásico, etc.):
--     esos fueron ejemplos de referencia que yo agregué, no venían
--     con imagen real del equipo. O les crean una imagen y la
--     asignan con UPDATE, o los eliminan si no son parte del menú
--     real.
-- ------------------------------------------------------------

-- ------------------------------------------------------------
-- Limpieza de doble extensión: por si alguno de los productos
-- del bloque CALL sp_insertar_producto quedó con imagen
-- terminada en ".png" (ej. 'Granizado de Arándano.png'), lo
-- cual generaría "archivo.png.png" al cargar. Esto la deja en
-- blanco (sin imagen real de todas formas) para que caiga
-- limpiamente al genérico en vez de intentar una ruta rota.
-- ------------------------------------------------------------
SET SQL_SAFE_UPDATES = 0;

UPDATE producto 
SET imagen = NULL 
WHERE imagen LIKE '%.png';

SET SQL_SAFE_UPDATES = 1;

-- ------------------------------------------------------------
-- Verificación final: productos sin imagen real asignada
-- ------------------------------------------------------------
SELECT nombre FROM producto WHERE imagen IS NULL OR imagen = '' ORDER BY nombre;