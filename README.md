创建于3月4日

使用了h2数据库，集成了Flyway插件，可以进行数据库的迁移整合。

已完成Github的OAuth认证登录功能。

## 一些工具的参考文档

[Flyway](https://flywaydb.org/getstarted/firststeps/maven)

[OkHttp](https://square.github.io/okhttp/)

[lombok](https://projectlombok.org/)
```bash
# flyway插件运行
mvn flyway:migrate
# mybatis generator运行
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

## 2020.03.09工作

1. 首页增加问题列表的展示，打开MyBatis对驼峰与大写字母的转换
2. 修复publish界面的textarea框的回显问题
3. 引入热部署工具devtools
4. 完成基本的分页功能

## 2020.03.10工作

1. 增加“我发布的”页面，展示“我的提问”、“我的回复”页
2. 我的提问页面增加分页
3. 初步展示问题详情页，增加修改问题功能
3. 增加退出登录功能
4. 完善重复登录会创建多个同名用户问题，保证只更新token，不增加新用户
5. 引入MyBatis Generator可以省去自己写sql语句，使用Criteria封装查询条件

## 2020.03.11

1. 增加自定义异常类CustomizeException，和全局异常处理@ControllerAdvice，error.html模板