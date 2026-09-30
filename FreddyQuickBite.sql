-- ============================================================
-- BASE DE DATOS: FreddyQuickBite
-- Proyecto: Freddy Fazbear's Quick Bite - Pantalla de autoservicio
-- ============================================================
-- Estructura del archivo:
--   1. Creación de la base de datos
--   2. Definición de tablas (DDL final)
--   3. Índices
--   4. Procedimientos, triggers y vistas
--   5. Datos iniciales (seed data)
--   6. Consultas de verificación (comentadas, opcionales)


-- 1. CREACIÓN DE LA BASE DE DATOS
DROP DATABASE IF EXISTS FreddyQuickBite;
CREATE DATABASE IF NOT EXISTS FreddyQuickBite;
USE FreddyQuickBite;


-- 2. CREACION DE TABLAS 

-- TABLA: rol
CREATE TABLE rol (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

-- TABLA: usuario
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

-- TABLA: categoria
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(50) NOT NULL UNIQUE,
    descripcion   VARCHAR(200),
    icono         VARCHAR(100),
    imagen        VARCHAR(255),
    estado        BOOLEAN DEFAULT TRUE
);

-- TABLA: promocion
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

-- TABLA: producto
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

-- TABLA: producto_categoria (N:M)
-- Permite que un producto aparezca en varias categorías 
CREATE TABLE producto_categoria (
    id_producto  INT NOT NULL,
    id_categoria INT NOT NULL,
    PRIMARY KEY (id_producto, id_categoria),
    FOREIGN KEY (id_producto)  REFERENCES producto(id_producto)   ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE CASCADE
);

