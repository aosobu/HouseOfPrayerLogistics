FROM mcr.microsoft.com/mssql/server:2019-latest

WORKDIR $HOME/Documents/musala-logistics-roughsheet/app

COPY . $HOME/Documents/musala-logistics-roughsheet/app

ENV SA_PASSWORD myPassword@123
ENV ACCEPT_EULA Y
ENV MSSQL_PID Express

EXPOSE 1433

CMD /bin/bash ./entrypoint.sh