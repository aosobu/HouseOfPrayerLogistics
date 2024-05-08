-- creates table Drone which stores information about all registered drones
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Drone' and xtype='U')
BEGIN
    CREATE TABLE Drone (
        id INT IDENTITY(1,1) PRIMARY KEY,
        serial varchar(102) NOT NULL UNIQUE,
        model varchar(15) NOT NULL,
        weight SMALLINT NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50),
        activated BIT DEFAULT 0
    )
END

---- creates table DroneState table which contains the list of possible drone states
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneStateType' and xtype='U')
BEGIN
    CREATE TABLE DroneStateType (
        id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
        type varchar(20) UNIQUE NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END

---- creates table DroneStateSnapshot table that holds current state for all active drones
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneActivitySnapshot' and xtype='U')
BEGIN
    CREATE TABLE DroneActivitySnapshot (
        id INT IDENTITY(1,1) PRIMARY KEY,
        state varchar(20) FOREIGN KEY REFERENCES DroneStateType(type) NOT NULL,
        drone INT FOREIGN KEY REFERENCES Drone(id),
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END

---- creates table Property table that holds state properties
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Property' and xtype='U')
BEGIN
    CREATE TABLE Property (
        id INT IDENTITY(1,1) PRIMARY KEY,
        property varchar(100) NOT NULL,
        state varchar(100) NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END

--creates DroneMedicationBatch - stores information about all dispatched medication batches associated with a drone
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneMedicationBatch' and xtype='U')
BEGIN
    CREATE TABLE DroneMedicationBatch (
        id INT IDENTITY(1,1) PRIMARY KEY,
        drone INT FOREIGN KEY REFERENCES Drone(id) NOT NULL,
        batch INT NOT NULL,
        CreatedDate DATETIME NOT NULL,
        UpdatedDate DATETIME DEFAULT(getDate()),
        CreatedBy varchar(50),
        UpdatedBy varchar(50)
    )
END

--creates table Medication - stores information about all items available for dispatch
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Medication' and xtype='U')
BEGIN
    CREATE TABLE Medication (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name varchar(100) NOT NULL,
        weight SMALLINT NOT NULL,
        code varchar(50) NOT NULL,
        image VARBINARY(MAX) NOT NULL,
        batch INT,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END

-- creates table DroneActivity - journals all events of a drone
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneActivity' and xtype='U')
BEGIN
    CREATE TABLE DroneActivity (
        ID INT IDENTITY(1,1) PRIMARY KEY,
        drone INT FOREIGN KEY REFERENCES Drone(id),
        type varchar(20) FOREIGN KEY REFERENCES DroneStateType(type) NOT NULL,
        batch INT FOREIGN KEY REFERENCES DroneMedicationBatch(id),
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END

-- creates table DroneBattery - journals all drone battery level
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneBattery' and xtype='U')
BEGIN
    CREATE TABLE DroneBattery (
        id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
        drone INT FOREIGN KEY REFERENCES Drone(id) NOT NULL,
        battery SMALLINT NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50),
    )
END

-- creates table DroneBatterySnapshot - specifies most recent drone battery level
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneBatterySnapshot' and xtype='U')
BEGIN
    CREATE TABLE DroneBatterySnapshot (
        id INT IDENTITY(1,1) PRIMARY KEY,
        drone INT FOREIGN KEY REFERENCES Drone(ID) NOT NULL,
        battery SMALLINT NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END


-- creates table DroneAudit - records drone audit event
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneAudit' and xtype='U')
BEGIN
    CREATE TABLE DroneAudit (
        id INT IDENTITY(1,1) PRIMARY KEY,
        type varchar(100) NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50) NOT NULL default('system'),
        updater varchar(50) NOT NULL default('system')
    )
END

-- creates table DroneAuditType - records drone audit types
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneAuditType' and xtype='U')
BEGIN
    CREATE TABLE DroneAuditType (
        ID INT IDENTITY(1,1) PRIMARY KEY,
        type varchar(100) NOT NULL UNIQUE,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50)
    )
END


-- creates table DroneBatteryAuditData - records drone battery data at audit time
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='DroneBatteryAuditData' and xtype='U')
BEGIN
    CREATE TABLE DroneBatteryAuditData (
        id INT IDENTITY(1,1) PRIMARY KEY,
        drone INT FOREIGN KEY REFERENCES Drone(ID),
        battery SMALLINT NOT NULL,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50) default('system'),
        updater varchar(50) default('system')
    )
END

