# BoxHelper

## 使用

下载并安装(自动安装JDK1.8)：

`wget -qO- https://raw.githubusercontent.com/SpereShelde/BoxHelper/master/BoxHelper.sh | bash`

screen开启后台：

`screen -R BoxHelper`

编辑配置：

`vi BoxHelper.conf`

运行BoxHelper：

`java -jar BoxHelper.jar`

Ctrl + a + d 退出screen后台

## 目标

自动监听并下载指定 PT 站内指定页面的指定类型的种子

## 目前完成度

- 支持 tp.m-team.cc、chdbits.co

- 支持 Free 2xFree 种

- 可选择监听页面

- 可按大小筛选种子

## To Do List

- ~~一键安装脚本~~

- 主流站点适配

- 磁盘空间预警提醒

## 说明
BoxHelper 只完成获取并下载Free种，所以需要配合 Deluge 或 rTorrent 等软件的 watch directory

BoxHelper 需要你提供账号和密码，但 BoxHelper 不会上传它们
