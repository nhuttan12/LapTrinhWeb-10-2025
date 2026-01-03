BEGIN;

-- ========================
-- USERS & ROLES
-- ========================
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) CHECK (status IN ('active','inactive')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS users (
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

CREATE TABLE IF NOT EXISTS user_details (
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
CREATE TABLE IF NOT EXISTS images (
    id SERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    status VARCHAR(50) CHECK (status IN ('active','inactive')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_images (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    image_id INT REFERENCES images(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- CATEGORY
-- ========================
--CREATE TABLE IF NOT EXISTS categories (
--    id SERIAL PRIMARY KEY,
--    name VARCHAR(255) UNIQUE NOT NULL,
--    status VARCHAR(50) CHECK (status IN ('active','inactive')) DEFAULT 'active',
--    created_at TIMESTAMP DEFAULT NOW(),
--    updated_at TIMESTAMP DEFAULT NOW()
--);

-- ========================
-- BRANDS
-- ========================
CREATE TABLE IF NOT EXISTS brands (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(50) CHECK (status IN ('active','inactive')) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- PRODUCTS
-- ========================
CREATE TABLE IF NOT EXISTS products (
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

CREATE TABLE IF NOT EXISTS product_details (
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

CREATE TABLE IF NOT EXISTS product_images (
    id SERIAL PRIMARY KEY,
    image_id INT REFERENCES images(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    type VARCHAR(50) CHECK (type IN ('thumbnail','gallery','product')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- CART
-- ========================
CREATE TABLE IF NOT EXISTS carts (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) CHECK (status IN ('open','checked_out','abandoned')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS cart_details (
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
CREATE TABLE IF NOT EXISTS wishlist_items (
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
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    price DOUBLE PRECISION NOT NULL,

    shipping_status VARCHAR(50) CHECK (
        shipping_status IN ('pending','shipped','completed','cancelled')
    ) NOT NULL DEFAULT 'pending',

    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS order_details (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- PAYMENTS 
-- ========================
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id) ON DELETE CASCADE,
    payment_method VARCHAR(50) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) DEFAULT 'pending',
    transaction_id VARCHAR(255),
    provider VARCHAR(100),
    raw_response JSONB,
    paid_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    -- Ràng buộc kiểm tra phương thức thanh toán
    CONSTRAINT payments_method_check CHECK (payment_method IN ('cod', 'bank_transfer', 'paypal_test', 'momo_test', 'vnpay_test')),
    -- Ràng buộc kiểm tra trạng thái thanh toán
    CONSTRAINT payments_status_check CHECK (status IN ('pending', 'completed', 'failed', 'refunded'))
);

COMMIT;