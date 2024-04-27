-- creates table Drone - stores information about all registered drones
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Drone' and xtype='U')
BEGIN
    CREATE TABLE Drone (
        ID INT NOT NULL PRIMARY KEY,
        SerialNumber varchar(100) NOT NULL UNIQUE,
        Model varchar(15) NOT NULL,
        Weight SMALLINT NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL,
        isActivated BIT NOT NULL DEFAULT 0
    )
END

-- creates table DroneState table which contains the list of possible drone states
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneStateType' and xtype='U')
BEGIN
    CREATE TABLE DroneStateType (
        ID INT NOT NULL PRIMARY KEY,
        DroneStateTypeId varchar(20) UNIQUE NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

-- creates table DroneStateSnapshot table that holds current state for all active drones
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneStateSnapshot' and xtype='U')
BEGIN
    CREATE TABLE DroneStateSnapshot (
        ID INT NOT NULL PRIMARY KEY,
        DroneStateTypeId varchar(20) FOREIGN KEY REFERENCES DroneStateType(DroneStateTypeId) NOT NULL,
        DroneId INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

-- creates table DroneActivity - journals all events of a drone
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneActivity' and xtype='U')
BEGIN
    CREATE TABLE DroneActivity (
        ID INT NOT NULL PRIMARY KEY,
        DroneId INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        DroneStateTypeId varchar(20) FOREIGN KEY REFERENCES DroneStateType(DroneStateTypeId) NOT NULL,
        BatchId INT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

-- creates table DroneBattery - journals all drone battery level
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneBattery' and xtype='U')
BEGIN
    CREATE TABLE DroneBattery (
        ID INT NOT NULL PRIMARY KEY,
        DroneId INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        BatteryCapacity SMALLINT NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL,
        CONSTRAINT BATTERY_CHECK CHECK( BatteryCapacity >= 0 AND BatteryCapacity <= 100 )
    )
END

-- creates table DroneBatterySnapshot - specifies most recent drone battery level
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneBatterySnapshot' and xtype='U')
BEGIN
    CREATE TABLE DroneBatterySnapshot (
        ID INT NOT NULL PRIMARY KEY,
        DroneId INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        BatteryCapacity SMALLINT NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END


-- creates table DroneAudit - records drone audit event
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneAudit' and xtype='U')
BEGIN
    CREATE TABLE DroneAudit (
        ID INT NOT NULL PRIMARY KEY,
        AuditEvent varchar(100) NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

-- creates table DroneAuditType - records drone audit types
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneAuditType' and xtype='U')
BEGIN
    CREATE TABLE DroneAuditType (
        ID INT NOT NULL PRIMARY KEY,
        DroneAuditTypeId varchar(100) NOT NULL UNIQUE,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END


-- creates table DroneBatteryAuditData - records drone battery data at audit time
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneBatteryAuditData' and xtype='U')
BEGIN
    CREATE TABLE DroneBatteryAuditData (
        ID INT NOT NULL PRIMARY KEY,
        DroneId INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        BatteryCapacity SMALLINT NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

--creates table Medication - stores information about all items available for dispatch
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Medication' and xtype='U')
BEGIN
    CREATE TABLE Medication (
        ID INT NOT NULL PRIMARY KEY,
        Name varchar(100) NOT NULL,
        Weight SMALLINT NOT NULL,
        Code varchar(50) NOT NULL,
        MedicationImage VARBINARY(MAX) NOT NULL,
        BatchId INT NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

--creates DroneMedicationBatch - stores information about all dispatched medication batches associated with a drone
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneMedicationBatch' and xtype='U')
BEGIN
    CREATE TABLE DroneMedicationBatch (
        ID INT NOT NULL PRIMARY KEY,
        DroneId INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        BatchId INT NOT NULL,
        CreatedDate DATETIME NOT NULL DEFAULT(getDate()),
        UpdatedDate DATETIME NOT NULL DEFAULT(getDate()),
        CreatedBy varchar(50) NOT NULL,
        UpdatedBy varchar(50) NOT NULL
    )
END

