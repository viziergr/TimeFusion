CREATE TABLE `team` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` longtext,
  `color` varchar(7),
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
)