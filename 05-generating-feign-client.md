# Generating Client Stub using Openapi generator

- openapi spec을 통해서 생성된 server skeleton 을 생성하고, 생성된 인터페이스를 생성했었다. 
- 이는 swagger을 통해서 관련 문서와 테스트를 수행할 수 있다. 
- 동일한 openapi spec을 이용하여 클라이언트도 다양한 언어로 generate가 가능하다. 
- 여기서는 생성된 Feign Client 을 통해서 Server의 REST API를 호출할 수 있다. 

## Feign Client 소개

- Feign 은 서버의 REST API를 클라이언트의 Service 메소드를 호출하듯이 사용할 수 있는 인터페이스를 제공한다. 
- Spring Cloud 에 탑재되어 REST API를 호출하여 Boilerplate 코드를 줄여주고, 신뢰성 있고, 일관된 서버 API를 호출할 수 있는 도구이다. 

## 의존성 설정하기 

- openapi generator를 구성하기 위해서 다음 의존성 설정을 수행하자. 

### 플러그인 설정학디. 

```groovy
plugins {
	id 'java'
	id 'org.springframework.boot' version '2.7.5'
	id 'io.spring.dependency-management' version '1.1.0'
	id "org.openapi.generator" version "6.5.0"
}

apply plugin: 'org.openapi.generator'
```

- springboot 에 알맞은 openapi generator 플러그린을 설정한다. 

### Spring Cloud 설정하기. 

- spring cloud내부에 feign client를 지원하며, 다음과 같이 spring client 의존성 관리자를 설정하였다. 

```groovy
ext {
	set('springCloudVersion', "2021.0.4")
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
	imports {
		mavenBom "io.awspring.cloud:spring-cloud-aws-dependencies:2.4.4"
	}
}
```

### 의존성 추가하기. 

- 이제 openapi generate를 위해서 의존성을 추가한다. 

```groovy
	implementation 'org.openapitools:openapi-generator:6.5.0'
	implementation 'org.openapitools:jackson-databind-nullable:0.2.6'
	implementation 'javax.validation:validation-api:2.0.1.Final'
	implementation 'javax.annotation:javax.annotation-api:1.3.2'
	implementation 'javax.servlet:javax.servlet-api:4.0.1'

	implementation "org.openapitools:openapi-generator-gradle-plugin:6.5.0"
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign:4.0.3'
	implementation 'javax.servlet:javax.servlet-api:4.0.1'

	implementation 'io.swagger.core.v3:swagger-annotations-jakarta:2.2.9'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	annotationProcessor "org.springframework.boot:spring-boot-configuration-processor"

```

- openapi-generator: 제너레이터를 설정한다. 
- jackson-databind-nullable: json --> object 변환을 위해 설정한다. 
- validation-api: 데이터 검증용 의존성 추가 
- javax.annotation-api: swagger 의존성 추가 
- javax.servlet-api: 서블릿 처리를 위해 설정
- openapi-generator-gradle-plugin: gradle 플러그인용 의존성 
- spring-cloud-starter-openfeign: 오픈페인 라이브러리, 이 도구를 이용해서 우리는 인터페이스 정의만으로 REST Call을 수행할 수 있다. 
- spring-boot-configuration-processor: 스프링 부트내 객체가 자동 생성되도록 필요한 의존성 라이브러리 

### Generator 설정하기. 

- 이제 Generator 를 다음과 같이 설정하자. 

```groovy
openApiGenerate {
	inputSpec = "$rootDir/src/main/resources/petstore.yaml";
	outputDir = "${buildDir}/generated-sources"

	generatorName = "spring"

	groupId = group

	apiPackage = "com.schooldevops.openapi.feigndemo.api"
	invokerPackage = "com.schooldevops.openapi.feigndemo.client.invoker"
	modelPackage = "com.schooldevops.openapi.feigndemo.client.model"

	configOptions = [
			library        : "spring-cloud",
			dateLibrary    : "java8",
			artifactId     : "ingestion-gateway-client",
			artifactVersion: project.version.toString(),
	]
}
```

- inputSpec: openapi spec이 위치한 디렉토리를 설정한다. 
- outputDir: 생성된 코드가 출력될 위치를 지정한다. 
- generatorName: 스프링 기반 생성기를 사용한다. openFeign은 스프링기반 의존성이므로 spring으로 지정한다. 
- apiPackage: 생성될 패키지 위치를 지정한다. 
- invokerPackage: feign을 생성할 클라이언트 호출을 담당하는 코드를 생성한다. 
- modelPackage: 전달되는 DTO 모델이 저장되는 패키지이다. 
- configOptions: 생성될 파일에 대한 추가 설정을 지정한다. 
  - library: "spring-cloud" - openFiegn 코드는 spring-cloud에 속한다. 
  - dateLibrary: java8 이상부터 LocalDateTime을 이용할 수 있다. 
  - artifactId: 아티팩트 아이디를 지정하였다. 이는 injestion-gateway-client로 지정하자. 
  - artifactVersion: 스프링 버젼을 지정한다. 

