alter table DroneAudit
add foreign key(type)
references DroneAuditType(type);

alter table Users
add username varchar(80) NOT NULL