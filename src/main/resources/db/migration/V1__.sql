CREATE TABLE customer
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE registerjwt
(
    id    BIGINT       NOT NULL,
    token VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT pk_registerjwt PRIMARY KEY (id)
);

CREATE TABLE retailer
(
    id              BIGINT       NOT NULL,
    company_address VARCHAR(255) NOT NULL,
    company_brand   VARCHAR(255) NOT NULL,
    company_city    VARCHAR(255) NOT NULL,
    company_pincode VARCHAR(255) NOT NULL,
    company_state   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_retailer PRIMARY KEY (id)
);

CREATE TABLE user
(
    id               BIGINT       NOT NULL,
    email            VARCHAR(255) NOT NULL,
    password         VARCHAR(255) NOT NULL,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(255) NOT NULL,
    country_code     VARCHAR(5)   NOT NULL,
    user_status      SMALLINT     NOT NULL,
    user_type        SMALLINT     NOT NULL,
    created_at       datetime NULL,
    last_modified_at datetime NULL,
    is_deleted       BIT(1)       NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE verification_code
(
    id                             BIGINT       NOT NULL,
    email                          VARCHAR(255) NOT NULL,
    phone_number_verification_code VARCHAR(255) NULL,
    email_verification_code        VARCHAR(255) NULL,
    verification_code              VARCHAR(255) NULL,
    CONSTRAINT pk_verificationcode PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_abab829049de9e1a4182939ca UNIQUE (phone_number, country_code);

ALTER TABLE registerjwt
    ADD CONSTRAINT uc_registerjwt_email UNIQUE (email);

ALTER TABLE registerjwt
    ADD CONSTRAINT uc_registerjwt_token UNIQUE (token);

ALTER TABLE retailer
    ADD CONSTRAINT uc_retailer_companybrand UNIQUE (company_brand);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE verification_code
    ADD CONSTRAINT uc_verificationcode_email UNIQUE (email);

CREATE INDEX idx_email ON user (email);

CREATE INDEX idx_phone_country ON user (phone_number, country_code);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_ID FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE retailer
    ADD CONSTRAINT FK_RETAILER_ON_ID FOREIGN KEY (id) REFERENCES user (id);