### 컴파일 소스 위치 설정하기. 

- 코드를 생성하였다면 이제 생성된 코드를 컴파일 해야한다. 
- 이를 위해 설정을 다음과 같이 수행하자. 

```groovy
sourceSets {
    main {
        java {
            srcDirs = ['src/main/java', 'build/generated-sources/src/main/java']
        }
    }
}
```

- src/main/java 는 비즈니스 로직이 존재하는 코드이다. 
- bild/generated-sources/src/main 은 openapi generator가 생성한 Feign client 코드를 컴파일할 소스파일 위치이다. 

### 환경변수 설정 

- application.property에 다음과 같이 작업하자. 

```src
server.port=9999
commonAlarm.url=http://localhost:8080

```

- server.port: 서버 포트는 9999 로 설정하였다. 
- XXX.url: 우리가 생성한 openapi 의 대상 서버 url을 지정한다. openFeign은 기본적으로 https로 호출한다. 아직 개발단계라면 서버 엔드포인트를 직접 잡아줄 수 있다. 

### 클라이언트 코드 생성하기. 

- PushService.java 파일을 하나 생성하고, 다음과 같이 OpenFeign 코드를 사용할 서비스코드를 생성하자. 

```java
package com.schooldevops.openapi.feigndemo.services;

import com.schooldevops.openapi.feigndemo.api.CommonAlarmApiClient;
import com.schooldevops.openapi.feigndemo.client.model.AppPush;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PushService {
    @Autowired
    CommonAlarmApiClient apiClient;

    public ResponseEntity<AppPush> getAppPush(String id) {
        try {
            return apiClient.getAppPush(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

- 클라이언트 코드가 자동으로 생성한 CommonAlarmApiClient 를 Autowired 하자. 
- CommonmAlarmApiClient는 openapi generator가 생성한 Feign Client 인터페이스이다. 
- apiClient.getAppPush 라는 메소드를 호출하여, 서버에 푸시 정보를 조회하는 메소드를 호출할 수 있다. 

### 컨트롤러 생성하기. 

- 단순히 컨트롤러는 서비스를 호출하여 openFeign 클라이언트 코드를 호출할 수 있다. 

```java
package com.schooldevops.openapi.feigndemo.controller;

import com.schooldevops.openapi.feigndemo.services.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    PushService pushService;

    @GetMapping("/test")
    public ResponseEntity<?> getTest() {
        return pushService.getAppPush("testId");
    }
}
```

- /test 엔드포인트를 통해서 PushService 서비스를 autowired 하였다. 
- 이후 pushService.getAppPush 와 같이 우리 서비스를 호출하면 서비스가 자동으로 서버의 REST endpoint를 호출하게 된다. 

### 테스트하기

- 우선 서버부터 실행해야한다. 이전에 생성한 server skeleton을 먼저 실행하자. 
- 서버는 [http://localhost:8080](http://localhost:8080) 을 엔드포인트 호스트로 호출할 대상서버이다. 
- FeignClient를 구현한 클라이언트 서버를 실행하자. 
- 클라이언트 엔드포인트로 호출하면 ```http://localhost:9999/test ```로 호출하면 client --> server 로 호출하게 되고 결과를 확인할 수 있게 된다. 

## WrapUp

- 지금까지 우리는 OpenApi spec을 이용하여 FeignClient 인터페이스를 생성하였다.
- FeignClient 인터페이스는 Autowired만 수행하면, 자신의 서비스 메소드를 호출하는 방법과 같이, REST endpoint를 호출할 수 있다. 
- 기본저긍로 feign client는 https 호출을 수행한다. 만약 도메인과 https 설정을 하지 않았다면, 설정파일을 이전에 보았듯이 수정하여 호출하면 된다. 
- 중요한 포인트는 openapi spec 을 한번 작성하면, 서버/클라이언트 코드를 모두 작성해준다. 
- 개발자는 오직 비즈니스 로직에 신경을 쓰면 된다. 
- 또한 api spec이 바뀌면, 동일한 스펙을 사용하여 기존 코드의 변경을 취소화 하고 서버/클라이언트 코드를 다시 생성하면 되므로 생산성이 향상된다. 
