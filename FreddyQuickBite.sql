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