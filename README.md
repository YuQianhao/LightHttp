# LightHttp

![于千皓](https://github.com/YuQianhao/LightHttp/blob/master/0.png)

​	框架，顾名思义，是一个能够让开大人员节省时间提高开发效率的支撑。如今框架满天飞的时代，百家争鸣，各显神通，渐渐的忽略了“框架”这个词语的本质，反而开发人员需要大量的时间和精力去学习一些花里胡哨，极其复杂的“无意义”框架，而忽略了本质侧重点的“业务逻辑”。

​	LightHttp是一个将网络请求简化，请求的配置简化的请求框架，能够让你从复杂的框架学习中逃离出来，将精力重新搬运回“业务逻辑”层面，现在的请求底层实现实现来自于[OkHttp](https://github.com/square/okhttp)。

### 如何使用？

------

[![](https://www.jitpack.io/v/YuQianhao/LightHttp.svg)](https://www.jitpack.io/#YuQianhao/LightHttp)

- Gradle

1、首先将jitpack仓库添加到项目的build.gradle中

```text
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

2、在将仓库的项目依赖到你的项目Module的build.gradle中

```text
implementation 'com.github.YuQianhao:LightHttp:1.0.14'
```

### Bug收集

我们会将收到的开发者Bug反馈记录下来并尽力更正，详见[Bug收集日志](https://github.com/YuQianhao/LightHttp/blob/master/bug.md)，如果您有Bug或者疑问或者更好的建议提出，欢迎您添加我的微信：**185 6138 5652**。

### 如何构建一个网络请求？

---

```java
LightHttp.create(GetRequest.create("https://www.baidu.com/testApi.json"))
         .params(FormRequestParameter.create().add("ket","value"))
         .callback(new ResponseCallback<TestBean>() {
              @Override
              public void onSuccess(TestBean responseData) {
              }
          })
          .async();
```

建造者模式提供给了我们一个很好的流式代码布局，能够让我们一眼就能看出这些代码代表的含义，减少了开发人员写代码和读代码的时间和精力成本。

到这里，这个网络请求就完整的结束了。

一个普通的网络请求应该分为四部分：

* 创建一个网络请求，设置这个请求的地址以及请求方法，例如Get，Post，Put，Delete
* 传入这个请求接口的参数以及参数的字符编码，这个参数的类型可能有Form或者Json
* 绑定这个请求请求成功后的回调函数用来接收结果
* 发送这个请求

#### 1、创建请求

LightHttp使用了一个静态方法LightHttp.create()来创建一个请求，这个静态方法的原型如下：

```java
public static final LightHttp create(IRequestAddress address);
public static final LightHttp create(IRequestAddress address,Object tag);
```

create方法接收了一个接口类**IRequestAddress**，这个类包装了要请求的地址和请求方法，接口类的定义如下：

```java
public interface IRequestAddress {

    String url();

    int method();

    Object tag();

}
```

* String url()：方法用来指定要请求的地址
* int method()：方法指定请求的类型
* Object tag()：方法来制定请求的Tag

通常来讲，我们没有必要去实现这个接口，因为LightHttp预设了四种请求：

* GetRequest：Get请求
* PostRequest：Post请求
* PutRequest：Put请求
* DeleteRequest：Delete请求

这四个实现类的都提供了一个重载的**create**方法，用来帮助开发人员快速创建一个类的实例，例如我们要创建一个Post请求

```java
LightHttp.create(PostRequest.create("https://www.baidu.com"));
LightHttp.create(PostRequest.create("https://www.baidu.com",1));
```

然而创建请求还有第二种方式，LightHttp还提供了一个静态方法**loadRequest**来创建一个请求，这个方法相当于create，庞大的项目需要一个统一管理请求地址的方案，显然每一次请求都去写一次请求地址不是一个好的主意，LightHttp注意到了这一点，提供了几个注解类：

| 注解类         | 对应的请求类  |
| -------------- | ------------- |
| @GetRequest    | GetRequest    |
| @PostRequest   | PostRequest   |
| @PutRequest    | PutRequest    |
| @DeleteRequest | DeleteRequest |

```java
public class RequestAddressManager{
    
    @GetRequest("https://www.baidu.com/image.json")
    public static class Image{}
    
    @PostRequest(url="https://www.baidu.com/setNickName.json",charset="UTF-8")
    public static class SetNickName{}
}
```

```java
LightHttp.loadRequest(RequestAddressManager.Image.class);
```

#### 2、传递参数

LightHttp使用成员方法params来传递请求的参数，方法原型如下：

```java
public final LightHttp params(IRequestParameter requestParameter);
```

params方法接收一个接口类IRequestParameter的参数，这个接口类的声明如下：

```java
public interface IRequestParameter {

    String mediaType();

    Charset charset();

    Headers headers();

    String data();

}
```

这个接口类定义了四个函数，这四个函数分别对应了请求参数的基本数据。

* String mediaType()：参数的类型，例如Form，Json，File等一些常见类型
* Charset charset()：传递的参数的字符编码
* Headers headers()：传递参数的Headers
* String data()：传递的实际参数

LightHttp预设了这个接口类的实现类，通常来说不需要开发人员去实现，预设了**FormRequestParameter**和**JsonRequestParameter**这两种类型的请求参数，分别对应Form（application/x-www-form-urlencoded）和Json（application/json）。

*※ 如果开发人员不满足于这两种类型，可以继承**AbsRequestParameter**这个类来实现其他类型的请求参数，可以参考[FormRequestParameter](https://github.com/YuQianhao/LightHttp/blob/master/LightHttp-Library/src/main/java/com/yuqianhao/lighthttp/reqbody/FormRequestParameter.java)源码。*

预设的这两个请求参数包装类都提供了一个重载静态方法**create**来帮助开发人员去快速创建一个实例，其中FormRequestParameter提供了**add(String key,String value)**和**addMap(Map<String,String> map)**来构建请求参数，而JsonRequestParameter提供了**json(Object obj)**来将一个对象反序列化成Json字符串，如果开发人员直接拥有Json字符串，那么可以使用另一个方法**jsonSource(String jsonSource)**直接传入一个Json字符串。

```java
FormRequestParameter.create();
FormRequestParameter.create(Charset chatset);
JsonRequestParameter.create();
JsonRequestParameter.create(Charset chatset);
```

其中这两个子类都拥有header方法的两个版本，用来向请求中添加Header。

```java
FormRequestParameter.create().header("key","value");
```

```java
Map<String,String> headerMap=new HashMap<>();
headerMap.put("key","value");
FormRequestParameter.create().header(headerMap);
```

那么我们可以通过下面几个示例来结束传递参数的内容：

```java
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(FormRequestParameter.create().add("k1","v1").add("k2","v2"));
```

```java
Map<String,String> patameter=new HashMap<>();
patameter.put("k1","v1");
patameter.put("k2","v2");
LightHttp.create(GetRequest.create("https://www.baidu.com",Charset.UTF8))
         .params(FormRequestParameter.create().addMap(patameter));
```

```java
Student student=new Student();
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(JsonRequestParameter.create().json(student))
```

```java
String jsonSource="{\"name\":\"LightHttp\",\"age\":18}";
LightHttp.create(GetRequest.create("https://www.baidu.com",Charset.UTF8))
         .params(JsonRequestParameter.create().jsonSource(jsonSource))
```

#### 3、绑定回调

我们通过了1和2两个小篇幅了解了如何创建一个请求和传递参数，一个完整的请求还应该来获取服务器的应答，来获取服务器传给我们的数据，当然，我们也可以不绑定回调函数，代表这个请求只是发送出去，而不会处理服务器返回的数据。

LightHttp提供了一个成员方法**callback**来设置回调，如果不绑定回调函数这个方法无需调用，callback的原型如下：

```java
public final LightHttp callback(ResponseCallback responseCallback);
```

callback是由LightHttp提供的一个**ResponseCallback<_Tx>**类构成，这个类是一个抽象类，抽象方法如下：

```java
public void onFailure(int code,String message){}
public void onSuccess(_Tx responseData){}
public void onCompany(){}
```

网络请求分为两个情况，一种是请求成功，一种是请求失败，而LightHttp为了便于开发人员操作还提供了一个无论如何都会回调的方法**onCompany**。

* onFailure：如果请求失败了就会回调这个方法，并且将错误编码和错误信息传递给对应的参数1和参数2。
* onSuccess：如果请求成功就会回调这个方法，如果**返回的数据是Json格式**，并且在**前端拥有和返回的数据格式完全对应的Bean**，LightHttp会直接将返回的数据通过类声明的泛型参数直接反序列化成对应的对象并传递给这个方法。
* onCompany：无论请求成功还是失败，这个方法都会回调。

onSuccess是一个特殊的方法,它的方法参数类型是会根据类声明指定的泛型参数类型而改变，例如：

```java
ResponseCallback<Student> responceCallback=new ResponseCallback<>(){
    @Override
    public void onSuccess(Student responseData) {
        
    }
};
```

1、当服务端返回了一个Json格式的数据，并且前端刚好有对应的Bean，例如服务器返回的数据如下：

```json
{
    "name":"LightHttp",
    "age":18
}
```

前端刚好有对应的bean，如：

```java
public class Student{
    public String name;
    public int age;
}
```

那么ResponseCallback的泛型参数就可以指定为Student，当回调onSuccess的时候LightHttp会自动的将Json反序列化成Student并传递给方法。

2、服务端返回的不是Json或者前端没有对应的Bean，但是开发人员还是想LightHttp直接将返回的数据自动的反序列化成想要的数据，那么开发人员可以自定义一个数据转换器，例如服务端返回的是xml格式的数据：

```xml
<name>LightHttp</name>
<age>18</age>
<sex>neutral</sex>
```

而且前端拥有的数据类型定义如下：

```java
public class Student{
    public String name;
    public int age;
    public String className;//班级
}
```

这种情况下LightHttp无法反序列化成对应的数据，那么开发人员可以创建一个类并且实现**TypeConvertProcessor<_Tx>**这个接口，例如：

```java
@ConvertProcessor(className = Student.class)
public class StudentConvert implements TypeConvertProcessor<Student> {

    @Override
    public Student convertType(byte[] sourceBuffer) {
        XmlAnalysis xml=XmlAnalysis.from(new String(sourceBuffer,CharSet.UTF8));
        Student student=new Student();
        student.name=xml.getElement("name").asString();
        student.age=xml.getElement("age").asInt();
        student.className="无班级";
        return student;
    }

}
```

注解@ConvertProcessor拥有一个参数className，className用来指定要为哪一个类型进行数据转换。

创建完成过后通过调用LightHttp的静态方法**loadTypeConvert**来添加到反序列化器队列中：

```java
LightHttp.loadTypeConvert(StudentConvert.class);
```

通常来说，加载类转换器应该在Application中统一调用，例如

```java
public class Application{
    @Override
    public void onCreate(){
        super.onCreate();
        LightHttp.loadTypeConvert(StudentConvert.class,Person.class,ClassGroup.class);
    }
}
```

3、请求不需要任何转换器，只需要知道请求成功，或者不需要LightHttp进行反序列化，那么可以传递给ResponseCallback一个Nullptr类型或者让Void类型，例如：

```java
ResponseCallback<Void> responceCallback=new ResponseCallback<>(){
    @Override
    public void onSuccess(Void responseData) {
        
    }
};
ResponseCallback<Nullptr> responceCallback=new ResponseCallback<>(){
    @Override
    public void onSuccess(Nullptr responseData) {
        
    }
};
```

在这种情况下， 我们可以调用ResponseCallback提供的几个成员方法来获取服务器返回的数据，例如：

```java
ResponseCallback<Void> responceCallback=new ResponseCallback<>(){
    @Override
    public void onSuccess(Void responseData) {
        byte[] responceData=getResponseBuffer();
        String responceString=getResponseBufferString();
    }
};
```

ResponseCallback提供的成员方法如下：

| 方法名称                                        | 说明                                 |
| ----------------------------------------------- | ------------------------------------ |
| Headers getHeaders()                            | 获取Headers                          |
| Long getContentLength()                         | 获取返回的数据的长度                 |
| byte[] getResponseBuffer()                      | 获取返回的数据的原始类型             |
| String getResponseBufferString()                | 使用默认编码获取字符串类型的返回数据 |
| String getResponseBufferString(Charset charset) | 使用指定编码获取字符串类型的返回数据 |
| String getMessage()                             | 获取Http请求的状态消息               |
| int getCode()                                   | 获取Http请求的状态码                 |
| MediaType getMediaType()                        | 获取返回数据类型的MediaType          |

我们接下来会用几个示例来结束第三部分

①服务返回的学生信息

```java
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(FormRequestParameter.create().add("k1","v1").add("k2","v2"))
    	 .callback(new ResponseCallback<Student>(){
        	@Override
            public void onSuccess(Student student){
                runOnUI({
                    mStudentNameView.setText(student.getName());
                })
            }
    	});
```

②服务返回了一个图片的数据

```java
@ConvertProcessor(className = Bitmap.class)
public class BitmapConvert implements TypeConvertProcessor<Bitmap> {

    @Override
    public Bitmap convertType(byte[] sourceBuffer) {
       	Bitmap bitmap=BitmapFactory.createWith(sourceBuffer);
        return bitmap;
    }

}
```

```java
public class Application{
    @Override
    public void onCreate(){
        super.onCreate();
        LightHttp.loadTypeConvert(BitmapConvert.class);
    }
}
```

```java
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(FormRequestParameter.create().add("k1","v1").add("k2","v2"))
    	 .callback(new ResponseCallback<Bitmap>(){
        	@Override
            public void onSuccess(Bitmap studentFace){
                runOnUI({
                    mStudentFaceImage.setImageBitmap(studentFace);
                })
            }
    	});
```

③服务器返回了一个文件

```java
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(FormRequestParameter.create().add("k1","v1").add("k2","v2"))
    	 .callback(new ResponseCallback<Void>(){
        	@Override
            public void onSuccess(Void data){
                LightIo.io(PathUtils.create("data.file"))
                    .write(getResponseBuffer())
                    .close();
            }
    	});
```

#### 4、发送请求

我们通过前面三个篇幅了解到如何创建请求，并且传递请求参数和绑定回调函数，那么我们将用最后一个篇幅来结束LightHttp的网络请求部分。

LightHttp提供了两个成员方法来发送请求：

* async：发送异步请求
* sync：发送同步请求

例如：

```java
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(FormRequestParameter.create().add("k1","v1").add("k2","v2"))
    	 .callback(new ResponseCallback<Student>(){
        	@Override
            public void onSuccess(Student student){
                runOnUI({
                    mStudentNameView.setText(student.getName());
                })
            }
    	})
    	.async();
```

```java
LightHttp.create(GetRequest.create("https://www.baidu.com"))
         .params(FormRequestParameter.create().add("k1","v1").add("k2","v2"))
    	 .callback(new ResponseCallback<Student>(){
        	@Override
            public void onSuccess(Student student){
                runOnUI({
                    mStudentNameView.setText(student.getName());
                })
            }
    	})
    	.sync();
```

到这里，我们就结束了LightHttp的网络请求部分，接下来我们将用一个短小的篇幅来介绍LightHttp提供的请求配置，例如超时设置，Cookie设置和请求监听。

### 如何配置超时和Cookie？

---

LightHttp提供了一个init方法和loadTypeConvert方法来完成对于请求之前的配置。

#### 1、全局初始化

```java
public static final void init(RequestConfig requestConfig);
```

init方法接收一个RequestConfig作为参数，这个类作为网络请求的配置类，里面包含了如下的方法：

```java
public class RequestConfig {

    public TimeUnit timeUnit(){return TimeUnit.MINUTES;}

    public long connectTimeout(){return 1L;}

    public long writeTimeOut(){return 5L;}

    public long readTimeout(){return 5L;}

    public long callTimeout(){return 1L;}

    public RequestInterceptor requestInterceptor(){return null;}

}
```

* TimeUnit timeUnit()：超时的时间计量单位，默认为分钟。
* long connectTimeout()：连接超时时间，默认为1分钟。
* long writeTimeOut()：写数据的超时时间，默认为5分钟。
* long readTimeout()：读数据的超时时间，默认为5分钟。
* long callTimeout()：调用的超时时间，默认为1分钟。
* RequestInterceptor requestInterceptor()：返回一个网络请求的监听器

RequestInterceptor 是一个网络请求的监听器，在这个方法中创建一个RequestInterceptor的实例，并且重写方法request，开发人员可以获取到每一次请求的所有的数据，这个类的定义如下：

```java
public class RequestInterceptor {

    public void request(String url, 
                        String method, 
                        String contentType, 
                        Charset charset, 
                        Map<String,String> headers, 
                        String body){}

}
```

#### 2、绑定类型转换器

LightHttp可能无法全部的为开发人员快速的将服务器返回的数据转换为对应的数据类型，开发人员可以实现接口类TypeConvertProcessor并且使用注解@ConvertProcessor来标注一个类来创建一个转换器。

```java
public static final void loadTypeConvert(Class ...typeConvertProcessors);
```

### 何时进行初始化？

---

建议在Application的onCreate中进行init方法和loadTypeConvert方法的调用，例如：

```java
public class Application{
    @Override
    public void onCreate(){
        super.onCreate();
        LightHttp.init(new RequestConfig());
        LightHttp.loadTypeConvert(StudentConvert.class,Person.class,ClassGroup.class);
    }
}
```

### 开源许可

---

```text
Copyright 2019 YuQianhao, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

