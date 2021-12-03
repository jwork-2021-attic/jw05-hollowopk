# jw05

本次实验中，我实现了服务端和客户端两方代码。具体思路为首先开启服务端，然后开启客户端。在服务端对游戏内容以及用户输入进行处理，并将处理后的数据传输给客户端，由客户端以此展示图形化界面。同时，客户端在接到用户键盘输入时，将数据传送到服务端进行处理。代码分别上传至jw05仓库中的server和client分支，对应服务端和客户端代码。

#### Server端：

Server端中有两个主类，分别为MainServer和NIOServer。其中MainServer为使用多线程websocket实现，NIOServer为使用NIO中selector实现。运行对应主类的main方法即可开启对应服务。

#### Client端：

Client端中与Server端类似，有两个主类MainClient和NIOClient，对应Server端中两个主类，开启方法为在Main类中调用Application类的launch方法，并传入对应参数。

```java
public static void main(String[] args) {
    //Application.launch(MainClient.class);
    Application.launch(NIOClient.class);
}
```

# jw06：

在服务端向客户端传送地图信息时，将数据包装在一个MapData对象中，通过网络发送给客户端，客户端从中取出数据并以此构建地图。在每次客户端收到该对象时，都将其写入到record文件中。由此，在客户端新建RecordPlayer类，并可类似于jw05中客户端代码启动方式启动回放，即可从文件中依次重新取出MapData对象并解析展示到屏幕，从而实现回放。

# jw07：

在jw05基础上，开启服务端后，可以开启多个客户端，即可进行对战。（目前为2人对战，服务器会等待至两个客户端都连接成功后才生成地图并推送给客户端）。