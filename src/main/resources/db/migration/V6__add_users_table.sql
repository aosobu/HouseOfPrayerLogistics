--creates table User - stores information about all users of the application
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Users' and xtype='U')
BEGIN
    CREATE TABLE Users (
        id INT IDENTITY(1,1) PRIMARY KEY,
        email varchar(255) NOT NULL,
        password varchar(255) NOT NULL,
        Role varchar(50) NOT NULL,
        enabled bit,
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
        creator varchar(50),
        updater varchar(50) 
    )
END

--creates table UserAudit - stores information about all users of the application
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='UserAudit' and xtype='U')
BEGIN
    CREATE TABLE UserAudit (
        id INT IDENTITY(1,1) PRIMARY KEY,
        event varchar(50) NOT NULL ,
        details varchar(255) NOT NULL ,
        initiator INT FOREIGN KEY REFERENCES Users(id),
        created DATETIME NOT NULL,
        updated DATETIME DEFAULT(getDate()),
    )
END