#!/bin/bash
#author SpereShelde
base_dir=$(cd "$(dirname "$0")";pwd)

JDK_FILE=$(ls | grep jdk-*-linux-*.tar.gz)
#下载JDK
download(){
os_version=`uname -a`
echo $os_version
architecture="64"
echo "$os_version" | grep -q "$architecture"

if [ $? -eq 0 ]
then
if [ ! -f "$JDK_FILE" ]; then
echo "您正在使用64位操作系统，为您选择64位JDK"
wget -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz
fi
else
if [ ! -f "$JDK_FILE" ]; then
echo "您正在使用32位操作系统，为您选择32位JDK"
wget -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-i586.tar.gz
fi
fi
JDK_FILE=$(ls | grep jdk-*-linux-*.tar.gz)
}

#安装JDK
install_jdk(){
JAVA_DIR=/usr/local/java
JDK_DIR="jdk1.8.0_131"
JDK_PATH="$JAVA_DIR"/"$JDK_DIR"

tar xzf $JDK_FILE

mkdir -p $JAVA_DIR
mv $JDK_DIR  $JAVA_DIR
#配置环境变量
cp ~/.bashrc ~/.bashrc.backup.java
if [ ! -n "$JAVA_HOME" ]; then
echo "export JAVA_HOME=\"$JDK_PATH\"" >> ~/.bashrc
fi
if [ ! -n "$JRE_HOME" ]; then
echo "export JRE_HOME=\"\$JAVA_HOME/jre\"" >> ~/.bashrc
fi
if [ ! -n "$CLASSPATH" ]; then
echo "export CLASSPATH=.:\$JDK_PATH/lib/dt.jar:\$JDK_PATH/lib/tools.jar" >> ~/.bashrc
fi
echo "export PATH=\$JAVA_HOME/bin:\$JRE_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc
echo "JDK install success!"
}

download_release(){
echo "Downloading BoxHelper..."
wget --no-check-certificate 'https://github.com/SpereShelde/BoxHelper/releases/download/v1.0/BoxHelper.jar'
wget --no-check-certificate 'https://raw.githubusercontent.com/SpereShelde/BoxHelper/master/BoxHelper.conf'
}

copy_wget(){
echo "Copy wget to /bin ..."
cp /usr/bin/wget /bin/wget
}

main(){
download
if [ $? != 0 ]; then
echo "JDK download  failed"
exit 1
fi
install_jdk
if [ $? != 0 ]; then
echo "JDK install failed"
exit 1
fi
download_release
if [ $? != 0 ]; then
echo "Release download failed"
exit 1
fi
copy_wget
if [ $? != 0 ]; then
echo "Copy wget failed"
exit 1
fi
}
main