-- TABLA: carrito
CREATE TABLE carrito (
    id_carrito      INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado          ENUM('Activo','Finalizado','Cancelado') DEFAULT 'Activo',
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- TABLA: carrito_detalle
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

-- TABLA: pedido
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

-- TABLA: detalle_pedido
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

-- TABLA: pago
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

-- TABLA: factura
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


-- 3. ÍNDICES
CREATE INDEX idx_usuario_correo ON usuario(correo);
CREATE INDEX idx_producto_categoria ON producto(id_categoria);
CREATE INDEX idx_pedido_usuario ON pedido(id_usuario);
CREATE INDEX idx_producto_nombre ON producto(nombre);
CREATE INDEX idx_pedido_estado ON pedido(estado);
CREATE INDEX idx_producto_stock ON producto(stock);
CREATE INDEX idx_detalle_pedido_pedido ON detalle_pedido(id_pedido);


-- 4. PROCEDIMIENTOS, TRIGGERS Y VISTAS

-- PROCEDIMIENTO: sp_insertar_producto
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

-- TRIGGER: trg_descontar_stock
-- Descuenta el stock automáticamente al registrar el detalle
-- de un pedido.
-- TRIGGER: trg_restaurar_stock
-- Devuelve el stock si un pedido pasa a estado 'Cancelado'
-- después de haber sido creado (evita que el inventario quede
-- descontado por pedidos que nunca se concretaron).
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

-- VISTA: vw_ventas_dia
-- Ventas agrupadas por día, para el módulo de Reportes.
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


-- 5. DATOS INICIALES 

-- rol
INSERT INTO rol (nombre, descripcion) VALUES
('Administrador', 'Control total'),
('Trabajador', 'Gestiona pedidos');

-- categoria
-- El orden define el id_categoria (los primeros 9 no cambian):
--   1 Desayunos, 2 Almuerzos y Cenas, 3 Postres, 4 McCafe,
--   5 Bebidas, 6 Antojos, 7 Cajita Feliz, 8 Combos, 9 Promociones,
--   10 Hamburguesas, 11 Pizzas, 12 Platos Fuertes.
INSERT INTO categoria (nombre, descripcion) VALUES
('Desayunos', 'Platos y bebidas que se ofrecen en la mañana'),
('Almuerzos y Cenas', 'Todo lo que se puede servir como comida principal al mediodía y en la noche'),
('Postres', 'Postres y dulces'),
('McCafe', 'Café y bebidas de cafetería'),
('Bebidas', 'Bebidas frías y refrescantes'),
('Antojos', 'Snacks y botanas'),
('Cajita Feliz', 'Menú infantil'),
('Combos', 'Combos para toda la familia'),
('Promociones', 'Ofertas especiales'),
('Hamburguesas', 'Todo lo que lleva hamburguesa'),
('Pizzas', 'Pizzas y combos que llevan pizza'),
('Platos Fuertes', 'Platos principales que no son hamburguesa ni pizza');

-- promocion
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

-- producto (con imagen incluida en la misma llamada)


-- Desayunos
CALL sp_insertar_producto(1, 'Desayuno Fazbear Clásico', 'Huevos revueltos, tocino crujiente, pan tostado y papas hash brown.', 48.00, 100, 'Desayuno Fazbear Clasico');
CALL sp_insertar_producto(1, 'Pancakes Freddy', 'Tres pancakes esponjosos con mantequilla y miel de maple.', 36.00, 100, 'Pancakes Freddy');
CALL sp_insertar_producto(1, 'Omelette Rockstar', 'Omelette relleno de jamón, queso cheddar y vegetales frescos.', 42.00, 100, 'Omelette Rockstar');
CALL sp_insertar_producto(1, 'Sándwich Morning Bite', 'Pan brioche con huevo, queso americano y salchicha artesanal.', 34.00, 100, 'Sandwich Morning Bite');
CALL sp_insertar_producto(1, 'Waffle golden bear', 'Waffle belga acompañado de frutas y crema batida.', 39.00, 100, 'Waffle Golden Bear');  -- +Postres
CALL sp_insertar_producto(1, 'Burrito Despertador', 'Tortilla rellena de huevo, queso, salchicha y papas.', 41.00, 100, 'Burrito Despertador');
CALL sp_insertar_producto(1, 'Croissant Supremo', 'Croissant relleno de jamón ahumado y queso mozzarella.', 32.00, 100, 'Croissant Supremo');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(8, 'Combo Buenos días', 'Café, jugo de naranja y muffin de vainilla', 38.00, 100, 'Combo Buenos Dias');  -- +Desayunos
CALL sp_insertar_producto(1, 'Burrito de Desayuno Grande', 'Tortilla rellena de huevo, queso, salchicha y papas, tamaño grande.', 42.00, 100, 'Burrito de Desayuno Grande');
CALL sp_insertar_producto(1, 'Pancakes Clásico', 'Tres pancakes esponjosos con mantequilla y miel.', 36.00, 100, 'Pancakes Clasico');
CALL sp_insertar_producto(1, 'Pancakes con Miel de Maple', 'Pancakes bañados en miel de maple auténtica.', 38.00, 100, 'Pancakes con Miel de Maple');

-- Platos principales (hamburguesas, pizza, wraps y platos fuertes)
CALL sp_insertar_producto(10, 'Freddy Burger Deluxe', 'Carne 100% res, doble queso cheddar, lechuga, tomate y salsa especial Quick Bite.', 58.00, 100, 'Freddy Burger Deluxe');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(10, 'Bonnie BBQ Burger', 'Hamburguesa con salsa BBQ, cebolla caramelizada y queso suizo.', 62.00, 100, 'Bonnie BBQ Burger');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(10, 'Chica Chicken Burger', 'Pechuga de pollo empanizada, queso y salsa miel-mostaza.', 54.00, 100, 'Chica Chicken Burger');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(10, 'Foxy Triple Burger', 'Triple carne, doble queso, tocino y pepinillos.', 72.00, 100, 'Foxy Triple Burger');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(11, 'Pizza Party Personal', 'Pizza individual de pepperoni con queso mozzarella.', 48.00, 100, 'Pizza Party Personal');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(12, 'Wrap Fazbear', 'Tortilla de harina con pollo, vegetales y aderezo ranch.', 44.00, 100, 'Wrap Fazbear');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(8, 'Combo Fazbear Supremo', 'Hamburguesa Deluxe, papas grandes y bebida mediana', 79.00, 100, 'Combo Fazbear Supremo');  -- +Hamburguesas, Almuerzos y Cenas
CALL sp_insertar_producto(12, 'Chicken Tenders Basket', 'Seis tiras de pollo con papas fritas y salsa BBQ.', 59.00, 100, 'Chicken Tenders Basket');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(12, 'Plato Fazbear Clásico', 'Plato principal insignia de la casa.', 55.00, 100, 'Plato Fazbear Clasico');  -- +Almuerzos y Cenas, Hamburguesas

-- Postres
CALL sp_insertar_producto(3, 'Brownie Freddy', 'Brownie de chocolate con helado de vainilla.', 28.00, 100, 'Brownie Freddy');
CALL sp_insertar_producto(3, 'Sundae Fazbear', 'Helado de vainilla con chocolate, nueces y cereza.', 24.00, 100, 'Sundae Fazbear');
CALL sp_insertar_producto(3, 'Pastel Golden', 'Rebanada de pastel de vainilla con crema.', 27.00, 100, 'Pastel Golden');
CALL sp_insertar_producto(3, 'Cheesecake Puppet', 'Cheesecake con salsa de frutos rojos.', 30.00, 100, 'Cheesecake Puppet');
CALL sp_insertar_producto(3, 'Galletas Animatronic', 'Cuatro galletas con chispas de chocolate.', 22.00, 100, 'Galletas Animatronic');
CALL sp_insertar_producto(3, 'Mini donuts', 'Seis mini donuts espolvoreadas con azúcar y canela.', 25.00, 100, 'Mini Donuts');  -- +Desayunos
CALL sp_insertar_producto(3, 'Banana Split Freddy', 'Helado, frutas, crema batida y chocolate.', 36.00, 100, 'Banana Split Freddy');
CALL sp_insertar_producto(3, 'Volcán de chocolate', 'Pastel tibio con centro líquido de chocolate.', 34.00, 100, 'Volcan de Chocolate');
CALL sp_insertar_producto(3, 'Bol de Acaí del Pirata', 'Bowl de acaí con fruta fresca y granola, estilo pirata.', 34.00, 100, 'Bol de Acai del Pirata');  -- +Desayunos
CALL sp_insertar_producto(3, 'Sundae de Helado', 'Copa de helado con toppings variados.', 26.00, 100, 'Sundae de Helado');
CALL sp_insertar_producto(3, 'Root Beer Float', 'Root beer con una bola de helado de vainilla.', 28.00, 100, 'Root Beer Float');  -- +Bebidas
CALL sp_insertar_producto(3, 'Waffles de Chocolate', 'Waffles bañados en chocolate.', 34.00, 100, 'Waffles de Chocolate');  -- +Desayunos

-- McCafe
CALL sp_insertar_producto(4, 'Espresso Fazbear', 'Café espresso de grano seleccionado.', 18.00, 100, 'Espresso Fazbear');  -- +Desayunos
CALL sp_insertar_producto(4, 'Cappuccino Freddy', 'Espresso con leche vaporizada y espuma cremosa.', 26.00, 100, 'Cappuccino Freddy');  -- +Desayunos
CALL sp_insertar_producto(4, 'Latte Vainilla', 'Café latte con un toque de vainilla.', 28.00, 100, 'Latte Vanilla');  -- +Desayunos
CALL sp_insertar_producto(4, 'Mocha Chica', 'Café con chocolate y crema batida.', 30.00, 100, 'Mocha Chica');  -- +Desayunos
CALL sp_insertar_producto(4, 'Chocolate Caliente', 'Chocolate caliente con malvaviscos.', 25.00, 100, 'Chocolate Caliente');  -- +Desayunos
CALL sp_insertar_producto(4, 'Frappé Cookies', 'Frappé de vainilla con galleta triturada.', 34.00, 100, 'Frappe Cookies');  -- +Bebidas
CALL sp_insertar_producto(5, 'Té Helado Limón', 'Té negro con limón natural.', 20.00, 100, 'Te Helado Limon');  -- +McCafe
CALL sp_insertar_producto(4, 'Muffin Arándanos', 'Muffin recién horneado de arándanos.', 24.00, 100, 'Muffin Arandanos');  -- +Desayunos, Postres
CALL sp_insertar_producto(4, 'Expresso Machiato', 'Espresso con un toque de espuma de leche.', 22.00, 100, 'Expresso Machiato');  -- +Desayunos
CALL sp_insertar_producto(4, 'Latte Clásico', 'Espresso con leche vaporizada.', 26.00, 100, 'Latte Clasico');  -- +Desayunos
CALL sp_insertar_producto(4, 'Mocha Chocolate Iced', 'Café frío con chocolate.', 30.00, 100, 'Mocha Chocolate Iced');  -- +Bebidas
CALL sp_insertar_producto(4, 'Mocha Chocolate Iced (Frío)', 'Versión bien fría del mocha de chocolate.', 30.00, 100, 'Mocha Chocolate Iced Frio');  -- +Bebidas
CALL sp_insertar_producto(4, 'Frappé de Caramelo (Frío)', 'Frappé de caramelo bien frío.', 32.00, 100, 'Frappe de Caramelo Frio');  -- +Bebidas
CALL sp_insertar_producto(4, 'Frappé de Caramelo con Helado', 'Frappé de caramelo con una bola de helado encima.', 36.00, 100, 'Frappe de Caramelo con Helado');  -- +Bebidas, Postres

-- Bebidas
CALL sp_insertar_producto(5, 'Refresco Mediano', 'Bebida gaseosa de 16 oz.', 15.00, 100, 'Refresco Mediano');
CALL sp_insertar_producto(5, 'Refresco Grande', 'Bebida gaseosa de 22 oz.', 18.00, 100, 'Refresco Grande');
CALL sp_insertar_producto(5, 'Limonada natural', 'Limonada preparada con limón fresco.', 18.00, 100, 'Limonada Natural');
CALL sp_insertar_producto(5, 'Jugo de naranja', 'Jugo natural recién exprimido', 20.00, 100, 'Jugo de Naranja');  -- +Desayunos
CALL sp_insertar_producto(5, 'Malteada Chocolate', 'Malteada cremosa de chocolate.', 32.00, 100, 'Malteada Chocolate');  -- +Postres
CALL sp_insertar_producto(5, 'Malteada Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, 'Malteada Fresa');  -- +Postres
CALL sp_insertar_producto(5, 'Agua Embotellada', 'Agua purificada de 600 ml.', 10.00, 100, 'Agua Embotellada');
CALL sp_insertar_producto(5, 'Smoothie Tropical', 'Mango, piña y naranja licuados con hielo.', 34.00, 100, 'Smoothie Tropical');
CALL sp_insertar_producto(5, 'Bebida de Fresa', 'Bebida refrescante sabor fresa.', 20.00, 100, 'Bebida de Fresa');
CALL sp_insertar_producto(5, 'Botín de Pirata de Foxy', 'Bebida servida en vaso temático estilo bota pirata.', 25.00, 100, 'Botin de Pirata de Foxy');
CALL sp_insertar_producto(5, 'Ponche de Frutas', 'Mezcla de frutas tropicales rojas y naranjas en capas, con un toque cítrico y banderas pirata.', 22.00, 100, 'Ponche de Frutas');
CALL sp_insertar_producto(5, 'Granizado de Arándano', 'Granizado frío sabor arándano.', 24.00, 100, 'Granizado de Arandano');
CALL sp_insertar_producto(5, 'Malteada de Fresa', 'Malteada cremosa de fresa natural.', 32.00, 100, 'Malteada Fresa');  -- +Postres
CALL sp_insertar_producto(5, 'Slushie de Lima', 'Bebida helada sabor lima.', 22.00, 100, 'Slushie de Lima');
CALL sp_insertar_producto(5, 'Smoothie de Durazno', 'Smoothie natural de durazno.', 30.00, 100, 'Smoothie de Durazno');
CALL sp_insertar_producto(5, 'Té Helado', 'Té negro servido helado.', 18.00, 100, 'Te Helado');

-- Antojos
CALL sp_insertar_producto(6, 'Papas Clásicas', 'Papas fritas doradas y crujientes.', 18.00, 100, 'Papas Clasicas');
CALL sp_insertar_producto(6, 'Papas con Queso', 'Papas bañadas en queso cheddar.', 28.00, 100, 'Papas con Queso');
CALL sp_insertar_producto(6, 'Aros de Cebolla', 'Aros empanizados y crujientes.', 26.00, 100, 'Aros de cebolla');
CALL sp_insertar_producto(6, 'Nuggets (6 piezas)', 'Nuggets de pollo con salsa BBQ.', 32.00, 100, 'Nuggets (6 piezas)');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(6, 'Mozzarella Sticks', 'Palitos de queso mozzarella empanizados.', 34.00, 100, 'Mozzarella Sticks');
CALL sp_insertar_producto(6, 'Alitas BBQ', 'Seis alitas bañadas en salsa BBQ.', 42.00, 100, 'Alitas BBQ');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(6, 'Nachos Supreme', 'Nachos con queso, carne y jalapeños.', 39.00, 100, 'Nachos Supreme');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(6, 'Papas Fazbear', 'Papas con tocino, queso cheddar y cebollín.', 38.00, 100, 'Papas Fazbear');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(6, 'Alitas de Foxy', 'Alitas bañadas en salsa, tema Foxy.', 40.00, 100, 'Alitas de Foxy');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(6, 'Bocados de Maíz', 'Bocados crujientes de maíz.', 22.00, 100, 'Bocados de Maiz');
CALL sp_insertar_producto(6, 'Sartén de Queso', 'Queso fundido servido en sartén individual.', 30.00, 100, 'Sarten de Queso');

-- Cajita Feliz
CALL sp_insertar_producto(7, 'Cajita Freddy Burger', 'Mini hamburguesa, papas pequeñas, jugo y juguete coleccionable.', 46.00, 100, 'Cajita Freddy Burger');  -- +Hamburguesas, Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Cajita Nuggets', 'Cuatro nuggets, papas, bebida y juguete sorpresa.', 45.00, 100, 'Cajita Nuggets');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Cajita Mini Pizza', 'Mini pizza, jugo y juguete.', 48.00, 100, 'Cajita Mini Pizza');  -- +Pizzas, Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Copa de Pastel de Chica', 'Un pastel helado con capas de pastel de vainilla, bebida pequeña y juguete de Chica.', 35.00, 100, 'Copa de Pastel de Chica');  -- +Postres
CALL sp_insertar_producto(7, 'Festín de Tacos de Bonnie', 'Tres tacos de carne asada estilo Fazbear, bebida y juguete de colección.', 52.00, 100, 'Festin de Tacos de Bonnie');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Paquete de Papas Shadow', 'Papas fritas rizadas con salsa Fazbear, café y juguete de Shadow Freddy.', 38.00, 100, 'Paquete de Papas Shadow');  -- +Antojos
CALL sp_insertar_producto(7, 'Cajita Fazbear Deluxe', 'Hamburguesa infantil, postre pequeño y juguete exclusivo.', 52.00, 100, 'Cajita Fazbear Deluxe');  -- +Hamburguesas, Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Paquete de Pizza de Chica', 'Mini pizza, bebida y juguete de Chica.', 46.00, 100, 'Paquete de Pizza de Chica');  -- +Pizzas, Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Cajita Chicken Wrap', 'Mini wrap de pollo, papas pequeñas, jugo y juguete sorpresa.', 45.00, 100, 'Cajita Chicken Wrap');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Cajita Hot Dog', 'Hot dog clásico, papas pequeñas, jugo y juguete sorpresa.', 42.00, 100, 'Cajita Hot Dog');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(7, 'Cajita Pancake Kids', 'Mini pancakes con miel, jugo y juguete sorpresa.', 40.00, 100, 'Cajita Pancake Kids');  -- +Desayunos
CALL sp_insertar_producto(7, 'Cajita Quesadilla', 'Mini quesadilla de queso, papas pequeñas, jugo y juguete sorpresa.', 43.00, 100, 'Cajita Quesadilla');  -- +Almuerzos y Cenas

