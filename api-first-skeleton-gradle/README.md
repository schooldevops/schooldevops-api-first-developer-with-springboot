# Gradle이용한 REST API 개발

- 참고.
    - 응답이 application/xml application/json 을 2개 동시에 설정한경우 다음과 같은 오류 발생.

```json
[http-nio-9099-exec-1] WARN org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver - Resolved [org.springframework.http.converter.HttpMessageNotWritableException: No converter for [class com.schooldevops.apifirst.openapi.domain.Pet] with preset Content-Type 'null']
```

- 해결방법:
    - 오버라이드할때 @RequestMapping을 작성해서 명확히 응답코드를 반환할 수 있도록 한다.

```java
@RestController
public class PetStoreControllerImpl implements PetApi {

    @Override
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/pet/{petId}",
            produces = { "application/json" }
    )
    public ResponseEntity<Pet> getPetById(Long petId) {
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("MyDog");
        Category category = new Category();
        category.setId(1001L);
        category.name("Dog");
        pet.setCategory(category);
        pet.setStatus(Pet.StatusEnum.AVAILABLE);
        pet.setPhotoUrls(List.of("url://http"));

        return ResponseEntity.ok(pet);
    }
}
```