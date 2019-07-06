#Dockerfile 都必须以 FROM 命令开始，指定镜像基于那个集成基础镜像创建
#dazer/guower
#FROM daocloud.io/maven:3.3.3
#FROM airdock/oracle-jdk:latest
#FROM frolvlad/alpine-oraclejdk8:slim
FROM java:8
#FROM jboss/wildfly
MAINTAINER dazer <duandazhi@ourslook.com>
#指定文件临时目录
VOLUME /tmp
#ADD：复制文件指令，ADD <source> <destination>  复制源文件到目标文件；source 可以是 URL 或者是启动配置上下文中的一个文件
ADD guower.jar app.jar
#RUN bash -c 'touch /app.jar'
#配置给容器一个可执行的命令；多个会覆盖只会执行最后的一个命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

#docker 修改市区，否则docker内部时间和服务器时间不一样，比如和oracle不一致，导致无法运行
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

