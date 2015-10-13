CBR Quickstart
--------

### Download tools
* [wildfly-8.2.0-Final](http://download.jboss.org/wildfly/8.2.0.Final/wildfly-8.2.0.Final.tar.gz)
* [wildfly-camel-patch-3.2.0](https://github.com/wildfly-extras/wildfly-camel/releases/download/2.3.0/wildfly-camel-patch-2.3.0.tar.gz)
* [blacktie-5.1.0-Final](http://downloads.jboss.org/jbosstm/5.1.0.Final/binary/blacktie-5.1.0.Final-centos54x64-bin.tar.gz)

### Install
* install wildfly-8.2.0-Final
```
tar xzvf wildfly-8.2.0.Final.tar.gz
```
* install wildfly-camel-patch
```
tar xzvf wildfly-camel-patch-2.3.0.tar.gz -C wildfly-8.2.0.Final
```
* install blacktie
```
tar xzvf blacktie-5.1.0.Final-centos54x64-bin.tar.gz
```
* install wildfly-blacktie
```
unzip blacktie-5.1.0.Final/wildfly-blacktie-build-5.1.0.Final-bin.zip -d wildfly-8.2.0.Final/
```
* deploy the blacktie-admin-service
```
cp blacktie-5.1.0.Final/blacktie-admin-services-ear-5.1.0.Final.ear wildfly-8.2.0.Final/standalone/deployments
```

### Configuration
```
cp config/* wildfly-8.2.0.Final/standalone/configuration
export JBOSS_HOME=`pwd`/wildfly-8.2.0.Final
```
You should edit blacktie-5.1.0.Final/setenv.sh and make sure to replace the BLACKTIE_HOME nd JBOSSAS_IP_ADDR
```
. blacktie-5.1.0.Final/setenv.sh
```

### Start the wildfly
```
cd wildfly-8.2.0.Final
bin/standalone.sh -c standalone-blacktie.xml -Djboss.bind.address=localhost
```

### Run the quickstart
```
./run.sh
```
