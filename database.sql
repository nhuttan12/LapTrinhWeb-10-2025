DO $$
BEGIN
    -- The DO block automatically manages the transaction:
    -- it commits on successful exit and rolls back on an unhandled error.
    
    RAISE NOTICE 'Starting table creation...';

    -- USERS Table
    CREATE TABLE users (
        id BIGSERIAL PRIMARY KEY,
        username VARCHAR(255) UNIQUE NOT NULL,
        password TEXT NOT NULL,
        full_name VARCHAR(255),
        email TEXT UNIQUE NOT NULL,
        role VARCHAR(50) DEFAULT 'user',
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- IMAGES Table
    CREATE TABLE images (
        id BIGSERIAL PRIMARY KEY,
        url TEXT NOT NULL,
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- PRODUCTS Table
    CREATE TABLE products (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        price DOUBLE PRECISION NOT NULL,
        discount DOUBLE PRECISION,
        status VARCHAR(50),
        category VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- ORDERS Table
    CREATE TABLE orders (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        price DOUBLE PRECISION NOT NULL,
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- CARTS Table
    CREATE TABLE carts (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT UNIQUE NOT NULL,
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- USER_DETAILS Table
    CREATE TABLE user_details (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT UNIQUE NOT NULL REFERENCES users(id),
        address_1 TEXT,
        address_2 TEXT,
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- PRODUCT_DETAILS Table
    CREATE TABLE product_details (
        id BIGSERIAL PRIMARY KEY,
        product_id BIGINT UNIQUE NOT NULL REFERENCES products(id),
        size VARCHAR(255),
        color VARCHAR(255),
        description TEXT,
        rating DOUBLE PRECISION,
        review_count INTEGER,
        inventory_stock INTEGER DEFAULT 0,
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- ORDER_DETAILS Table
    CREATE TABLE order_details (
        id BIGSERIAL PRIMARY KEY,
        order_id BIGINT NOT NULL REFERENCES orders(id),
        product_id BIGINT NOT NULL REFERENCES products(id),
        quantity INTEGER NOT NULL,
        price DOUBLE PRECISION NOT NULL,
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- CART_DETAILS Table
    CREATE TABLE cart_details (
        id BIGSERIAL PRIMARY KEY,
        cart_id BIGINT NOT NULL REFERENCES carts(id),
        product_id BIGINT NOT NULL REFERENCES products(id),
        quantity INTEGER NOT NULL,
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- REVIEWS Table
    CREATE TABLE reviews (
        id BIGSERIAL PRIMARY KEY,
        product_id BIGINT NOT NULL REFERENCES products(id),
        user_id BIGINT NOT NULL REFERENCES users(id),
        name VARCHAR(255) NOT NULL,
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- WISHLIST_ITEMS Table
    CREATE TABLE wishlist_items (
        id BIGSERIAL PRIMARY KEY,
        product_id BIGINT NOT NULL REFERENCES products(id),
        user_id BIGINT NOT NULL REFERENCES users(id),
        status VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE,
        UNIQUE (product_id, user_id)
    );

    -- Junction Tables
    CREATE TABLE user_images (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL REFERENCES users(id),
        image_id BIGINT NOT NULL REFERENCES images(id),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    CREATE TABLE product_images (
        id BIGSERIAL PRIMARY KEY,
        product_id BIGINT NOT NULL REFERENCES products(id),
        image_id BIGINT NOT NULL REFERENCES images(id),
        type VARCHAR(50),
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITHOUT TIME ZONE
    );

    -- Add remaining Foreign Keys
    ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user_id
    FOREIGN KEY (user_id) REFERENCES users(id);

    ALTER TABLE carts
    ADD CONSTRAINT fk_carts_user_id
    FOREIGN KEY (user_id) REFERENCES users(id);
    
    RAISE NOTICE '✅ SUCCESS: All tables created and constraints applied.';
    
    -- The transaction COMMITs here automatically upon successful exit.

-- Catch any error that occurs during the process
EXCEPTION
    WHEN OTHERS THEN
        -- The transaction ROLLBACKs here automatically upon error.
        RAISE EXCEPTION '❌ FAILURE: Table creation failed. Error: %', SQLERRM;
        
END $$;