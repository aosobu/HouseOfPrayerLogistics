alter table Medication add column delivered BIT NOT NULL DEFAULT (0)
alter table Medication modify column batch INT NULL

alter table DroneMedicationBatch add column delivered BIT NOT NULL DEFAULT (0)
alter table DroneMedicationBatch modify column batch INT NULL