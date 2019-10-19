# LightHttpBug记录

#### 1、当ResponceCallback的泛型参数为Void.class或者Nullptr.class的时候方法onSuccess无法被回调。

日期：2019年10月19日

状态：<font color=green>已修复</font>

Bug示例：

```java
LightHttp.loadRequest(BaiDuRequest.class)
		 .callback(new ResponseCallback<Void>() {
             @Override
             public void onFailure(int code, String message) {
             }

             @Override
             public void onSuccess(Void responcedata) {
                 //当不需要LightHttp序列化的时候，该方法无法被回调。
             }
          })
          .async();
```



