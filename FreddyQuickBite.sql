drop DATABASE IF EXISTS FreddyQuickBite;
CREATE DATABASE IF NOT EXISTS FreddyQuickBite;
USE FreddyQuickBite;

CREATE TABLE rol(
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);
CREATE TABLE usuario(

    id_usuario INT AUTO_INCREMENT PRIMARY KEY,

    id_rol INT NOT NULL,

    nombre VARCHAR(50) NOT NULL,

    apellido VARCHAR(50) NOT NULL,

    correo VARCHAR(100) NOT NULL UNIQUE,

    telefono VARCHAR(20),

    password VARCHAR(255) NOT NULL,

    fecha_nacimiento DATE,

    estado BOOLEAN DEFAULT TRUE,

    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	fecha_actualizacion TIMESTAMP
	DEFAULT CURRENT_TIMESTAMP
	ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(id_rol)
        REFERENCES rol(id_rol)

);
CREATE TABLE categoria(

    id_categoria INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(50) NOT NULL UNIQUE,

    descripcion VARCHAR(200),

    icono VARCHAR(100),

    imagen VARCHAR(255),

    estado BOOLEAN DEFAULT TRUE

);

CREATE TABLE promocion(

    id_promocion INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(80),

    descripcion VARCHAR(255),

    descuento DECIMAL(5,2),

    fecha_inicio DATE,

    fecha_fin DATE,

    estado BOOLEAN DEFAULT TRUE

);
CREATE TABLE producto(

    id_producto INT AUTO_INCREMENT PRIMARY KEY,

    id_categoria INT NOT NULL,

    id_promocion INT,

    nombre VARCHAR(100) NOT NULL,

    descripcion TEXT,

    precio DECIMAL(10,2) NOT NULL,

    stock INT NOT NULL DEFAULT 0,
	disponible BOOLEAN DEFAULT TRUE,
    imagen VARCHAR(255),

    estado BOOLEAN DEFAULT TRUE,

    FOREIGN KEY(id_categoria)
        REFERENCES categoria(id_categoria),

    FOREIGN KEY(id_promocion)
        REFERENCES promocion(id_promocion)

);
CREATE TABLE carrito(

    id_carrito INT AUTO_INCREMENT PRIMARY KEY,

    id_usuario INT NOT NULL,

    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    estado ENUM('Activo','Finalizado','Cancelado') DEFAULT 'Activo',

    FOREIGN KEY(id_usuario)
        REFERENCES usuario(id_usuario)

);
CREATE TABLE carrito_detalle(

    id_carrito_detalle INT AUTO_INCREMENT PRIMARY KEY,

    id_carrito INT NOT NULL,

    id_producto INT NOT NULL,

    cantidad INT NOT NULL,

    observaciones VARCHAR(255),

    FOREIGN KEY(id_carrito)
        REFERENCES carrito(id_carrito)
        ON DELETE CASCADE,

    FOREIGN KEY(id_producto)
        REFERENCES producto(id_producto)

);
CREATE TABLE pedido(

    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    
    numero_orden VARCHAR(20) UNIQUE,

    id_usuario INT NOT NULL,

    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    tipo_entrega ENUM(
'Comer en restaurante',
'Para llevar'
),
    estado ENUM(

        'Pendiente',
        'Preparacion',
        'Listo',
        'Entregado',
        'Cancelado'

    ) DEFAULT 'Pendiente',

    subtotal DECIMAL(10,2),

    descuento DECIMAL(10,2),

    total DECIMAL(10,2)DEFAULT 0,

    FOREIGN KEY(id_usuario)
        REFERENCES usuario(id_usuario)

);
CREATE TABLE detalle_pedido(

    id_detalle INT AUTO_INCREMENT PRIMARY KEY,

    id_pedido INT NOT NULL,

    id_producto INT NOT NULL,

    cantidad INT NOT NULL,

    precio DECIMAL(10,2) NOT NULL,

    subtotal DECIMAL(10,2)DEFAULT 0,


    FOREIGN KEY(id_pedido)
        REFERENCES pedido(id_pedido)
        ON DELETE CASCADE,

    FOREIGN KEY(id_producto)
        REFERENCES producto(id_producto)

);
CREATE TABLE pago(

    id_pago INT AUTO_INCREMENT PRIMARY KEY,

    id_pedido INT UNIQUE,

    metodo_pago ENUM(

        'Efectivo',
        'Tarjeta',
        'TRANSFERENCIA'

    ),

    monto DECIMAL(10,2),

    fecha_pago TIMESTAMP NULL,

    estado ENUM(

        'Pendiente',
        'Pagado',
        'Rechazado'

    ) DEFAULT 'Pendiente',

    FOREIGN KEY(id_pedido)
        REFERENCES pedido(id_pedido)

);
CREATE TABLE factura (

    id_factura INT AUTO_INCREMENT PRIMARY KEY,

    id_pedido INT NOT NULL UNIQUE,

    numero_factura VARCHAR(30) UNIQUE NOT NULL,

    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    nit VARCHAR(20),

    nombre_cliente VARCHAR(100),

    direccion VARCHAR(200),

    subtotal DECIMAL(10,2) NOT NULL,

    descuento DECIMAL(10,2) DEFAULT 0,

    iva DECIMAL(10,2) NOT NULL,

    total DECIMAL(10,2) NOT NULL,

    FOREIGN KEY(id_pedido)
        REFERENCES pedido(id_pedido)

);
INSERT INTO rol(nombre,descripcion) VALUES

