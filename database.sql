BEGIN;

-- ========================
-- USERS & ROLES
-- ========================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) CHECK (status IN ('active','inactive')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    full_name VARCHAR(255),
    email TEXT UNIQUE,
    status VARCHAR(50) CHECK (status IN ('active','inactive')),
    role_id INT REFERENCES roles(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE user_details (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    phone VARCHAR(25),
    address_1 TEXT,
    address_2 TEXT,
    address_3 TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- IMAGES
-- ========================
CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    status VARCHAR(50) CHECK (status IN ('active','inactive')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE user_images (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    image_id INT REFERENCES images(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- CATEGORY
-- ========================
--CREATE TABLE categories (
--    id SERIAL PRIMARY KEY,
--    name VARCHAR(255) UNIQUE NOT NULL,
--    status VARCHAR(50) CHECK (status IN ('active','inactive')) DEFAULT 'active',
--    created_at TIMESTAMP DEFAULT NOW(),
--    updated_at TIMESTAMP DEFAULT NOW()
--);

-- ========================
-- BRANDS
-- ========================
CREATE TABLE brands (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(50) CHECK (status IN ('active','inactive')) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- PRODUCTS
-- ========================
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    discount DOUBLE PRECISION DEFAULT 0,
    status VARCHAR(50) CHECK (status IN ('active','inactive')),
--    category_id INT REFERENCES categories(id) ON DELETE SET NULL,
    category VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE product_details (
    id SERIAL PRIMARY KEY,
    product_id INT UNIQUE REFERENCES products(id) ON DELETE CASCADE,
    brand_id INT REFERENCES brands(id) ON DELETE CASCADE,
--	brand VARCHAR(100),
	os VARCHAR(255),
	ram INT,
	storage INT,
	battery_capacity INT,
	screen_size DECIMAL(4, 2),
	screen_resolution VARCHAR(255),
	mobile_network VARCHAR(255),
	cpu VARCHAR(255),
	gpu VARCHAR(255),
	water_resistance VARCHAR(255),
	max_charge_watt INT,
	design VARCHAR(255),
	memory_card VARCHAR(255),
	cpu_speed DECIMAL(4, 2),
	release_date TIMESTAMP,
	description TEXT,
	rating DOUBLE PRECISION DEFAULT 0.0,
	created_at TIMESTAMP DEFAULT NOW(),
	updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE product_images (
    id SERIAL PRIMARY KEY,
    image_id INT REFERENCES images(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    type VARCHAR(50) CHECK (type IN ('thumbnail','gallery','other')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- CART
-- ========================
CREATE TABLE carts (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) CHECK (status IN ('open','checked_out','abandoned')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE cart_details (
    id SERIAL PRIMARY KEY,
    cart_id INT REFERENCES carts(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(50) CHECK (status IN ('active','removed')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- WISHLIST
-- ========================
CREATE TABLE wishlist_items (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) CHECK (status IN ('active','removed')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- ORDERS
-- ========================
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    price DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) CHECK (status IN ('pending','paid','shipped','completed','cancelled')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE order_details (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

COMMIT;