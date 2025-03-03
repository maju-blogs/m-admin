### 使用autoTask对手机实现任务管理的后台系统

#### 功能简介：

1. 客户端管理，通过mqtt协议对手机进行管理
2. 任务管理，通过手机上的autoTask软件上传任务，可以在后台直接执行任务，也可以将上传的任务添加到我的任务，下载给其他手机，通过common订阅地址对所有手机进行群控
3. 获取手机上的文本，通过autoTask上传文本
4. 脚本管理，支持shell和python脚本
5. 微信公众号自动回复机器人，通过配置调用脚本实现
6. 微信收款码管理，可配置百度ocr自动识别，美化收款码
7. 个人微信支付，收款成功自动回调
8. 订单管理

#### 项目部署

1. maven构建jar包部署到服务器

2. 创建m-web数据库，并导入执行m-web.sql

3. 修改配置文件

   ```
   #服务端口
   server.port=9000
   #日志等级
   logging.level.root.org.m=debug
   #数据库连接配置
   spring.datasource.url=jdbc:mysql://192.168.1.108:3306/m-web?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
   spring.datasource.username=root
   spring.datasource.password=root
   #前端代码静态目录
   spring.web.resources.static-locations=static
   spring.jackson.time-zone=Asia/Shanghai
   #mqtt服务端配置
   mqtt.server.username=mqtt
   mqtt.server.password=2e51bf05564158d7eff6d5f0b9fcbb5f
   #mqtt客户端配置
   mqtt.client.username=mqtt
   mqtt.client.password=2e51bf05564158d7eff6d5f0b9fcbb5f
   
   ```

   需要注意mqtt用户名和密码配置后，在autoTask上使用该用户名和密码进行连接，连接后才可以使用，端口为8082，其他配置可以在application.yml中查看

4. 构建前端代码，并上传到前端代码静态目录

5. 手机上安装auto-task-mqtt.apk 并配置mqtt进行连接，启动服务，推荐使用shizuku授权

#### 支付实现

1. org.m.pay.service.IPayServer#pay 开始调用支付，通过addPayTask方法加入任务开始获取手机上的支付信息，手机需要关注微信收款助手，成功后会回调org.m.pay.service.impl.PayServerImpl#onCallBack方法

2. 支付成功匹配规则参考org.m.pay.handle.PayHandleMsgImpl#mather 方法，会匹配支付时间以及备注

3. 获取使用二维码使用org.m.pay.service.IPayQrConfigService#getOneUnLockQr方法，获取对应支付方式的最近没使用的二维码，防止时间相近，直接支付成功

4. 理论上可以支持生成备注、有收款提示任何支付方式

5. 目前只实现了微信支付

   

#### 使用技术

1. 框架使用springboot3.2.1
2. [登录鉴权 sa-token](https://sa-token.cc/doc.html#/)
3. 数据库存储 [mybatis-flex](https://mybatis-flex.com/)

#### 其他仓库地址

1. 前端代码 [m-admin-vue](https://github.com/maju-blogs/m-admin-vue)
2. auto-task-mqtt [auto-task-mqtt](https://github.com/maju-blogs/AutoTask/)

#### 项目优点

1. 个人免签约支付，无需商家营业执照
2. 支持拓展其他方式支付，目前只实现了微信支付，其它支付只有配置任务即可，基本上无需修改代码
3. 手机端无需root
4. 手机端只有在有支付任务时才会开始执行任务，功耗较低
5. 支付回调及时，基本上在1s左右

#### 特别说明
1、项目自带的任务可能不支持最新的微信，可以自行在autoTask调试修改,主要是匹配文本和上传文本
2、autoTask要保证后台自启动，推荐使用shiziku进行授权
3、使用openjdk22
#### 项目截图

![客户端管理](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191257127.png)

![任务管理](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191320744.png)

![二维码管理](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191357242.png)

![支付方式管理](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191423672.png)

![订单管理](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191433005.png)

![支付体验](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191454294.png)

![支付成功](https://github.com/maju-blogs/pulbic-static-file/blob/master/m-admin/image-20240616191532061.png)



#### 支付体验

[支付体验](https://ydfm.cc/madmin/order.html)
