# mainapp
Main app for ashago

## 整体架构
参见：ashago-frame.png

### 安全认证方案
##### 登录信息的认证
基于session-cookie认证，session保存在服务端，存储在cookie里。
##### rest-api的认证
基于JWT的方案，secret全局唯一，与用户无关，但是服务端定期更新。
这个没有特别的需求，就简单的反爬就行。
