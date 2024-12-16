create table if not exists vehicle
(
    id                 serial        primary key,
    alias              varchar(50),
    country_code       varchar(50)   not null,
    region             varchar(50),
    license_plate      varchar(50)   not null,
    license_plate_date timestamp     not null,
    weight             int           not null,
    mileage            int           not null,
    make               varchar(50),
    model              varchar(50),
    version            varchar(50),
    created_at         timestamp     not null,
    modified_at        timestamp     not null,
    created_by         varchar(50)   not null,
    modified_by        varchar(50)   not null,
    UNIQUE NULLS NOT DISTINCT(country_code, region, license_plate)
);

CREATE INDEX idx_lic_plate ON vehicle (license_plate);
CREATE INDEX idx_mileage ON vehicle (mileage);