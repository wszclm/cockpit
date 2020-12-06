## 菜单项数据加载成功之后，在前端有几个可以存放的地方：

```java
1. sessionStorage
2. localStorage
3. vuex**
```
####  前端工程安装依赖
-- 安装失败，需要更改镜像为淘宝镜像
num install

####  前端工程运行
npm run serve
-- 转发配置 vue.config.js  这个里面配置请求后端地址信息。

-- 菜单查询服务
```SQL
SELECT DISTINCT
	m1.*, m2.`id` AS id2,
	m2.`component` AS component2,
	m2.`enabled` AS enabled2,
	m2.`iconCls` AS iconCls2,
	m2.`keepAlive` AS keepAlive2,
	m2.`name` AS name2,
	m2.`parentId` AS parentId2,
	m2.`requireAuth` AS requireAuth2,
	m2.`path` AS path2
FROM
	menu m1,
	menu m2,
	user_role urr,
	menu_role mr
WHERE
	m1.`id` = m2.`parentId`
AND urr.`urid` = 3 and urr.`rid`=mr.`rid` and mr.`mid`=m2.`id` and m2.`enabled`=true order by m1.`id`,m2.`id`

## 开发子功能需要配置的地方
1，需要在表menu 表中 添加sql  prentId2 为父菜单（一级菜单）id
2，需要在menu.js 配置子功能组件的位置，根据sql返回一级菜单和二级菜单的对应关系进行展示。
3，需要配置 menu_role 表中角色和菜单的关联关系，菜单配置可根据以上sql查询 一级菜单和二级菜单的展示内容。

```
