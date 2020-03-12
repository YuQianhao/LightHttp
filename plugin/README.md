# LightHttp模板创建

​	LightHttp通过插件的方式提供了一种全新的请求处理过程，使用模板可以为您节省开发时间，忽略编写Java代码的时间。

### 如何安装插件？

​	在使用这个功能之前，请确保您已经安装过我们的LightHttpGeneration插件，如果您没有这个插件，您可以<a href="https://github.com/YuQianhao/LightHttp/raw/master/plugin/LightHttpGeneration.jar">点击此处下载</a>，如果您不知道如何安装，您可以<a href="https://jingyan.baidu.com/article/a3aad71a36856ab1fa009673.html">点击此处了解</a>。

​	如果您已经安装成功，您可以在窗口左侧的Project视图中右键，可以看到在最底部有一个“LightHttpAutoGeneration”按钮，如图。

![](https://github.com/YuQianhao/LightHttp/blob/master/plugin_0.jpg)

当出现如图所示的按钮的时候，代表我们的插件安装成功。

### 如何使用？

1、在名为java的目录中创建一个json配置文件，路径可能是这个样子：

/home/yuqianhao/AndroidProject/您项目的名称/app/main/src/java/LightHttp.json

创建配置文件的时候要注意的就是**这个配置文件只能出现在名为Java的目录下**，如果您配置文件创建的正确，可能会看到如下的结构：

![](https://github.com/YuQianhao/LightHttp/blob/master/plugin_1.jpg)

这个时候，代表配置文件创建成功，可以看到，这个配置文件直接在Java的目录下，并且与com文件夹同级。

当您的配置文件编写完成的时候，选中这个配置文件，然后对它进行右键，选中"LightHttpAutoGeneration"这个按钮，插件会跟配置文件的内容在指定的位置生成类文件。

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
    "content":[
    
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

* type：上传的参数的格式，目前模板只支持form和json这两种格式。
* requestType：请求的方式，只能取以下两个值，async是异步请求，sync是同步请求，如果是异步请求插件会给我们生成用来回调的参数，如果是同步请求插件会为我们生成返回值，例如：

```json
//为了方便展示，代码进行了折叠
public class LightHttpRequest{
    
    //请求方式为async的时候
    public static final void getUserToken(ResponseCallback<User> callback){}
    
    //请求方式为sync的时候
    public static final Response<User> getUserToken(){}
    
}
```

* header：请求的Header是可选的参数，使用数组的方式为我们设置要传入的Header的值和参数名称，例如：

```json
{
    "header":[
        {
            "id":"token",
            "type":"java.lang.String"
        },
        {
            "id":"size",
            "type":"int"
        }
    ]
}
```

那么生成的类会是这样子的（代码进行了折叠）

```java
public class LightHttpRequest{
    
    public static final void getUserToken(
        java.lang.String token,
        int size,
        ResponseCallback<User> callback){}
}
```

header使用JsonObject的方式创建，它拥有连个字段，其中id相当于参数的名称，type是参数的类型，值得注意的是，除了基本数据类型以外，类类型必须使用类全名，因为插件无法知道应该引入哪一个类。

* params：请求参数数组，可选字段，这个字段只有在type是form的时候才会生效，因为form总是以键值对的方式传递数据，所以它使用了和header相同的数组组成方式，例如：

```json
{
    "params":[
        {
            "id":"userId",
            "type":"java.lang.String"
        },
        {
            "id":"name",
            "type":"java.lang.String"
        }
    ]
}
```

那么生成的类会是这样子的（代码进行了折叠）

```java
public class LightHttpRequest{
    
    public static final void getUserToken(
        java.lang.String userId,
        java.lang.String name,
        ResponseCallback<User> callback){}
}
```

* defaultParams：默认的参数，可选字段，这个字段和params字段一样，只能在type为form的时候生效。这个字段可以为配置的接口设置默认的值，例如可能会出现这种借口：

```html
api/getUserData(int userType,String userName);
//userType：0-普通用户，1-会员用户
//userName：用户名
```

这种接口都会有一个特点，某一个字段会传入指定的常量来查询不同的数据，如果我们使用模板的方式生成出来的代码可能会是这样子的：

```java
public class LightHttpRequest{
    
    public static final void getUserToken(
        int userType,
        java.lang.String userName,
        ResponseCallback<User> callback){}
}
```

但是其实我只是想用这个接口查询普通用户而已，如果生成这样的代码每次我们都需要传入一个0，这样调用起来会很繁琐，所以插件提供了defaultParams字段来为我们填充值，例如：

```json
{
    "params":[
        {
            "id":"userName",
            "type":"java.lang.String"
        }
    ],
    "defaultParams":[
        {
            "id":"userType",
            "value":"0"
        }
    ]
}
```

生成的Java代码会是这个样子的：

```java
public class LightHttpRequest{
    
    public static final void getUserToken(
        java.lang.String userName,
        ResponseCallback<User> callback){
        
        Map<String,String> paramsMap=new HashMap<>(2);
        paramsMap.put("userName",userName);
        //我们可以看到，这个默认值已经自动的填充进来了
        paramsMap.put("userType","0");
        
    }
}
```

默认填充了值，我们在调用的时候直接传入userName字段就可以了。

* jsonParams：Json形式的请求参数，这个字段只有在type为json的时候才会生效，例如：

```json
{
    "type":"json",
    "jsonParams":{
        "id":"user",
        "type":"com.yuqianhao.model.User"
    }
}
```

生成的Java代码会是这个样子的：

```java
public class LightHttpRequest{
    
    public static final void getUserToken(
        com.yuqianhao.model.User user,
        ResponseCallback<User> callback){}
}
```

值得注意的是，jsonParams是一个JsonObject而不是JsonArray。

* responseType：接口返回的数据自动序列化的类型，可选参数，当接口返回数据的时候，LightHttp会根据这个字段指定的类型进行反序列化，例如：

```json
{
    "responseType":"java.utils.List<com.yuqianhao.User>"
}
```

生成的Java代码会是这样子的：

```java
public class LightHttpRequest{
    
    //当requestType是async的时候
    public static final void getUserToken(
        ResponseCallback<java.utils.List<com.yuqianhao.User>> callback){}
    
    //当requestType是sync的时候
    public static final Response<java.utils.List<com.yuqianhao.User>> getUserToken(){}
    
}
```

* note：方法的注释，可选参数，这个字段会用来给插件生成的代码进行注释，例如：

```json
{
    "note":"默认的方法"
}
```

生成的Java代码会是这样子的：

```java
public class LightHttpRequest{
    
    /**
      *默认的方法
      **/
    public static final void getUserToken(
        ResponseCallback<java.utils.List<com.yuqianhao.User>> callback){}
    
    
}
```

好了，到此为止，我们看到了如何构建一个请求方法体，每一个请求方法模板对应一个请求方法体，但是我们注意到，编写一个这样长度的脚本也不是那么容易的，因为可能每一个请求接口的method，type，requestType会是一样的，而每一次都需要添加上，这样会显得编写起来很麻烦，所以这就是我们刚刚跳过的第二部分，默认创建请求的参数。

（3）、默认创建请求的参数

刚刚我们提到了，每一次编写重复的内容会显得很臃肿，所以插件提供了默认请求参数，它的定义如下：

```json
{
    "method":"post",
    "type":"form",
    "requestType":"async"
}
```

在模板中的定义如下;

```json
{
    "packageName":"com.yuqianhao.request",
    "defaultParameters":{
        "method":"post",
    	"type":"form",
    	"requestType":"async"
    },
    "content":[
        
    ]
}
```

如果指定了defaultParameters这个对象，那么在创建方法请求体的时候，不需要再编写那个对象中指定了的值，除非要修改，插件在生成代码的时候，会优先使用方法体中的参数，如果方法体中找不到必传的参数，那么会到defaultParameters中去寻找，如果依然寻找不到，将会报错误。例如：

```json
{
    "packageName":"com.yuqianhao.request",
    "defaultParameters":{
        "method":"post",
    	"type":"form",
    	"requestType":"async"
    },
    "content":[
        {
            "name":"getUserToken",
   			"url":"api/getUserToken",
            "params":[
                {
                    "id":"name",
                    "type":"java.lang.String"
                }
            ],
            "responseType":"com.yuqianhao.User"
        },
        {
            "name":"setUserToken",
            "method":"get",
   			"url":"api/getUserToken",
            "params":[
                {
                    "id":"name",
                    "type":"java.lang.String"
                },
                {
                    "id":"newName",
                    "type":"java.lang.String"
                }
            ],
            "responseType":"com.yuqianhao.User"
        }
    ]
}
```

那么生成的java代码会是这个样子的：

```java
public class LightHttpRequest {
    @PostRequest(url = "api/getUserToken")
    private static final class getUserToken {
    }

    public static final void getUserToken(
        java.lang.String name, 
        ResponseCallback<com.yuqianhao.User> callBack) {
    }

    @GetRequest(url = "api/getUserToken")
    private static final class setUserToken {
    }

    public static final void setUserToken(
        java.lang.String name, 
        java.lang.String newName, 
        ResponseCallback<com.yuqianhao.User> callBack) {
    }
}
```





#### 到此为止，您已经看到关于插件部分的内容，如果您有什么不能白的敌方，可以通过首页的Readme中查找到我们的联系方式联系到我们。

