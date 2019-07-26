# TEST FRAMEWORK COLLECTION

Container based test framework for application container migrations 


# 1. Environment Setup

There are a few differences in configuration based on your operating system. 
Required component installation for project execution: <br>
	- Java JDK8 - for all areas of the project <br> 
	- Maven - for all areas of the project <br>
	- Chrome and Firefox browsers - for Selenium UI tests <br>
	- Execution rights to files under the src/test/resources/drivers - for Selenium UI tests  <br>
	- run solution <br>

### 1.1 Windows Setup <a name="1.1"></a>

For the windows platform we have TWO WAYS of configuring the environment. We will first take the more simple approach, install windows apps and libraries via the Chocolatey package manager. 

#### I. SETUP WITH PACKAGE MANAGER - Chocolatey (quick and easy setup)
---
```
https://chocolatey.org/
```
Go to chocolatey page, download and install the kit.

After chocolatey has been setup, open a terminal and run the commands:

```choco install jdk8```
The command will install JDK 8 and setup the Java and the Java_Home environment variables

```choco install maven```
The command will setup Maven on the machine.

You should now have a working windows environment for implementing some tests. You can double check the installation of the JDK by running:
```java -version```

### 1.2 MacOS Setup <a name="1.2"></a>

Like with Windows, we can either use a package manager to install the needed applications or we can do it manually.

#### I. Setup via Homebrew

```
https://brew.sh/
```
Installation instructions can be found on the brew website, but we will provide quick reference code snippets. So the next command will actually install homebrew, you can find the same command on their website.
```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
Once you have installed homebrew, you will need to add versions repositories, so we setup a stable version of Java.
```
brew tap caskroom/versions
```
You should now be able to install Java JDK 8 by running:
```
brew cask install java8
```
Now you just need to install maven and all is done:
```
brew install maven
```



### 1.3 Linux Setup <a name="1.3"></a>

Setup guide is done with Ubuntu/Debian based linux distributions.

#### OpenJDK

Linux systems will usually have an openJDK installed, so this step might be skipped. 
You will need to run the following commands in sequence.
```
sudo apt update
```
Will update the repositories list.
```
sudo apt install openjdk-8-jdk
```
It will install open JDK on your machine

```
sudo apt install maven
```

#### Oracle JDK

Alternatively, you can install the oracle JDK
```
add-apt-repository ppa:webupd8team/java
```
It will add repository for oracle packages

```
apt update
```
Will update the list of dependencies on your computer

```
apt install oracle-java8-installer
```
Will install oracle JDK 8



# 2. Project Run 


This section describes different ways of running the project, Container/native command line run. We will be focusing on the command line run due to its usage in CI pipelines and containers. 

Note: if you have problems importing the project in an IDE (IntelliJIdea or Eclipse) please delete all configuration folders and files (i.e. /.idea/ /.project/ .project .classpath etc) and reimport the project. The problems are caused by IDE configuration clash. Project should be initialized based on the pom configuration.

The project makes use of maven profiles to separate the different types of builds required for different areas of testing. In the light of this approach we have defined a few build profiles.
The default profile ```-Pdefault``` will be used for Serenity and JUnit tests. 
The jmeter profile ```-Pjmeter``` is used when running the jmeter scripts, since it uses different plugins.

There are also profiles that will trigger automatically, based on the OS type the project is running. This is due to the fact that different OS types use different types of executable resources that might be used. You can find all these configurations in the pom.xml.


## 2.1 Command Line Run

Note: all run commands must be executed from the project folder root ( folder that contains the pom.xml file) <br>

Run command for Junit tests (Backend and API) <br>

```
mvn -Pdefault -DconfigFile=local -DtestSuite=TC002ApiReqresGetTest verify
```

Run command for Serenity + Selenium Tests <br>

```
mvn -Pdefault -DconfigFile=local -DtestSuite=TC002ApiReqresGetTest -Dwebdriver.driver=firefox verify
```

Notes:  <br>

testSuite - property can be either test or testsuite class <br>
configFile - will point the solution on which environment it should run, so all configuration accounts and urls will be stored in the config.properties file  under src/test/resources/config path <br>
webdriver.driver - property will specify on what browser the solution will run (current accepted values are: chrome or firefox) <br>

Also, here webdriver.driver is ignored by the API test since it does not use a browser. You may specify any number of properties (especially when running test suites) and the correct tests will pick up those properties.

Run with a specific config file

```
mvn -Pdefault -DconfigFile=local -DtestSuite=RegressionSuite verify
```

---

### Local run configurations of browser drivers 

We suggest skiping this section if you dont have any specific requirements of running it locally since it makes execution on other OS more brittle if not handled correctly.
The current configuration does not support local run of UI tests since we cannot be sure of the browsers version, operating system and the drivers that match for your specific configuration. We provide an alternative solution by running it through a selenium grid like infrastructure. If you do want to run it locally please download the corresponding drivers for your browser.

Chrome - http://chromedriver.chromium.org/downloads

Firefox - https://github.com/mozilla/geckodriver/releases

IE -  with all the changes now with Edge moving to a chromium based browser we did not include a link.


You will need to place the drivers in the ```src/test/resources/drivers/``` folder and use the naming used in the pom.xml file. Variables with the drivers are passed to the plugin that has the properties:

```
	<webdriver.chrome.driver>${chromeDriverPath}</webdriver.chrome.driver>
	<webdriver.firefox.driver>${firefoxDriverPath}</webdriver.firefox.driver>
	<webdriver.gecko.driver>${firefoxDriverPath}</webdriver.gecko.driver>