-- Combos
CALL sp_insertar_producto(8, 'Combo Golden Pizza-Burger', 'Un combo dorado: burger premium con sabor a pizza, bebida grande y juguete.', 55.00, 100, 'Combo Golden Pizza-Burger');  -- +Hamburguesas, Pizzas, Almuerzos y Cenas
CALL sp_insertar_producto(8, 'Combo Bonnie-Nuggets', 'Nuggets, papas, bebida y juguete temático de Bonnie.', 48.00, 100, 'Combo Bonnie-Nuggets');  -- +Almuerzos y Cenas
CALL sp_insertar_producto(8, 'Combo Freddy Fazbear', 'Combo insignia con juguete de colección de Freddy.', 55.00, 100, 'Combo Freddy Fazbear');  -- +Hamburguesas, Pizzas, Almuerzos y Cenas

-- producto_categoria (N:M)
-- Paso 1: cada producto entra a su categoría PRINCIPAL.
-- Paso 2: se agregan las categorías ADICIONALES, una categoría por
--         bloque, para ver de un vistazo qué contiene cada una.
--         (Se busca por NOMBRE, no por número, para no depender del
--         orden de los ids.) ON DUPLICATE KEY hace que se pueda
--         volver a correr sin errores.
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT id_producto, id_categoria FROM producto;

