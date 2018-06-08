# BoxHelper
## 目标：

自动监听并下载指定 PT 站内指定页面的指定类型的种子

## 目前完成度：

- 仅支持M-Team馒头

- 仅支持Free种

- 可选择监听页面

- 可按大小筛选种子

## 环境要求：
需要JDK 1.8及以上

## 使用说明：
BoxHelper 只完成获取并下载Free种，所以需要配合Deluge或rTorrent的watch directory

BoxHelper 需要你提供账号和密码，但 BoxHelper 不会上传它们

## 使用方法：
screen开启后台：
`screen -R mt`

编辑配置：
`vi spider.mt.txt`

运行BoxHelper：
`java -jar mt.jar`
