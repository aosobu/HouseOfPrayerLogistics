alter table Medication add delivered BIT NOT NULL DEFAULT (0)

alter table Medication alter column batch INT NULL

alter table DroneMedicationBatch add delivered BIT NOT NULL DEFAULT (0)