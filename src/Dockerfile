FROM centos:7
# 创建一个新目录来存储jdk文件
RUN mkdir /usr/local/java
#将jdk压缩文件复制到镜像中，它将自动解压缩tar文件
ADD jdk-11.0.16_linux-x64_bin.tar.gz /usr/local/java/
# 设置环境变量
ENV JAVA_HOME /usr/local/java/jdk-11.0.16
ENV PATH $JAVA_HOME/bin:$PATH
VOLUME /tmp

# 将jar包添加到容器中并更名为app.jar
ADD ./library.jar /app.jar

EXPOSE 8080

# 运行jar包
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar","–server.port=8080"]