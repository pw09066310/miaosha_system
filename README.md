# 1 先建表
CREATE TABLE `t_global_param` (
  `id` bigint(20) NOT NULL,
  `param` decimal(10,0) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 2 安装redis

# 3 本文技术栈

- mybatis-plus
- springboot 2.2.6
- redis


# 4 基于CAS的形式设计秒杀系统， 项目目前牛刀小试  欢迎大神指导
