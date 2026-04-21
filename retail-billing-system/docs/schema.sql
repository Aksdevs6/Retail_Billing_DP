-- ============================================================
-- RETAIL BILLING SYSTEM - DATABASE SCHEMA
-- Textile Shop Use Case
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS retail_billing_db;
USE retail_billing_db;

-- ============================================================
-- TABLE 1: product
-- Stores all textile products available in the shop
-- ============================================================
CREATE TABLE IF NOT EXISTS product (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,   -- Unique product ID
    name        VARCHAR(100) NOT NULL,                -- Product name (e.g., "Cotton Saree")
    price       DOUBLE NOT NULL,                      -- Price per unit
    quantity    INT NOT NULL,                          -- Available stock
    gst_percent DOUBLE DEFAULT 5.0                    -- GST % (optional, default 5%)
);

-- ============================================================
-- TABLE 2: bill
-- Each bill represents one purchase transaction
-- ============================================================
CREATE TABLE IF NOT EXISTS bill (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Unique bill ID
    bill_date       DATETIME NOT NULL,                   -- Date & time of purchase
    total_amount    DOUBLE NOT NULL,                     -- Total before GST
    gst_amount      DOUBLE DEFAULT 0.0,                  -- Total GST amount
    discount_amount DOUBLE DEFAULT 0.0,                  -- Discount applied
    net_amount      DOUBLE NOT NULL                      -- Final payable amount
);

-- ============================================================
-- TABLE 3: bill_item
-- Each row = one product line in a bill
-- Links bill ↔ product (many-to-many via this join table)
-- ============================================================
CREATE TABLE IF NOT EXISTS bill_item (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Unique item ID
    bill_id     BIGINT NOT NULL,                    -- Foreign key → bill
    product_id  BIGINT NOT NULL,                    -- Foreign key → product
    quantity    INT NOT NULL,                        -- Qty purchased
    unit_price  DOUBLE NOT NULL,                    -- Price at time of purchase
    total_price DOUBLE NOT NULL,                    -- quantity × unit_price

    -- Foreign Key constraints
    CONSTRAINT fk_bill     FOREIGN KEY (bill_id)    REFERENCES bill(id)    ON DELETE CASCADE,
    CONSTRAINT fk_product  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT
);

-- ============================================================
-- SAMPLE DATA (for testing)
-- ============================================================
INSERT INTO product (name, price, quantity, gst_percent) VALUES
    ('Cotton Saree',    850.00, 50, 5.0),
    ('Silk Dupatta',    450.00, 30, 12.0),
    ('Linen Kurta',     650.00, 40, 5.0),
    ('Woolen Shawl',   1200.00, 20, 12.0),
    ('Cotton Shirt',    550.00, 60, 5.0);