-- Desayunos: 14 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Desayunos'
WHERE p.nombre IN (
    'Combo Buenos días',
    'Mini donuts',
    'Bol de Acaí del Pirata',
    'Waffles de Chocolate',
    'Espresso Fazbear',
    'Cappuccino Freddy',
    'Latte Vainilla',
    'Mocha Chica',
    'Chocolate Caliente',
    'Muffin Arándanos',
    'Expresso Machiato',
    'Latte Clásico',
    'Jugo de naranja',
    'Cajita Pancake Kids'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- Almuerzos y Cenas: 27 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Almuerzos y Cenas'
WHERE p.nombre IN (
    'Croissant Supremo',
    'Freddy Burger Deluxe',
    'Bonnie BBQ Burger',
    'Chica Chicken Burger',
    'Foxy Triple Burger',
    'Pizza Party Personal',
    'Wrap Fazbear',
    'Combo Fazbear Supremo',
    'Chicken Tenders Basket',
    'Plato Fazbear Clásico',
    'Nuggets (6 piezas)',
    'Alitas BBQ',
    'Nachos Supreme',
    'Papas Fazbear',
    'Alitas de Foxy',
    'Cajita Freddy Burger',
    'Cajita Nuggets',
    'Cajita Mini Pizza',
    'Festín de Tacos de Bonnie',
    'Cajita Fazbear Deluxe',
    'Paquete de Pizza de Chica',
    'Cajita Chicken Wrap',
    'Cajita Hot Dog',
    'Cajita Quesadilla',
    'Combo Golden Pizza-Burger',
    'Combo Bonnie-Nuggets',
    'Combo Freddy Fazbear'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- Postres: 7 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Postres'
WHERE p.nombre IN (
    'Waffle golden bear',
    'Muffin Arándanos',
    'Frappé de Caramelo con Helado',
    'Malteada Chocolate',
    'Malteada Fresa',
    'Malteada de Fresa',
    'Copa de Pastel de Chica'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- McCafe: 1 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'McCafe'
WHERE p.nombre IN (
    'Té Helado Limón'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- Bebidas: 6 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Bebidas'
WHERE p.nombre IN (
    'Root Beer Float',
    'Frappé Cookies',
    'Mocha Chocolate Iced',
    'Mocha Chocolate Iced (Frío)',
    'Frappé de Caramelo (Frío)',
    'Frappé de Caramelo con Helado'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- Antojos: 1 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Antojos'
WHERE p.nombre IN (
    'Paquete de Papas Shadow'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- Hamburguesas: 6 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Hamburguesas'
WHERE p.nombre IN (
    'Combo Fazbear Supremo',
    'Plato Fazbear Clásico',
    'Cajita Freddy Burger',
    'Cajita Fazbear Deluxe',
    'Combo Golden Pizza-Burger',
    'Combo Freddy Fazbear'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;

-- Pizzas: 4 producto(s) adicional(es)
INSERT INTO producto_categoria (id_producto, id_categoria)
SELECT p.id_producto, c.id_categoria
FROM producto p
JOIN categoria c ON c.nombre = 'Pizzas'
WHERE p.nombre IN (
    'Cajita Mini Pizza',
    'Paquete de Pizza de Chica',
    'Combo Golden Pizza-Burger',
    'Combo Freddy Fazbear'
)
ON DUPLICATE KEY UPDATE id_producto = producto_categoria.id_producto;


UPDATE carrito c
JOIN pedido p ON p.id_carrito = c.id_carrito
SET c.estado = 'Finalizado'
WHERE c.id_carrito > 0
  AND c.estado = 'Activo';


-- 6. CONSULTAS DE VERIFICACIÓN 

-- Productos con más de una categoría asociada:
 SELECT
     p.nombre AS producto,
     GROUP_CONCAT(c.nombre ORDER BY c.nombre SEPARATOR ', ') AS categorias
 FROM producto_categoria pc
 JOIN producto p  ON p.id_producto  = pc.id_producto
 JOIN categoria c ON c.id_categoria = pc.id_categoria
 GROUP BY p.id_producto
 HAVING COUNT(*) > 1;

-- Cuántos productos "vendibles" (estado=1, disponible=1, stock>0)
-- aparecen en el panel de cada categoría. Se cuenta con
-- producto_categoria (N:M), que es lo que usa perteneceACategoria();
-- contar solo producto.id_categoria daría números más bajos porque
-- ignora las categorías adicionales.
 SELECT
     c.nombre AS categoria,
     COUNT(*) AS productos_visibles_en_panel
 FROM producto_categoria pc
 JOIN producto p  ON p.id_producto  = pc.id_producto
 JOIN categoria c ON c.id_categoria = pc.id_categoria
 WHERE p.estado = 1
   AND p.disponible = 1
   AND p.stock > 0
 GROUP BY c.nombre
 ORDER BY c.nombre;

-- FREDDY-FAZBEAR'S QUICK BITE
-- Crea las cuentas de Administrador de los 4 contribuidores del

--   Axel Ramirez     - axelramirez@emilianisomascos.edu.gt
--   Melany Mejía     - melanymejia@emilianisomascos.edu.gt
--   Diego Quisqué    - diegoquisque@emilianisomascos.edu.gt
--   Cristhian Pocon  - cristhianpocon@emilianisomascos.edu.gt
--
-- Contraseña para las 4 cuentas: admin8181920

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