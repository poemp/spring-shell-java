# spring-shell-java
spring-shell-java

## 使用方法
>> 1、使用@ShellComponent注释类，可以使用name='xxx'重命名类
    `````
    @ShellComponent(name = "test")
    public class TestCommand {}
    `````
>> 2、使用@ShellMethod(name="xxx",detail="xxx")注释方法，name为名字，缺省默认为方法名
`````
  @ShellMethod(name = "method", detail = "测试方法")
  public void method(){}
`````
>> 3、使用@ShellOptions(detail="xxx")注释方法参数
``````
    @ShellMethod(name = "method", detail = "")
    public void method(
            @ShellOptions(detail = "Ip地址") String ip,
            @ShellOptions(detail = "地址") String address,
            @ShellOptions(detail = "使用的年限") String age){
            }
``````

## 注意：
1、同一个@ShellComponent中，@ShellMethod不能相同
2、同一个方法的参数的第一个字母不能相同。

## 参数
>> @ShellComponent

>>> name：命令行类，缺省是当前类的名字

>> @shellMethod

>>> name:方法名字，缺省是方法的名字

>>> detail:方法的说明，会显示在命令行中

>> @shellOptions

>>> detail 参数说明

## 使用
1、使用 help 可以查看生成的命令
``````
help
``````
2、使用 exit 退出整个工程
``````
exit
``````
3、实例
``````
用法: test method [-i,--id][-a,--age]
选项：
    -i ,--id       <ID>       端口号
    -a ,--age      <AGE>      使用的年限


用法: test method2 [-n,--name][-d,--dsfds][-i,--ip][-p,--port][-a,--age]
说明：测试方法第二
选项：
    -n ,--name     <NAME>     xxxxxx
    -d ,--dsfds    <DSFDS>    xxxxxx
    -i ,--ip       <IP>       Ip地址
    -p ,--port     <PORT>     端口号
    -a ,--age      <AGE>      使用的年限

``````

4、使用
`````
test method -i 8080 -a 45
`````
