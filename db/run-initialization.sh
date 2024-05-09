sleep 90s

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P myPassword@123! -d master -i create-database.sql
