# Booklay front server!
🤩


# html 템플릿 사용법

### 1. 컨트롤러에서 직접적으로 사용할 html 생성
[Sample Page](src/main/resources/templates/samplePage.html)

### 2. notice 폴더를 참고하여 사용할 layout 템플릿 선택
[Default Layout](src/main/resources/templates/thymeleaf/layout/default.html) </br>
[Admin Layout](src/main/resources/templates/thymeleaf/layout/admin.html) </br>
[Profile Layout](src/main/resources/templates/thymeleaf/layout/profile.html)

### 3. 어플리케이션을 실행시키고 해당하는 URL 에 접속하고, 레이아웃이 제대로 적용되었는지 확인하기

### 4. 이미 만들어진 레이아웃 템플릿에서 더 필요한 프레그먼트가 있다면 [fragment template](src/main/resources/notice/fragment/defaultFragmentsTemplate.html) 참고하여 만들기

### 5. 프레그먼트 적용시, 적용할 위치에 th:block 태그에서 th:replace 필드 사용, 레이아웃 참조

      <th:block th:replace="{프레그먼트 파일 경로} :: {프레그먼트 이름}">
      </th:block>

### 6. 버튼은 하단의 클래스 사용
    <button class="btn btn-outline-primary" > 버튼 </button>
