echo "Quickstart: Running CBR"

# BUILD THE CBR TestOne SERVER
echo "Build CBR_TestOne server"
generate_server -Dservice.names=CBR_TestOne -Dserver.includes=TestOneService.c -Dserver.name=server_one
if [ "$?" != "0" ]; then
	exit -1
fi
mv server svr/server_one

# BUILD THE CBR TestTwo SERVER
echo "Build CBR_TestTwo server"
generate_server -Dservice.names=CBR_TestTwo -Dserver.includes=TestTwoService.c -Dserver.name=server_two
if [ "$?" != "0" ]; then
	exit -1
fi
mv server svr/server_two

# BUILD CLIENT
echo "Build CBR client"
generate_client -Dclient.includes=client.c
mv client cli/

# BUILD CAMEL ROUTER AND DEPLOY
echo "Build Camel Router"
cd router
mvn install
if [ "$?" != "0" ]; then
    echo "BUILD Camel Router FAILED"
    exit -1
fi

# WAIT
echo "Waiting for the CBR example to deploy"
cp target/blacktie-camel-router-2.3.0.war $JBOSS_HOME/standalone/deployments/
sleep 10
echo "Waited for the CBR example to deploy"

# RUN TestOne AND TestTwo SERVER
cd ../svr
btadmin startup
if [ "$?" != "0" ]; then
    echo "STARTUP CBR servers FAILED"
    exit -1
fi

# RUN THE C CLIENT
cd ../cli
result=`./client`
if [ "$?" != "0" ]; then
  echo "BUILD FAILED - CLIENT APPLICATION FAILED"
	killall -9 server_one
	killall -9 server_two
	exit -1
fi

# SHUTDOWN SERVERS
cd ../svr
btadmin shutdown
if [ "$?" != "0" ]; then
	exit -1
fi

# CHECK RESULT
if [ "$result" != "TestOneTestTwo" ]; then
	echo "result is $result, not expect TestOneTestTwo"
	exit -1
fi

echo "CBR Quickstart test OK"
