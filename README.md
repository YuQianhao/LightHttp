# LightHttp

![LightHttp](https://github.com/YuQianhao/LightHttp/blob/master/0.png)

​	框架，顾名思义，是一个能够让开大人员节省时间提高开发效率的支撑。如今框架满天飞的时代，百家争鸣，各显神通，渐渐的忽略了“框架”这个词语的本质，反而开发人员需要大量的时间和精力去学习一些花里胡哨，极其复杂的“无意义”框架，而忽略了本质侧重点的“业务逻辑”。

​	LightHttp是一个将网络请求简化，请求的配置简化的请求框架，能够让你从复杂的框架学习中逃离出来，将精力重新搬运回“业务逻辑”层面，现在的请求底层实现实现来自于[OkHttp](https://github.com/square/okhttp)。

​	LightHttp提供了的Get，Post，Put，Delete四种不同方法的同步和异步请求，提供了一个文件下载服务以及一个请求拦截处理器。

### 温馨提醒

如果你想给我这个项目Star的话，我觉得你可以暂停1分钟听我说，不要Star这个项目，因为我只是个封装者，核心并不是我做的，如果要Star，我觉得还是应该给[OkHttp](https://github.com/square/okhttp)，因为它为我们提供了核心，我们在它的基础上进行了高度包装而已。

### 如何依赖？

```gradle
implementation 'com.yuqianhao:LightHttp:1.3.5'
```

Bug收集

我们会将收到的开发者Bug反馈记录下来并尽力更正，详见[Bug收集日志](https://github.com/YuQianhao/LightHttp/blob/master/bug.md)，如果您有Bug或者疑问或者更好的建议提出，欢迎您添加我的微信：**185 6138 5652**。

### 最近一次修正记录

* 最高支持Android版本为11.
* 提高了对OkHttp的支持。
* 我们增加了实验性的Cookie操作。


### 如何构建一个网络请求？

---

发送一个异步请求。

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

发送一个同步请求。

```java
Response<TestBean> response=LightHttp
		.create(GetRequest.create("https://www.baidu.com/testApi.json"))
		.params(FormRequestParameter.create().add("ket","value"))
		.sync(TestBean.class);
int code=response.getCode();
TestBean testBead=response.to();
```

建造者模式提供给了我们一个很好的流式代码布局，能够让我们一眼就能看出这些代码代表的含义，减少了开发人员写代码和读代码的时间和精力成本。

到这里，这个网络请求就完整的结束了。

一个普通的网络请求应该分为四部分：

* 创建一个网络请求，设置这个请求的地址以及请求方法，例如Get，Post，Put，Delete
* 传入这个请求接口的参数以及参数的字符编码，这个参数的类型可能有Form或者Json
* 绑定这个请求请求成功后的回调函数用来接收结果
* 发送这个请求

#### 1、创建请求

LightHttp提供了3种不同的请求创建方式，我们会在下面的代码片段中给您展示。

* 直接创建
* 通过配置类创建
* 使用模板创建（如果您并不关心请求的创建过程，建议您使用这个方式，它可以提高您的开发效率）

##### （1）、直接创建

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

##### （2）、通过配置类创建

LightHttp还提供了一个静态方法**loadRequest**来创建一个请求，这个方法相当于create，庞大的项目需要一个统一管理请求地址的方案，显然每一次请求都去写一次请求地址不是一个好的主意，LightHttp注意到了这一点，提供了几个注解类：

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

##### （3）、使用模板创建

**在为您展示使用模板创建之前，建议您先了解一下下面的内容，简单了解一下LightHttp的传参，请求，类型转换，自动序列化等处理过程，因为模板创建的方式忽略了这些内容。如果您已经了解，那么您可以<a href="https://github.com/YuQianhao/LightHttp/blob/master/plugin/README.md">点击此处</a>直接查看模板创建方式。**

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

#### 3、绑定回调，获取应答的数据

我们通过了1和2两个小篇幅了解了如何创建一个请求和传递参数，一个完整的请求还应该来获取服务器的应答，来获取服务器传给我们的数据，当然，我们也可以不绑定回调函数，代表这个请求只是发送出去，而不会处理服务器返回的数据。

- 相对于异步请求，发送出去请求之后在异步等待结果的返回，所以采用Callback的方式去完成请求。

LightHttp提供了一个成员方法**callback**来设置异步请求的回调，如果不绑定回调函数则这个方法无需调用（在sync同步请求时，不需要指定callback），callback的原型如下：

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

* 对于同步请求，客户端发送出去请求之后会在当前线程环境中等待服务器的返回，无法采用Callback的方式进行返回，即便可以，也并不直观，所以采用了立即返回结果的方式进行使用。

LightHttp提供了一个sync方法进行同步请求，sync提供了连个重载版本，用来处理数据结果的转换。

```java
public Response sync();
public <_Tx> Response<_Tx> sync(Type type);
```

如果客户端不想知道服务器具体给出了什么样的应答，只需要知道请求已发出，那么可以调用无参数的sync方法，这个方法默认传入了一个Nullptr.class并调用了有参数的sync方法，两个方法都返回了一个Response对象，这个对象代表远程服务的应答，应答类提供了以下几个方法。

| 方法名称                                        | 说明                                         |
| ----------------------------------------------- | -------------------------------------------- |
| Headers getHeaders()                            | 获取Headers                                  |
| Long getContentLength()                         | 获取返回的数据的长度                         |
| byte[] getResponseBuffer()                      | 获取返回的数据的原始类型                     |
| String getResponseBufferString()                | 使用默认编码获取字符串类型的返回数据         |
| String getResponseBufferString(Charset charset) | 使用指定编码获取字符串类型的返回数据         |
| String getMessage()                             | 获取Http请求的状态消息                       |
| int getCode()                                   | 获取Http请求的状态码                         |
| MediaType getMediaType()                        | 获取返回数据类型的MediaType                  |
| _Tx to()                                        | 获取服务具体返回的数据，并给提供转换后的版本 |

值得注意的是，to()方法返回了一个_Tx的泛型类型，这个泛型的具体类型由传入sync方法里的类型决定，例如：

```java
//需要获取返回结果并处理
Response<TestBean> response=LightHttp....sync(TestBean.class);
//无需获取返回结果
Response response=LightHttp....sync();
```

**注意！如果调用sync()，不传入任何具体的Class，那么to()方法一定会返回null。除非传入给sync一个具体的类型。除此之外的任何方法都可以安全调用。**

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
Response<Student> response=LightHttp
					.create(GetRequest.create("https://www.baidu.com"))
         			.params(FormRequestParameter.create().add("k1","v1").add("k2","v2"))
    	 			.sync(Student.class);
```

到这里，我们就结束了LightHttp的网络请求部分，接下来我们将用一个短小的篇幅来介绍LightHttp提供的请求配置，例如超时设置，Cookie设置和请求监听。

### 如何配置超时？

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

### 如何下载文件？

----

LightHttp支持下载文件以及下载文件进度的监听。

```java
public static class Main implements IDownloadCallback{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IDownloadAction downloadAction=LightHttp.createDownload("文件下载地址","文件保存路径");
        downloadAction.setOnDownloadListener(this);
        downloadAction.start();
    }
    
    @Override
    public void onDownloadStart() {
        Log.e("Download","开始下载");
    }

    @Override
    public void onDownloadError(int core, Exception e) {
        Log.e("Download","下载失败："+e.getMessage());
    }

    @Override
    public void onDownloadProgress(int size, int maxSize, double schedule) {
		Log.e("Download","已下载："+size+"，总长度："+maxSize+"，下载进度："+(schedule*100)+"%");
    }

    @Override
    public void onDownloadComplete(File file) {
		Log.e("Download","下载完成");
    }

    @Override
    public void onDownloadCancel() {
        Log.e("Download","取消下载");
    }
}
```

控制台输出：

```text
开始下载
已下载：4096,总长度：81920，下载进度：5%
已下载：8192,总长度：81920，下载进度：10%
已下载：12888,总长度：81920，下载进度：15%
已下载：16384,总长度：81920，下载进度：20%
...
已下载：77824,总长度：81920，下载进度：95%
已下载：81920,总长度：81920，下载进度：100%
下载完成
```

LightHttp提供了一个静态方法**createDownload**来创建一个下载任务，这个方法有三个重载版本：

```java
public static final IDownloadAction createDownload(){
	return new DownloadImpl();
}

public static final IDownloadAction createDownload(String url){
	return new DownloadImpl(url);
}

public static final IDownloadAction createDownload(String url,String path){
	return new DownloadImpl(url,path);
}
```

这三个方法都会创建一个接口类**IDownloadAction**的实例，这个接口类里定义了所有关于下载文件的方法。

```java
public interface IDownloadAction {

    IDownloadAction setDownloadUrl(String url);

    IDownloadAction setDownloadSaveFile(String path);

    boolean isDownloading();

    IDownloadAction setOnDownloadListener(IDownloadCallback downloadListener);

    IDownloadAction start();

    void cancel();
}
```

* setDownloadUrl(String url)：设置下载文件的URL地址，在start方法调用之前调用有效。
* setDownloadSaveFile(String path)：设置下载文件在本地保存的路径，在start方法调用之前调用有效。
* isDownloading()：获取当前的下载状态。
* setOnDownloadListener(IDownloadCallback downloadListener)：设置下载监听器
* start()：开始下载。
* cancel()：取消当前已经开始的下载任务。

LightHttp提供了一个**IDownloadCallback**接口类用来监听下载的进度和状态，定义如下：

```java
public interface IDownloadCallback {

    void onDownloadStart();

    void onDownloadError(int core,Exception e);

    void onDownloadProgress(int size,int maxSize,double schedule);

    void onDownloadComplete(File file);

    void onDownloadCancel();
}
```

* onDownloadStart()：开始下载

* onDownloadError(int core,Exception e)：下载失败，失败原因由参数一和二决定

  参数一：core，定义了下载错误的编码，例如404,403

  参数二：e，定义了下载错误的异常类。

* onDownloadProgress(int size,int maxSize,double schedule)：下载进度的监听，下载的时候会通过调用这个方法来传递下载的进度：

  参数一：size，当前已经下载的长度

  参数二：maxSize，文件的总长度

  参数三：schedule，当前已经下载的进度，取值范围为0-1，可以将此参数*100获得下载进度的百分比。

* onDownloadComplete(File file)：下载完成的回调方法，下载完成LightHttp将会调用这个方法并把下载好的文件通过参数传递回来。
* onDownloadCancel()：这个下载人物被取消。

通常来说，我们通过静态方法创建并设置下载地址和文件保存路径来创建一个下载任务，如果下载地址和文件保存路径有一个缺失的这个下载任务将不会正常运转。当然，也可以使用无参数的静态方法创建实例，但是在start方法调用之前需要调用setDownloadUrl方法和setDownloadSaveFile方法设置。

**在创建下载任务并下载之前要确保拥有*网络权限*和*外置存储路径读写权限*。**

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

### LightHttp提供的请求拦截器

---

LightHttp在OkHttp的基础上额外提供了一个请求拦截器，这个拦截器可以拦截和修改请求所有相关的内容，包括返回值，可以在返回值到达CallBack的时候进行拦截修改。

LightHttp提供了一个静态方法**setRequestFirstHandler**来设置一个请求拦截器，这个方法的定义如下：

```java
public static final void setRequestFirstHandler(IRequestFirstHandle requestFirstHandler);
```

这个方法接受一个接口类**IRequestFirstHandle**的实例，这个接口类定义如下：

```java
public interface IRequestFirstHandle {

    String handlerUrl(String requestUrl);

    Map<String,String> handlerHeader(Map<String,String> header);

    String handlerBody(String body);

    String handlerResponse(String response);
    
    void cookie(String hosts, List<String> value);

    List<String> loadCookie(String hosts);
    
}
```

* handlerUrl：这个方法可以在请求之前处理请求的地址，在请求在创建的时候，会将请求地址通过参数一传递给这个方法，这个方法可以处理一下请求地址，例如增加一个请求头，或者修改一下请求地址，然后通过返回值返回即可。
* handlerHeader：这个方法可以在请求之前处理请求传递的Header，在请求在创建的时候，会将本次请求传递的Header通过参数一传递给这个方法，这个方法可以处理一下本次请求使用的Header，然后通过返回值返回即可。
* handlerBody：这个方法可以在请求之前处理一下请求要传递的Body，在青丘创建的时候，会将本次请求传递的Body通过参数一传递给这个方法，这个方法处理一下未格式化的请求Body，然后通过返回值进行返回。
* handlerResponse：这个方法可以在请求结束后处理请求返回的数据，这个方法会在数据进行格式化之前被调用，传递给这个方法的数据是未格式化的原始数据，处理完成后通过返回值的方式返回原始数据，然后LightHttp在进行数据反序列化或者直接调用Callback直接传递给请求方。
* cookie：当网络请求执行完成的时候，会将获取到的cookie传送给这个方法，如果开发者传入了**AbsRequestFirstHandler**这个类的实例，那么可以直接调用super的版本即可，他为我们提供了一个简单缓存的版本。
* loadCookie：当创建网络请求的时候，LightHttp在需要上传Cookie的时候调用这个方法，，如果开发者传入了**AbsRequestFirstHandler**这个类的实例，那么可以直接调用super的版本即可，他为我们提供了一个简单缓存的版本。

当然， 并不是需要开发人员把所有的方法全部实现，开发人员直接传入**AbsRequestFirstHandler**类的实例，这个类实现了这个接口，通常来讲，建议在Application的onCreate中调用**setRequestFirstHandler**方法设置请求拦截器。

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

