# mainapp
Main app for ashago

# Build Setup
命令行打包
进入springboot工程目录，然后运行
`mvn package`命令

运行jar包, maven默认打包在target目录，进入springboot工程的target目录`cd target`

然后运行`java -jar mainapp-0.0.1-SNAPSHOT.jar`命令来运行程序,换成项目名字。


## 整体架构
参见：ashago-frame.png

### 安全认证方案
##### 登录信息的认证
基于session-cookie认证，session保存在服务端，存储在cookie里。
##### rest-api的认证
基于JWT的方案，secret全局唯一，与用户无关，但是服务端定期更新。
这个没有特别的需求，就简单的反爬就行。
