创建于3月4日

使用了h2数据库，集成了Flyway插件，可以进行数据库的迁移整合。

已完成Github的OAuth认证登录功能。

## 一些工具的参考文档

[Flyway](https://flywaydb.org/getstarted/firststeps/maven)

[OkHttp](https://square.github.io/okhttp/)

[lombok](https://projectlombok.org/)

[EditorMD](http://editor.md.ipandao.com/)

[MyBatis Generator](http://mybatis.org/generator/)


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
2. 增加异常处理Controller
3. 增加评论表，后台增加一个接收json格式post请求的controller

## 2020.03.12

1. 增强异常处理，可以根据请求头分别饭返回json或html响应
2. 实现评论的后台处理逻辑
3. 增加前台评论的输入框，完成ajax请求，评论后关闭编辑区

## 2020.03.13

1. 展示问题的所有评论
2. 学习了Lambda与streamAPI
3. 展示评论的子评论
4. 完成子评论列表的后台，前台动态展示未完成

## 2020.03.14

1. 完成子评论的异步加载，刷新页面

## 2020.03.22

1. 完成基本的通知功能展示与浏览
2. 修复多用户登录
3. 用户通知的同步，每次刷新页面都可以看到通知数

## 2020.03.23

1. 日志
2. 富文本编辑，使用MySQL数据库