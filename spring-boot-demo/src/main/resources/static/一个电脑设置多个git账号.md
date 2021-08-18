

## **思 路**

同时管理多个SSH key。

## **解决方案**

## **生成多个SSH key**

这里使用`one`、`two`两个账户进行举例。

**注意：** 在生成多个`SSH key`的时候一定要在`~/.ssh`目录下进行，否则生成的`SSH key`不会在`~/.ssh`目录下，所以以下有操作都是在`~/.ssh`目录下进行的。在生成之前尽量删除此目录下的所有文件再进行，以免出现不必要的问题。

```text
ssh-keygen -t rsa -C "one@email.com"

ssh-keygen -t rsa -C "two@email.com"
```

复制代码再输入命令行的时候在第一次提示`Enter file in which to save the key`的时候对`ssh`文件进行重命名（`idrsaone`和`idrsatwo`），这样就会生成如下目录中的四个文件。



即两份包含私钥和公钥的`4`个文件。

------

## **获取密钥**

```text
cat ~/.ssh/id_rsa_one.pub

cat ~/.ssh/id_rsa_two.pub
```

其中`idrsaone.pub`和`idrsatwo.pub`就是上面对`ssh`文件重命名的文件名。

有了这个密钥，你就可以将其添加到你所需要用的平台上去。

## **创建config文件**

在`~/.ssh`目录下创建一个`config`文件

```text
touch config
```

这样就会在`~/.ssh`目录下生成一个空的`config`文件，然后我们在文件中添加以下内容：

```text
# git server one
Host one.aliyun.com #别名
Hostname code.aliyun.com #真实域名
PreferredAuthentications publickey
IdentityFile ~/.ssh/id_rsa_one #ssh 文件路径
User one

#git server two
Host two.aliyun.com
Hostname code.aliyun.com
PreferredAuthentications publickey
IdentityFile ~/.ssh/id_rsa_two
User two
```

## **远程测试**

```text
ssh –T one.aliyun.com

ssh –T two.aliyun.com
```

## **使 用**

比如`clone`到本地

原来的写法：

```text
git clone git@code.aliyun.com:项目路径.git
```

现在的写法：

```text
git clone git@one.github.com:项目路径.git

git clone git@two.github.com:项目路径.git
```

给仓库设置局部用户名和邮箱

```text
git config user.name "one_name"; git config user.email "one_email"
git config user.name "two_name"; git config user.email "two_email"
```