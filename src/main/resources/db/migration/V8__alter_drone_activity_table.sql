alter table DroneActivity add delivered bit default(0)

alter table DroneActivitySnapshot add delivered bit default(0)

alter table DroneActivitySnapshot add batch int null