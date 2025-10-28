CREATE TABLE calculations (
    id BIGSERIAL PRIMARY KEY,
    first_number VARCHAR(255) NOT NULL,
    first_number_system VARCHAR(20) NOT NULL,
    second_number VARCHAR(255) NOT NULL,
    second_number_system VARCHAR(20) NOT NULL,
    operation_type VARCHAR(20) NOT NULL,
    result VARCHAR(255) NOT NULL,
    calculation_date TIMESTAMP NOT NULL
);