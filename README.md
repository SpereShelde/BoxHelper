# BoxHelper

## 使用; How to use

克隆仓库; Clone this repository:

`git clone https://github.com/SpereShelde/BoxHelper.git && cd BoxHelper`

安装环境，仅使用一次; Build environment for the first time: `bash java.sh`

编辑配置; Edit confiure file：`vi config.json` [帮助Help](https://github.com/SpereShelde/BoxHelper/wiki/%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E;-What-is-configures)

添加Cookie文件; Import cookie file：

在cookies目录下，使用json格式保存您的站点cookie，命名为`站点域名`.json

Create a file under 'cookies', names 'WEBSITE-DOMAIN'.json, to save cookie.

可以添加多个Cookie文件, You can add several cookie files. [帮助Help](https://github.com/SpereShelde/BoxHelper/wiki/%E4%BF%9D%E5%AD%98Cookie;-How-to-save-cookies)

开启后台; Create a background bash：`screen -R BoxHelper`

运行BoxHelper; Run BoxHelper：`java -jar BoxHelper.jar`

Ctrl + a + d 退出screen后台; Type Ctrl + a + d to exit;

---

升级(替换BoxHelper.jar); Upgrade(Download new BoxHelper.jar)：

`wget 'https://github.com/SpereShelde/BoxHelper/blob/master/BoxHelper.jar?raw=true' -O BoxHelper.jar`

## 目前完成度; Status

- Support M-Team, hdcmct.org, chdbits.co, hdchina.org, pt.btschool.net, open.cd and more unknown sites.

- NOT support totheglory.im

- Support Free, 2xFree

- Support Deluge, rTorrent, qBittorrent, Transmission

- 可选择监听页面; You can choose pages to listen

- 可按大小筛选种子; You can shift torrents by size

## 注意事项; Watch this！ 

- BoxHelper 需要你提供Cookie，并会获取你的passkey，但是 BoxHelper 不会上传他们

- BoxHelper needs your cookie and will acquire your passkey, but BoxHelper will not upload them.

- 账号有限，无法测试更多站点。如果确定 BoxHelper 支持 或 不支持 你所使用的站点，请告诉我，谢谢

- We cannot test all sites. Please help us to test and tell me the result, thank you!

- 反馈问题或希望适配其他站点，请发Issue或联系我: `spereshelde#gmail.com`

- To feedback bugs or want us to test more sites, just open an issue or mail me at `spereshelde#gmail.com`

