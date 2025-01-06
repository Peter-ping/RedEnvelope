CREATE TABLE `red_envelope`
(
    `id` INT(10) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `sub_red_envelope`
(
    `id`        INT(10) NOT NULL,
    `parent_id` INT(10) NOT NULL,
    `user_id`   INT(10) NOT NULL DEFAULT '0',
    `amount`    INT(10) NOT NULL,
    PRIMARY KEY (`id`, `parent_id`) USING BTREE
) COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
