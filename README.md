# 监控人员活动信息APP

## 功能要求
- 1、基于网络方式实现，服务器可自行搭建。
- 2、用户可以通过输入密码或手势密码登录APP。
- 3、可以管理关注人员信息（人员姓名、性别、身份证号码、头像、手机号码、QQ号码、微信号以及Email地址等）。
- 4、记录和展示某个关注人员的活动日志情况（基于时间轴，展示活动日志，活动日志包括：文字，图片，视频和GPS）。
- 5、展示关注人员的活动轨迹（基于百度地图，展示人员的活动轨迹和主要活动场所）。
- 6、展示人员的网络社交活动图（电话联系、QQ联系及微信联系等）。选作！！！


## 详细需求
- 1.输入密码、手势密码登录APP(注册好像没要求)，首次打开需要设置密码或者手势密码。
- 2.管理关注人员信息(添加、删除、更改、查看被关注人员及其信息)
- 3.记录、展示某个关注人员的信息(使用时间轴，展示其活动日志，文字、图片、视频、移动轨迹
  (GPS信息，基于百度地图，主要活动场所则用GPS停留轨迹判断停留时间通过地图API找出))


## APP页面

- 密码相关：
	- 1.设置密码界面(字符密码/手势密码)
	- 2.解锁页面(手动输入密码/手势密码)
- 人员相关：
	- 3.展示已经关注的人员(首页，有添加关注人员的按钮、删除已关注人员的按钮)
	- 4.添加关注人员信息界面，添加完成后返回页面3，修改被关注人员的信息。
	- 5.添加被关注人员的活动信息(文字、图片、视频、GPS信息)
	- 6.展示人员信息(个人信息、添加活动日志按钮、最近活动日志、展示活动轨迹按钮
	- 7.展示关注人员的活动轨迹(百度地图SDK，用记录的多个GPS点作为连接)，
	  主要活动场所(根据GPS停留频率确定，地图上标注出来)
	- 8(选做).用HTML(echarts or 其他的)展示人员网络社交活动图。

## leancloud 数据库
- **user**(用户表)
    - **objectId**(String, unique) 系统自动创建
    - **acl**(ACL) 一个JSON对象，对象权限控制，系统自动创建
    - **createdAt**(Date) 对象被创建的UTC时间，系统自动创建
    - **updateAt**(Date) 对象最后一次被修改的时间，系统自动创建
    - **usermac**(String, unique) 用户手机的MAC地址
    - **handpassword**(String) 手势密码
    - **password**(String) 字符面

- **followers**(关注人员表)
    - **objectId**(String, unique) 系统自动创建
    - **acl**(ACL) 一个JSON对象，对象权限控制，系统自动创建
    - **createdAt**(Date) 对象被创建的UTC时间，系统自动创建
    - **updateAt**(Date) 对象最后一次被修改的时间，系统自动创建
    - **usermac**(String, unique) 用户手机的MAC地址
    - **name**(String) 姓名
    - **gender**(String: *man, women*) 性别
    - **id_card**(String) 身份证号码
    - **head_img**(String,(file_objectId)) 头像，存头像文件的id
    - **phone**(String) 手机号
    - **QQ**(String) QQ号
    - **wechat**(String) 微信号
    - **email**(String) 邮箱地址

- **records**(用户活动记录表)
    - **objectId**(String, unique) 系统自动创建
    - **acl**(ACL) 一个JSON对象，对象权限控制，系统自动创建
    - **createdAt**(Date) 对象被创建的UTC时间，系统自动创建
    - **updateAt**(Date) 对象最后一次被修改的时间，系统自动创建
    - **follower_id**(String) 被关注用户的objectId
    - **ctime**(Int) 时间戳，单位秒
    - **content**(String) 活动内容记录
    - **pic**(String)活动图片文件的id
    - **video**(String)活动视频文件的id
    - **longitude**(Float) 记录的经度
    - **latitude**(Float) 记录的纬度

## 流程
- **注册登录流程**
    1. 用户打开 APP，让他输入手势密码/字符密码，界面上提供注册按钮
    2. 注册的用户就获取到了手势密码/字符密码之后，把用户手机的MAC地址作为用户名(对应数据库的usermac字段)进行注册
    3. 登录的用户输入完毕手势密码/字符密码之后就去获取这个用户，获取不到则密码错误，获取到了则进入首页

- **用户关注人员增删改查**
    1. 首页显示被关注人员。点击被关注人员可以进入被关注人员活动情况页面。(长按)提供删除/修改关注人员的功能。增加关注人员的功能
    2. 增加/修改被关注人员信息，增加、修改成功返回首页

- **添加被关注人员的活动信息**
    1. 增加活动信息，时间为时间戳。
    2. 活动内容：(文字(必须有))、照片(可有可无)、视频(可有可无)、GPS信息(必须有，手动填入或获取手机的GPS信息))
    3. 提交活动，根据获取到的关注人员的objectId进行添加。
    
- **展示被关注人员的信息**
    1. 基于时间轴进行显示(类似于QQ分组(不折叠)，顶部左边显示时间，左边显示照片、视频，右边显示内容...)。
    2. 在顶部加入添加记录的功能，点击跳转到添加被关注人员活动信息的界面(传递关注人员objectId)，添加成功后返回。
    3. 添加显示被关注人员活动轨迹按钮，跳转到被关注人员活动轨迹页面(传递关注人员objectId)。

- **展示活动轨迹**
    1. 使用WebView + 百度SDK
    2. 根据获取到的关注人员的objectId获取到用户的活动GPS列表，提取出来，作为list传递给百度SDK并显示。