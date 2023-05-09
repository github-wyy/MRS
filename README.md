# MRS

##准备工作

进入目录:```D:\service\RabbitMQ_Server\rabbitmq_server-3.7.14```

创建账号:
```rabbitmqctl add_user admin 1234```

设置用户角色:
```rabbitmqctl set_user_tags admin administrator```

设置用户权限:
```rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"```

新用户admin具有/ 这个virtualhost中所有资源的配置、写、读权限当前用户和角色，可以查看:
```rabbitmqctl list_users```

之后登录```http://localhost:15672```，添加/admin虚拟主机，为admin用户设置Virtual Hosts

---
##基本命令

查看当前状态:```rabbitmqctl status```	

开启Web插件:```rabbitmq-plugins enable rabbitmq_management```

启动服务:```rabbitmq-server start```

停止服务:```rabbitmq-server stop```	

重启服务:```rabbitmq-server restart```

---
##演示
100002和100003演示推荐
100044没有评价，首页返回热点视频
