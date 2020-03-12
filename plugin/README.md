# LightHttp模板创建

​	LightHttp通过插件的方式提供了一种全新的请求处理过程，使用模板可以为您节省开发时间，忽略编写Java代码的时间。

### 如何安装插件？

​	在使用这个功能之前，请确保您已经安装过我们的LightHttpGeneration插件，如果您没有这个插件，您可以<a href="https://github.com/YuQianhao/LightHttp/raw/master/plugin/LightHttpGeneration.jar">点击此处下载</a>，如果您不知道如何安装，您可以<a href="<https://jingyan.baidu.com/article/a3aad71a36856ab1fa009673.html>">点击此处了解</a>。

​	如果您已经安装成功，您可以在窗口左侧的Project视图中右键，可以看到在最底部有一个“LightHttpAutoGeneration”按钮，如图。

![](https://github.com/YuQianhao/LightHttp/blob/master/plugin_0.jpg)

当出现如图所示的按钮的时候，代表我们的插件安装成功。

### 如何使用？

1、在名为java的目录中创建一个json配置文件，路径可能是这个样子：

/home/yuqianhao/AndroidProject/您项目的名称/app/main/src/java/LightHttp.json

创建配置文件的时候要注意的就是**这个配置文件只能出现在名为Java的目录下**，如果您配置文件创建的正确，可能会看到如下的结构：

![](https://github.com/YuQianhao/LightHttp/blob/master/plugin_1.jpg)

这个时候，代表配置文件创建成功，可以看到，这个配置文件直接在Java的目录下，并且与com文件夹同级。

2、配置文件分为3个部分：

* 包名：插件生成的代码会放到这个包名指定的目录下
* 默认创建请求的参数
* 请求体数组

（1）、包名

```json
{
    "packageName":"com.yuqianhao.request"
}
```

通过配置名为packageName的字段来配置这个包名，当插件为我们生成类文件的时候，它会寻找到这个包名并为我们创建，如果这个包名不存在它会去创建，我们也可以理解为，当插件生成类的时候，类的全名为com.yuqianhao.request.LightHttpRequest。

值得注意的是，这个字段只能出现一次，而且必须配置。

（2）、请求体数组（我们将在稍后展示“默认创建请求的参数”）

```json
{
    "packageName":"com.yuqianhao.request",
    "content";[
    
    ]
}
```

请求体，即我们每一次的请求内容，其中包括了请求地址，方法名称，请求方式，传送数据的格式，以及自动序列化的类和注释文本。

这一部分内容使用JsonArray的形式创建，意味着可以创建多个不同的请求方法，每一个请求方法又是一个JsonObject，例如：

```json
{
    "packageName":"com.yuqianhao.request",
    "content":[
        {
    		"name":"getUserToken",
   			"url":"api/getUserToken",
    		"method":"post",
    		"type":"form",
    		"requestType":"async",
    		"header":[],
			"params":[],
			"defaultParams":[],
            "jsonParams":[],
            "responseType":"",
            "note":""
    	}
    ]
}
```

我们可以看到，创建一个请求体要设置的参数还是很多的，但其实我们只是在这里展示了全部的参数，当真正创建请求的时候，用不到这么多的参数。

* name：请求方法的名称，插件会按照这个字段为我们创建好方法的名称，例如：

```json
{
    "name":"getUserToken"
}
```

生成的Java代码（格式已处理，实际插件生成的格式可能有变化。）

```java
public class LightHttpRequest{
    public static final void getUserToken(){
    }
}
```

* url：这个接口的请求地址，插件会按照这个字段为我们设置好请求的地址，这里插件采用了使用注解的方式为我们创建这个请求参数，例如：

```json
{
    "name":"getUserToken",
    "url":"api/getUserToken"
}
```

生成的Java代码（格式已处理，实际插件生成的格式可能有变化。）

```java
public class LightHttpRequest{
    
    @PostRequest("api/getUserToken")
    private static final class getUserToken{}
    
    public static final void getUserToken(){
        LightHttp.loadRequest(loadData.class);
    }
}
```

* method：请求的方法，这里只能取指定的值，LightHttp支持get、post、put、delete这四种方式，插件会根据这个字段来确定这个请求到底是get还是post还是其他的值，例如：

```json
{
    "name":"getUserToken",
    "url":"api/getUserToken",
    "method":"get"
}
```

生成的Java代码（格式已处理，实际插件生成的格式可能有变化。）

```java
public class LightHttpRequest{
    
    //这里发生了变化，因为method被设置为get
    @GetRequest("api/getUserToken")
    private static final class getUserToken{}
    
    public static final void getUserToken(){
        LightHttp.loadRequest(loadData.class);
    }
}
```

