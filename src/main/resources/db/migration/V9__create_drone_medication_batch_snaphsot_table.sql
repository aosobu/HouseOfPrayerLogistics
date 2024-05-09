--creates DroneMedicationBatchSnapshot- stores information about all dispatched medication batches associated with a drone
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneMedicationBatchSnapshot' and xtype='U')
BEGIN
    CREATE TABLE DroneMedicationBatchSnapshot (
        id INT IDENTITY(1,1) PRIMARY KEY,
        drone INT FOREIGN KEY REFERENCES Drone(id) ,
        batch INT NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50) ,
        updater varchar(50) ,
        delivered BIT DEFAULT (0) NOT NULL
    )
END