alter table vehicle add owner_id varchar(50);

CREATE INDEX idx_owner ON vehicle (owner_id);