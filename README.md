部署环境列表
mysql version:8.0.40
redis version:3.0.504
jdk   version:1.8.0_112

配置文件
redis:
  url: redis://localhost:6379
spring:
  application:
    name: RedEnvelope

  datasource:
    url: jdbc:mysql://localhost:3306/hb?useSSL=false&serverTimezone=UTC
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver

部署步骤
1.安装mysq,初始化db.sql
2.安装redis
3.修改application.yml
4.启动应用



接口列表

生成红包接口(测试使用):http://localhost:8080/api/generate
返回值:{"TOTAL_COUNT":100,"TOTAL_AMOUNT":500,"id":3,"bindUserIds":null,"total_AMOUNT":500,"total_COUNT":100}


抢红包家口:http://localhost:8080/api/get?hbid=4&uid=62
返回值:{"parentId":3,"id":1,"amount":5,"userId":62}


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




