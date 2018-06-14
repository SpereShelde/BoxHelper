# BoxHelper

## 使用

安装JDK1.8：

`wget -qO- https://raw.githubusercontent.com/SpereShelde/BoxHelper/master/java.sh | bash`

下载脚本：

`wget --no-check-certificate 'https://github.com/SpereShelde/BoxHelper/releases/download/v1.1/BoxHelper.tar'`

解压脚本：`tar -zxf BoxHelper.tar && cd BoxHelper`

编辑配置：`vi config.json`

保存Cookie：在 cookies 目录下，使用json格式保存您的站点cookie，命名为`站点域名`.json，[查看 Wiki 详解](https://github.com/SpereShelde/BoxHelper/wiki/%E5%A6%82%E4%BD%95%E4%BF%9D%E5%AD%98Cookie)

screen开启后台：`screen -R BoxHelper`

运行BoxHelper：`java -jar BoxHelper.jar`

Ctrl + a + d 退出screen后台

## 目标

自动监听并下载指定 PT 站内指定页面的指定类型的种子

## 目前完成度

- 支持 M-Team, hdcmct.org, chdbits.co, hdchina.org, pt.btschool.net 等

- 支持 Free、 2xFree 种

- 可选择监听页面

- 可按大小筛选种子

## To Do List

- 更高效获取 Passkey

- 适配更多站点

- 磁盘空间预警提醒

## 说明

- BoxHelper 需要配合 Deluge、 rTorrent 等软件的 watch directory

- BoxHelper 需要你提供Cookie，并会获取你的passkey，但是 BoxHelper 不会上传他们

- 账号有限，无法测试更多站点。如果确定 BoxHelper 支持 或 不支持 你所使用的站点，请告诉我，谢谢

- 希望适配其他站点，请将站点Cookie发送到我的邮箱 `spereshelde#gmail.com`