```

They get picked up from automatic profiles that get triggered based on your OS in the profiles.

```
	<profile>
		<id>mac</id>
		<activation>
			<os>
				<family>mac</family>
			</os>
		</activation>
		<properties>
			<chromeDriverPath>src/test/resources/drivers/chromedriver_mac</chromeDriverPath>
			<firefoxDriverPath>src/test/resources/drivers/geckodriver_mac</firefoxDriverPath>
		</properties>
	</profile>
	<profile>
		<id>unix</id>
		<activation>
			<os>
				<family>unix</family>
				<name>Linux</name>
			</os>
		</activation>
		<properties>
			<chromeDriverPath>src/test/resources/drivers/chromedriver</chromeDriverPath>
			<firefoxDriverPath>src/test/resources/drivers/geckodriver_linux</firefoxDriverPath>
		</properties>
	</profile>
	<profile>
		<id>windows</id>
		<activation>
			<os>
				<family>windows</family>
			</os>
		</activation>
		<properties>
			<chromeDriverPath>src/test/resources/drivers/chromedriver.exe</chromeDriverPath>
			<firefoxDriverPath>src/test/resources/drivers/geckodriver.exe</firefoxDriverPath>
		</properties>
	</profile>
```

The profiles ensure that the project will execute correctly regardless of the OS the project is running on. We would suggest you pick up drivers for all operating systems you plan to execute this project locally on. 

Match the names used in the profiles and paths (or edit the paths here or call the properties when executing it from the command line).

Also if you download the drivers make sure they have execution rights on your machine.

---


Run against selenium grid ( You will need a selenium Grid or Zalenium setup)

```
mvn -Pdefault -DconfigFile=local -DtestSuite=RegressionSuite -Dwebdriver.driver=firefox verify -Dwebdriver.remote.url=http://localhost:4444/wd/hub
```

Run command for Serenity report generation <br>

```
mvn serenity:aggregate
```

Notes: Will generate a test report that can be accessed under the path ```/target/site/serenity/index.html``` <br>


## 2.2 Container Run

Build the docker image: 

```
docker build -t timage -f Dockerfile.suites .
```

Run the docker image:

```
docker run timage verify -Pdefault
```

Run docker image with volume mount on target folder to save generated artifacts during the test execution:

```
docker run -v /Users/USERNAME/Documents/dockerizer/volumemount:/app/target timage
```

Run docker container with Environment Variable set:

```
docker run -e GAK_KEY="$(cat /Users/USERNAME/.ssh/id_rsa)" timage
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=[TEST_CLASS_NAME] verify
```


## 2.3 API Test

API test implementation relies on the JCurlConnector ```com.monarch.tools.connectors``` to make the actual API calls. jCurlConnector is configurable from the test level by reading configurations from the ```src/test/resources/configs``` folder and its looking for specific test keys: 
```
BASE_URL_REQRES
```
The API test related classes:
```
com.monarch.apps.reqres.user.post
```
We have a RequestCreateUserModel model class that uses Jackson annotations for json/xml decoration.
We also have declared UserPostCreateData data class that will populate the fields of the model class with some specified rules of field data generation.

The test is located in ```com.monarch.runners.tests.reqres.api``` and it uses junit runner class. It is split into a before section that will contain all the data setup actions and a test section that has the API action and the assertions.

In order to run the API individual test you can execute:
```
mvn -Pdefault -DtestSuite=TC002ApiReqresGetTest verify
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=TC002ApiReqresGetTest verify
```


## 2.4 Outlook Email Test

Mail test implementation relies on the OutlookMailConnector located in ```com.monarch.tools.connectors```. Mail Connector is picking up the configurations from the ```src/test/resources/configs``` files. Its looking for the keys:
```
#user
EMAIL_ACCOUNT_USER
EMAIL_ACCOUNT_PASS
#server
EMAIL_SERVER_HOST
EMAIL_SERVER_PORT
EMAIL_STORE_TYPE
#work directory
EMAIL_READ_FOLDER
```
Keys are correlated with the OutlookConnectorSession object that defines the connection session details that are read from the config file. Data is used to perform the initial connection.

The test will grab all the emails received today and will sort out of all the messages that contain in the body, the required string specified in the test. It will then save all the data found in the emails to a text file. In order to map out the extracted data from emails there is an object EmailObjectModel that will contain all the data for extracted emails.

In order to run the email test you can execute:
```
mvn -Pdefault -DtestSuite=TC004CheckEmailTest verify
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=TC004CheckEmailTest verify
```


## 2.5 SFTP Test (using MinIO)

MinIO test implementation relies on the MinioSftpConnector that can upload and read files from the SFTP. MinioSftpConnector is going to setup the connection based on the properties set in the config file. 
```
SFTP_MINIO_SERVER
SFTP_MINIO_PORT
SFTP_MINIO_USER
SFTP_MINIO_SECRET
```
You will need to set these properties in the property file to point to the correct environment.

Note: please see the Environment setup section for the MinIO setup. 

The TC005UploadFileToSftpTest will connect to the MinIO server will upload a config file and check the list of the config files on the server and checks if the file has been uploaded.

You can run the test by running the command:
```
mvn -Pdefault -DtestSuite=TC005UploadFileToSftpTest verify
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=TC005UploadFileToSftpTest verify
```


## 2.6 Selenium UI Tests - JUnit

We have added two flavours of jUnit tests. One is a straight up jUnit test and the other is a Data driven test. The Data Driven test TC003DdtWikiSearchTest uses the TC002DdtWikiSearchTest.csv file (located in ```src/test/resources/features/wiki``` )to provide the test data. Both test implementations will go on wikipedia search for a term and then check that the definition contains some expected string.

Test relies on the ```BASE_URL_UI_WIKI``` variable to be set in the config file in order to point to the correct application.
Test object mappings from web pages are captured in ```Page``` classes that define the object locator and the action related to it (click, input, select and so on). The ```Step``` classes will define groups of actions (performLogin - input user, input password, click login) Step method annotations have the role to create report entries for each method. The tests will never call page classes directly, thus it will always rely on steps to perform actions. 

You can run the test by executing the command:
```
mvn -Pdefault -DtestSuite=TC001WikiSearchAppleTest verify
```

You can run the data driven test by executing the command:
```
mvn -Pdefault -DtestSuite=TC003DdtWikiSearchTest verify
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=TC003DdtWikiSearchTest verify
```

After you run the ```mvn serenity:aggregate``` command, you can find the generated test report in the:
```
target/site/serenity/index.html
```

Note: the selenium UI tests can have a remote webdriver configured for remote run (or run through test infrastructure) or configure the webdriver.driver in order to specify the desired browser.

Note: In order to run the tests locally you need to add webdrivers (chromedriver and geckodriver). You will need to place the drivers in the resources folder give execution rights and point the serenity.properties and the pom properties or profiles to have the paths to the binaries.
Alternatively you can setup the zalenium grid and run it through the infrastructure (locally). For more information please see the SerenityBDD documentation:
http://thucydides.info/docs/serenity-staging/#_running_tests_against_a_selenium_grid_server 


## 2.7 Selenium BDD UI Test (Cucumber)

The BDD scenarios follow on the lines of the selenium jUnit scenarios in that they still use the same Page and Step classes but they have a few extra classes and files. 
The ```Definition``` classes will hold the methods with the Given When Then annotations and will be using the Steps classes to define the actions to be taken.
The ```.feature``` files will be written in plain text and will contain the test scripts.
The ```TC008BddCucumberRunnerTest``` class will be the runner class. It will define on the features variable the folder location of the feature files. the glue variable will tell the framework where the definitions to the scenarios in the feature files are described.

In order to run the BDD feature tests execute:
```
mvn -Pdefault -DtestSuite=TC008BddCucumberRunnerTest verify
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=TC008BddCucumberRunnerTest
```

Note: you may also add tags and run the command with cucumber options (note the tags will need to be added to the feature files in order for this command to work)
```
mvn -Pdefault verify -Dcucumber.options="--tags @debug1 --tags @debug2"
```


## 2.8 Server Creation Test (Vertx)

When you intend to test a certain service, sometimes it might be hard to create a running server with some business login to make the application under test behave in some desired manner. This is where Vertx comes in. We can create lightweight services (client or server) with vertx, since it is a framework build for microservice development.

Test Scenario:

The TC009VertxRunnerTest test class is split into three sections. In the setup we will first do a clean up check for the KillSwitch property. After that we will be setting up Vertx.
In the test body we will be deploying the Server.
In the tearDown we will be looking for the killSwitch flag to be found in one of the requests. When the switch is found, vertx will undeploy all verticles and close.

Class Structure:

TestServerVerticle  - class is deployed in the Vertx Test Runner. The class defines the server configurations:
	- server port to start on
	- exposed endpoints - currently only the ```POST - [host.url]:[host.port]/broker/message```
	- here is where we will add more implementations of endpoint URLs

TestRequestHandler - each exposed endpoint will have a handler for the request. 
	- The handler will get the request from the client, apply the business logic and respond back to the client.
	- Specifically, this handler will generate a nanoId and will send back to the client the initial request on one filed and a nanoID on the second field.

ClientResponseMessage - is used by the TestRequestHandler as a data model for the answer it will return for the request.


Note: you will need to setup jMeter or run the jMeter script in order to test the functionality.

Command Line Run:
```
mvn -Pdefault -DtestSuite=TC009VertxRunnerTest verify
```

Container Run:
```
docker run timage -Pdefault -DtestSuite=TC009VertxRunnerTest 
```

Note: for more information on vertx please see the documentation page: ```https://vertx.io/docs/```

