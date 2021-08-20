# 	Docker

## 一、内网无法获取镜像处理方案

很多时候会碰到公司内网无法连接外网的情况，这样就不能从docker hub上直接pull镜像了
此时，

1、我们可以先用能连上docker 官方镜像的机子下载镜像

```bash
docker pull xxx镜像
```

2、然后保存为tar文件

```bash
docker save -o xxx.tar xxx镜像
```

3、将xxx.tar拷贝到内网机子上

4、在内网机子上导入

```bash
docker load -i xxx.tar
```