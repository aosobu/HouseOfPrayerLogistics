FROM maven:3.8.5-openjdk-17

WORKDIR $HOME/Documents/musala-logistics-roughsheet/db
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run