## 2.9 Performance/Functional Test Script (JMeter)

The VertxServerPerformancePlan.jmx file can be found in the ```src/test/resources/jmeter/``` folder.
The script contains 4 test scenarios that are run as part of the setup. Then the test will send a request with a killSwitch. The test will wait for 11 seconds and run the tearDown running the requests again and checking if the connection has been refused, thus validating if the server was taken down.

Note: that in order to write test scripts for jMeter we would recommend installing the jMeter IDE:
```
https://jmeter.apache.org/download_jmeter.cgi
```

Once you have added your jMeter script to the jmeter resource folder, you can trigger the jMeter scripts by running:

```
mvn -Pjmeter install
```

For a quick results/debug view you may run:
```
cat target/jmeter/results/VertxServerPerformancePlan.jtl
```

Docker run:
```
docker run timage -Pjmeter install
```


# 3. Infrastructure Setup


Components that require setup in order to make certain parts of the solution available

## 3.1 Zalenium Grid 


In order to run the UI selenium tests we will need to setup a Selenium GRID. We will be using the Zalenium docker images to provision the infrastructure.

Documentation: https://opensource.zalando.com/zalenium/

Steps to configure:

```
    # Pull docker-selenium
    docker pull elgalu/selenium
    
    # Pull Zalenium
    docker pull dosel/zalenium
    
    # Run it!
    docker run --rm -ti --name zalenium -p 4444:4444 \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v /tmp/videos:/home/seluser/videos \
      --privileged dosel/zalenium start
      
    # Point your tests to http://localhost:4444/wd/hub and run them

    # Stop
    docker stop zalenium
```

Zalenium Management Link: 
http://localhost:4444/grid/console

Zalenium Machine VNC:
http://localhost:4444/grid/admin/live


## 3.2 SFTP MinIO  

In order to have an SFTP test we need to have an external system to talk to. In the absence of any such system under test, we can create our own SFTP store by setting up MinIO on our machines.

Documentation: https://min.io/

```
# Pull docker image
docker pull minio/minio

# Run Minio with user and pass config
docker run  -e "MINIO_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE"  -e "MINIO_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" -p 9000:9000 minio/minio server /data
```

MiniIO Management Link:
http://localhost:9000/minio/login

(use the access and the secret set when running MinIO)




