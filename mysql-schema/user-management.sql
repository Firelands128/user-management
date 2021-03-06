-- MySQL Script generated by MySQL Workbench
-- Tue Jan 22 15:27:01 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET
@
OLD_UNIQUE_CHECKS
=
@
@
UNIQUE_CHECKS,
UNIQUE_CHECKS
=
0;
SET
@
OLD_FOREIGN_KEY_CHECKS
=
@
@
FOREIGN_KEY_CHECKS,
FOREIGN_KEY_CHECKS
=
0;
SET
@
OLD_SQL_MODE
=
@
@
SQL_MODE,
SQL_MODE
=
'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema user-management
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema user-management
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `user-management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE
`user-management`;

-- -----------------------------------------------------
-- Table `user-management`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`user`;

CREATE TABLE IF NOT EXISTS `user-management`.`user`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR
(
  32
) NOT NULL,
  `password` VARCHAR
(
  60
) NOT NULL,
  `status` TINYINT
(
  4
) NOT NULL,
  `user_type` TINYINT
(
  4
) NULL DEFAULT NULL,
  `phone` VARCHAR
(
  16
) NULL DEFAULT NULL,
  `email` VARCHAR
(
  32
) NULL DEFAULT NULL,
  `name` VARCHAR
(
  64
) NULL DEFAULT NULL,
  `avatar_url` VARCHAR
(
  200
) NULL DEFAULT NULL,
  `gender` TINYINT
(
  4
) NOT NULL,
  `birthday` DATE NULL DEFAULT NULL,
  `area` VARCHAR
(
  100
) NULL DEFAULT NULL,
  `address` VARCHAR
(
  100
) NULL DEFAULT NULL,
  `county` VARCHAR
(
  32
) NULL DEFAULT NULL,
  `city` VARCHAR
(
  32
) NULL DEFAULT NULL,
  `province` VARCHAR
(
  32
) NULL DEFAULT NULL,
  `country` VARCHAR
(
  32
) NULL DEFAULT NULL,
  `others` VARCHAR
(
  100
) NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `username_UNIQUE`
(
  `username` ASC
) VISIBLE,
  UNIQUE INDEX `id_UNIQUE`
(
  `id` ASC
) VISIBLE,
  UNIQUE INDEX `email_UNIQUE`
(
  `email` ASC
) VISIBLE,
  UNIQUE INDEX `phone_UNIQUE`
(
  `phone` ASC
) VISIBLE)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `user-management`.`employee_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`employee_profile`;

CREATE TABLE IF NOT EXISTS `user-management`.`employee_profile`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT
(
  64
) NOT NULL,
  `id_org` BIGINT
(
  64
) NOT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `id_unique`
(
  `id` ASC
) VISIBLE,
  UNIQUE INDEX `user_id_UNIQUE`
(
  `user_id` ASC
) VISIBLE,
  CONSTRAINT `employee_user_id`
  FOREIGN KEY
(
  `user_id`
)
  REFERENCES `user-management`.`user`
(
  `id`
))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `user-management`.`normal_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`normal_profile`;

CREATE TABLE IF NOT EXISTS `user-management`.`normal_profile`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT
(
  64
) NOT NULL,
  `id_org` BIGINT
(
  64
) NOT NULL,
  `id_employee` BIGINT
(
  64
) NULL DEFAULT NULL,
  `id_parent` BIGINT
(
  64
) NULL DEFAULT NULL,
  `school` VARCHAR
(
  100
) NULL DEFAULT NULL,
  `grade` INT
(
  11
) NULL DEFAULT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `id_unique`
(
  `id` ASC
) VISIBLE,
  UNIQUE INDEX `user_id_UNIQUE`
(
  `user_id` ASC
) VISIBLE,
  INDEX `user_id_idx`
(
  `user_id` ASC
) VISIBLE,
  CONSTRAINT `normal_user_id`
  FOREIGN KEY
(
  `user_id`
)
  REFERENCES `user-management`.`user`
(
  `id`
))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `user-management`.`oauth_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`oauth_user`;

CREATE TABLE IF NOT EXISTS `user-management`.`oauth_user`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT
(
  64
) NOT NULL,
  `oauth_type` TINYINT
(
  4
) NOT NULL,
  `oauth_id` VARCHAR
(
  64
) NOT NULL,
  `union_id` VARCHAR
(
  64
) NULL DEFAULT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `id_UNIQUE`
(
  `id` ASC
) VISIBLE,
  UNIQUE INDEX `normal_user_id_UNIQUE`
(
  `user_id` ASC
) VISIBLE,
  UNIQUE INDEX `oauth_id_UNIQUE`
(
  `oauth_id` ASC
) VISIBLE,
  CONSTRAINT `oauth_user_id`
  FOREIGN KEY
(
  `user_id`
)
  REFERENCES `user-management`.`user`
(
  `id`
)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `user-management`.`organization_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`organization_profile`;

CREATE TABLE IF NOT EXISTS `user-management`.`organization_profile`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT
(
  64
) NOT NULL,
  `id_parent` BIGINT
(
  64
) NULL DEFAULT NULL,
  `officer_name` VARCHAR
(
  32
) NOT NULL,
  `officer_phone` VARCHAR
(
  16
) NOT NULL,
  `description` VARCHAR
(
  200
) NULL DEFAULT NULL,
  `license` VARCHAR
(
  100
) NOT NULL,
  `license_copy` VARCHAR
(
  100
) NULL DEFAULT NULL,
  `type` TINYINT
(
  4
) NOT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `id_unique`
(
  `id` ASC
) VISIBLE,
  UNIQUE INDEX `license id_unique`
(
  `license` ASC
) VISIBLE,
  UNIQUE INDEX `user_id_UNIQUE`
(
  `user_id` ASC
) VISIBLE,
  CONSTRAINT `organization_user_id`
  FOREIGN KEY
(
  `user_id`
)
  REFERENCES `user-management`.`user`
(
  `id`
))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `user-management`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`user_role`;

CREATE TABLE IF NOT EXISTS `user-management`.`user_role`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT
(
  64
) NOT NULL,
  `role` TINYINT
(
  4
) NOT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `id_UNIQUE`
(
  `id` ASC
) VISIBLE,
  INDEX `user_id_idx`
(
  `user_id` ASC
) VISIBLE,
  CONSTRAINT `role_user_id`
  FOREIGN KEY
(
  `user_id`
)
  REFERENCES `user-management`.`user`
(
  `id`
))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `user-management`.`reset_password_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user-management`.`reset_password_token`;

CREATE TABLE IF NOT EXISTS `user-management`.`reset_password_token`
(
  `id` BIGINT
(
  64
) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT
(
  64
) NOT NULL,
  `token` VARCHAR
(
  36
) NOT NULL,
  `expire_timestamp` TIMESTAMP
(
  6
) NOT NULL,
  PRIMARY KEY
(
  `id`
),
  UNIQUE INDEX `id_UNIQUE`
(
  `id` ASC
) VISIBLE,
  UNIQUE INDEX `token_UNIQUE`
(
  `token` ASC
) VISIBLE,
  INDEX `reset_user_id_idx`
(
  `user_id` ASC
) VISIBLE,
  CONSTRAINT `reset_user_id`
  FOREIGN KEY
(
  `user_id`
)
  REFERENCES `user-management`.`user`
(
  `id`
)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET
SQL_MODE
=
@
OLD_SQL_MODE;
SET
FOREIGN_KEY_CHECKS
=
@
OLD_FOREIGN_KEY_CHECKS;
SET
UNIQUE_CHECKS
=
@
OLD_UNIQUE_CHECKS;
