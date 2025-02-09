create table if not exists vehicle
(
    id                 integer       primary key generated always as identity,
    alias              varchar(50),
    country_code       varchar(50)   not null,
    vehicle_type       varchar(50)   not null,
    region             varchar(50),
    owner_id           varchar(50)   not null,
    license_plate      varchar(50)   not null,
    status             varchar(50)   not null,
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

create table if not exists trailer
(
    id                      integer         primary key generated always as identity,
    owner_id                varchar(50)     not null,
    trailer_type            varchar(50)     not null,
    status                  varchar(50)     not null,
    license_plate           varchar(50)     not null,
    country_code            varchar(50)     not null,
    region                  varchar(50),
    maximum_freight_weight  integer         not null,
    md_length_centimeters   decimal(10, 3)  not null,
    md_width_centimeters    decimal(10, 3)  not null,
    md_height_centimeters   decimal(10, 3)  not null,
    fd_length_centimeters   decimal(10, 3),
    fd_width_centimeters    decimal(10, 3),
    fd_height_centimeters   decimal(10, 3),
    rd_length_centimeters   decimal(10, 3),
    rd_width_centimeters    decimal(10, 3),
    rd_height_centimeters   decimal(10, 3),
    created_at              timestamp       not null,
    modified_at             timestamp       not null,
    created_by              varchar(50)     not null,
    modified_by             varchar(50)     not null,
    UNIQUE NULLS NOT DISTINCT(country_code, region, license_plate)
);

CREATE INDEX idx_trailer_owner ON trailer (owner_id);
CREATE INDEX idx_trailer_status ON trailer (status);
CREATE INDEX idx_vehicle_status ON vehicle (status);
CREATE INDEX idx_vehicle_owner ON vehicle (owner_id);
CREATE INDEX idx_vehicle_lic_plate ON vehicle (license_plate);
CREATE INDEX idx_vehicle_mileage ON vehicle (mileage);