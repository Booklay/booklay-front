# Booklay front server!
๐คฉ


# html ํํ๋ฆฟ ์ฌ์ฉ๋ฒ

### 1. ์ปจํธ๋กค๋ฌ์์ ์ง์ ์ ์ผ๋ก ์ฌ์ฉํ  html ์์ฑ
[Sample Page](src/main/resources/templates/samplePage.html)

### 2. notice ํด๋๋ฅผ ์ฐธ๊ณ ํ์ฌ ์ฌ์ฉํ  layout ํํ๋ฆฟ ์ ํ
[Default Layout](src/main/resources/templates/thymeleaf/layout/default.html) </br>
[Admin Layout](src/main/resources/templates/thymeleaf/layout/admin.html) </br>
[Profile Layout](src/main/resources/templates/thymeleaf/layout/profile.html)

### 3. ์ดํ๋ฆฌ์ผ์ด์์ ์คํ์ํค๊ณ  ํด๋นํ๋ URL ์ ์ ์ํ๊ณ , ๋ ์ด์์์ด ์ ๋๋ก ์ ์ฉ๋์๋์ง ํ์ธํ๊ธฐ

### 4. ์ด๋ฏธ ๋ง๋ค์ด์ง ๋ ์ด์์ ํํ๋ฆฟ์์ ๋ ํ์ํ ํ๋ ๊ทธ๋จผํธ๊ฐ ์๋ค๋ฉด [fragment template](src/main/resources/notice/fragment/defaultFragmentsTemplate.html) ์ฐธ๊ณ ํ์ฌ ๋ง๋ค๊ธฐ

### 5. ํ๋ ๊ทธ๋จผํธ ์ ์ฉ์, ์ ์ฉํ  ์์น์ th:block ํ๊ทธ์์ th:replace ํ๋ ์ฌ์ฉ, ๋ ์ด์์ ์ฐธ์กฐ

      <th:block th:replace="{ํ๋ ๊ทธ๋จผํธ ํ์ผ ๊ฒฝ๋ก} :: {ํ๋ ๊ทธ๋จผํธ ์ด๋ฆ}">
      </th:block>

### 6. ๋ฒํผ์ ํ๋จ์ ํด๋์ค ์ฌ์ฉ
    <button class="btn btn-outline-primary" > ๋ฒํผ </button>