('Administrador','Control total'),

('Trabajador','Gestiona pedidos'),

('Cliente','Realiza compras');
INSERT INTO categoria(nombre,descripcion) VALUES

('Desayunos','Menú de desayuno'),

('Almuerzos y Cenas','Comidas principales'),

('Postres','Postres'),

('McCafe','Café y bebidas calientes'),

('Bebidas','Bebidas frías'),

('Antojos','Snacks'),

('Cajita Feliz','Menú infantil'),

('Promociones','Ofertas especiales');

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
(2, NULL, 'Bonnie BBQ Burger', 'Hmaburguesa con salsa BBQ, cebolla caramelizada y queso suizo.', 62.00, 100, TRUE, TRUE),
(2, NULL, 'Chica Chiken Burger', 'Pechuga de pollo empanizada, queso y salsa miel-mostaza.', 54.00, 100, TRUE, TRUE),
(2, NULL, 'Foxy Triple Burger', 'Triple care, doble queso, tocino y pepinillos.', 72.00, 100, TRUE, TRUE),
(2, NULL, 'Pizza Party Personal', 'Pizza individual de pepperoni con queso mozzarella.', 48.00, 100, TRUE, TRUE),
(2, NULL, 'Wrap Fazbear', 'Tortilla de harina con pollo, vegetales y aderezo ranch.', 44.00, 100, TRUE, TRUE),
(2, NULL, 'Combo Fazbear Supremo', 'Hamburguesa Deluxe, papas grandes y bebida mediana', 79.00, 100, TRUE, TRUE),
(2, NULL, 'Chicken Tenders Basket', 'Seis tiras de pollo con papas fritas y salsa BBQ.', 59.00, 100, TRUE, TRUE),
-- Postres
(3, NULL, 'Brownie Freddy', 'Brownie de chocolate con helado de vainilla.', 28.00, 100, TRUE, TRUE),
(3, NULL, 'Sundae Fazbear', 'Helado de vainilla con chocolate, nueces y cereza.', 24.00, 100, TRUE, TRUE),
(3, NULL, 'Patel Golden', 'Rebanada de pastel de vainilla con crema.', 27.00, 100, TRUE, TRUE),
(3, NULL, 'Cheesecake Puppet', 'Cheesecake con salsa de frutos rojos.', 30.00, 100, TRUE, TRUE),
(3, NULL, 'Galletas Animatronic', 'Cuatro Galletas con chispas de chocolate.', 22.00, 100, TRUE, TRUE),
(3, NULL, 'Mini donuts', 'Seis mini donuts espolvoreadas con azúcar y canela.', 25.00, 100, TRUE, TRUE),
(3, NULL, 'Banana Split Freddy', 'Helado, frutas, crema batida y chocolate.', 36.00, 100, TRUE, TRUE),
(3, NULL, 'Volcán de chocolate', 'Pastel tibio con centro líquido de chocolate.', 34.00, 100, TRUE, TRUE),
-- McCafé
(4, NULL, 'Espresso Fazbear', 'Café espresso de grano seleccionado.', 18.00, 100, TRUE, TRUE),
(4, NULL, 'Cappucino Freddy', 'Espresso con leche vaporizada y espuma cremosa.', 26.00, 100, TRUE, TRUE),
(4, NULL, 'Latte Vainilla', 'Café latte con un toque de vainilla.', 28.00, 100, TRUE, TRUE),
(4, NULL, 'Mocha Chica', 'Café con chocolate y crema batida.', 30.00, 100, TRUE, TRUE),
(4, NULL, 'Chocolate Caliente', 'Chocolate caliente con malvaviscos.', 25.00, 100, TRUE, TRUE),
(4, NULL, 'Frappé Cookies', 'Frappé de vainilla con galleta triturada.', 34.00, 100, TRUE, TRUE),
(4, NULL, 'Té Helado Limón', 'Té negro con limón natural.', 20.00, 100, TRUE, TRUE),
(4, NULL, 'Muffin Arándanos', 'Muffin recién horneado de arándanos.', .00, 100, TRUE, TRUE),
-- Bebidas
(5, NULL, 'Refresco Mediano', 'Bebida gaseosa de 16 oz.', 15.00, 100, TRUE, TRUE),
(5, NULL, 'Refresco Grande', 'Bebida gaseosa de 22 oz.', 18.00, 100, TRUE, TRUE),
(5, NULL, 'Limonada natural', 'Limonada preparada con limón fresco.', 18.00, 100, TRUE, TRUE),
(5, NULL, 'Jugo de naranja', 'Jugo natural recién exprimido', 20.00, 100, TRUE, TRUE),
(5, NULL, 'Malteada Chocolate', '', 32.00, 100, TRUE, TRUE),
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


CREATE INDEX idx_usuario_correo
ON usuario(correo);
CREATE INDEX idx_producto_categoria
ON producto(id_categoria);
CREATE INDEX idx_pedido_usuario
ON pedido(id_usuario);
CREATE INDEX idx_producto_nombre
ON producto(nombre);
CREATE INDEX idx_pedido_estado
ON pedido(estado);
CREATE INDEX idx_producto_stock
ON producto(stock);
