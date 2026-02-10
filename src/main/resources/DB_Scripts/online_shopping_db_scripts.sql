create DATABASE groceryshopping_db;

show databases;
use groceryshopping_db;

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(10) UNIQUE NOT NULL,
    product_name VARCHAR(50),
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00;
);


CREATE TABLE packaging_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    package_price DECIMAL(10,2) NOT NULL,
	CONSTRAINT uk_product_quantity UNIQUE (product_code, quantity),
    CONSTRAINT fk_packaging_product
        FOREIGN KEY (product_code)
        REFERENCES product(product_code)
        ON DELETE CASCADE
);

show tables;

describe product;
select * from product;

describe packaging_option;
select * from packaging_option;
