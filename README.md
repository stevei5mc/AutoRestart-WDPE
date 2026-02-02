# **AutoRestart-WDPE 自动重启插件**
[![GitHub License](https://img.shields.io/github/license/stevei5mc/AutoRestart-WDPE?style=plastic)](LICENSE)
[![GitHub Release](https://img.shields.io/github/v/release/stevei5mc/AutoRestart-WDPE?style=plastic&color=drak%20green)](https://github.com/stevei5mc/AutoRestart-WDPE/releases)
![GitHub Repo stars](https://img.shields.io/github/stars/stevei5mc/AutoRestart-WDPE?style=plastic)
![GitHub forks](https://img.shields.io/github/forks/stevei5mc/AutoRestart-WDPE?style=plastic)
![GitHub issues](https://img.shields.io/github/issues/stevei5mc/AutoRestart-WDPE?style=plastic&color=linkGreen)
![GitHub pull requests](https://img.shields.io/github/issues-pr/stevei5mc/AutoRestart-WDPE?style=plastic)
## **插件介绍**
### **功能介绍**
- [x] **取消自动重启任务**
- [x] **暂停自动重启任务**
  #### **支持的重启任务类型**
  - [x] **自动重启**
    - **周期模式： 每隔一段时间就会进行一次重启**
    - **定时模式： 只会在设定的时间进行重启**
  - [x] **手动重启（手动重启的时间为配置文件中的提示时间）**
  - [x] **服务器无人时自动重启**

### **命令与权限**
- **下列命令权限是给管理员使用的**

|命令|权限节点|权限介绍|
|:-:|:-:|:-:|
|/autorestart|autorestart.admin|主命令|
|/autorestart reload|autorestart.admin.reload|重载配置文件|
|/autorestart cancel|autorestart.admin.cancel|取消重启任务|
|/autorestart pause|autorestart.admin.pause|暂停/继续运行重启任务|
|/autorestart restart [任务类型]|autorestart.admin.restart|重启服务器|

#### **命令参数讲解**
- **[ ]  内的参数必填**

|任务类型|任务描述|
|:-:|:-:|
|manual|手动重启|
|no-players|无人时重启|=

### **配置文件介绍**
<details>
<summary>config.yml</summary>

```yml
# 配置文件版本（勿动!!!）
version: 1
# 重启时间
restart_time:
  # 时间模式
  # true 使用 cron_time 配置的时间
  # false 使用 cycle_time 配置的时间
  time_mode: true
  # 周期模式 （单位：分钟）
  cycle_time: 720
  # 定时模式
  # 格式：时&分 或 时&分&星期 星期范围（1-7）
  cron_time:
    - "3&30"
# 重启前提示时间(单位：秒)
restart_tip_time: 30
# 是否在重启前把玩家踢出
kick_player: true
# 一些显示的设置
show:
  # 显示 title 和 subtitle
  title: true
  # 底部显示(在物品栏上方)
  tip: true
# 重启前执行的命令
runCommand: true
commands:
  global:
    # 全局性的命令，需要在关服前执行的命令（注意，如果需要为每一名玩家都执行命令的请在 player 的命令配置中填写）
    - "wdlist"
  player:
    # 为每一名玩家执行的命令
    # &con为控制台执行 @p代表玩家名 ( 只有为每一名玩家都执行的命令 @p 与 &con 才会生效 )
    - "wdhelp"
    - "wdinfo&con"
# 调试模式
debug: false
# 消息前缀
message_prefix: "§l§bAutoRestart §r§7>> "
broadcast_message:
  # 定时提醒服务器执行的剩余时间
  reminder_time:
    ## 是否启用
    enable: true
    ## 广播周期 （单位：分钟）
    cycle: 30
```

</details>

## **使用方法**
|步骤|说明|
|:-:|:-|
|1|准备好相关的启动脚本并放置在服务端的根目录下 **（一般情况下启动脚本是放置在服务端的根目录下）** 并调整好启动脚本的配置 **(如果你拥有并使用相关的启动脚本则无视这一步骤)**|
|2|**将本插件放进`plugins`文件夹后启动服务器，如果不需要调整可以使用默认的配置**|

### **注意事项**
1. **自动重启需要脚本的配合才能实现自动重启，相关的自动重启脚本会在 [Releases](https://github.com/stevei5mc/AutoRestart/releases) 界面随着插件发布一并给出相关的脚本，如果你已经拥有了相关脚本则可以忽略这一点。**
    - **另外每种系统的脚本都有两种，建议根据实际需求选用**
    - **Windows系统用`.bat`后缀的脚本**
    - **Linux系统请用`.sh`后缀的脚本**
2. **本插件会受 `TPS` 的影响可能会导致跟时间相关的数据计算不准确！**
3. **在使用时请不要随意改动语言文件或配置文件的版本号，否则所带来的后果一切自负！！！**

### **自动重启-定时模式使用说明**
- **时间格式： 时&分 或者 时&分&星期**
- **注：星期的范围填写 1至7 代表 星期一代表星期日，如果不填写指定的星期则每天都会在指定的时间执行重启，如果填写了只会在指定的星期执行重启操作，但每一行只能填写一个星期**
- **例如： 3&30&5 就会在星期五的3点30分执行重启（24小时制）**

### **脚本使用说明**
- **此内容使用于配套的启动脚本，可配置项已在下方列出**
- **如果你没有相关知识，请不要修改可配置项以外的地方**
- **可配置项是在`=`后面的，例如`jvm_ram_xms="512M"`的`512M`就为可配置项, 注：`""`可加可不加，如果无法运行可尝试加上`""`**

<details>
<summary>Windows</summary>

```bat
::设置用于启动的java版本 (默认填 java，当然你也可以填java的安装路径)
set java_version=java
::服务器名提示
set server_name=test
::设置最小内存
set ram_Xms=512m
::设置最大内存
set ram_Xmx=1024m
::设置开服核心名(开服核心名不用加.jar的后缀名)
set server_jar_name=server
::设置重启等待时间
set restart_time=20
```
</details>

<details>
<summary>Linux</summary>

```sh
ReStart_Time=5s  #等待多少秒后重启要加时间单位
jvm_ram_xms="512M"  #设置最小内存
jvm_ram_xmx="1024M"  #设置最大内存
jar_name="server.jar"  #开服核心的名字记得加.jar后缀名
server_name="testmc"  #服务名(方便维护用的)，比如说是生存服就将testmc改为生存服
```
</details>