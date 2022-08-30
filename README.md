<br>
<br>
<p align="center">
<img src="https://user-images.githubusercontent.com/84304802/187101391-46357a6f-cd89-47ac-9f27-75f76c7b7dd9.png" alt="git-flow" width=450px height=450px>
</p>
<br>

# 🗺️ B-Map: 장애인 전용 주차구역 안내 서비스
### 2022 서울시 IoT 도시 공공데이터 해커톤 - 🏆 우수상 수상작

<br>

### "💁 비어있으면 주차해도 되는 것 아니에요?”

최근 장애인 주차구역의 필요성을 무시하거나 소홀히 하는 점이 사회적 이슈로 떠오르고 있습니다.
장애인 전용주차구역은, 장애인이 보다 편리하게 이동할 수 있도록 법적으로 지정해 둔 구역입니다.

보건복지부 자료에 따르면, 장애인은 외출 시 자가용을 가장 많이 이용한다고 답했습니다. 또한 지체 장애인의 70%가 운전으로 이동한다고 답했습니다.
이렇듯 많은 장애인분들이 자가용을 주요 이동수단으로 이용하지만 전용 주차 구역은 불법 주차 때문에 효율적으로 이용되지 못하고 있습니다.
실제로 4년 동안 불법 주차 위반 건수는 4배 증가했고, 중복 위반은 7배 증가한 것을 확인할 수 있었습니다.
이러한 문제를 해결하기 위해 서울시는 이미 IoT장비를이용한 서비스를 운영하고 있는데요. 문제점은 이 시스템은 단속만을 위한 시스템이라는 것입니다. 불법 차량이 진입했을 때 빨간등을 표시하여 단속하는 것인데, 정작 사용자는 직접 전용 주차구역에 가지 않으면 빈자리 여부를 알 수 없고, 결국 일반 구역을 이용해야 합니다.
장애인 분들에겐 신고가 아닌 정말로 자신들의 이동권을 보장받을 수 있도록 돕는 서비스가 필요했습니다.

그래서 IoT를 활용한 정보와 장애인의 신체를 고려한 UXUI 로 만들어진 장애인 주차구역 안내 서비스가 바로 **B-Map** 입니다.

<br>


## 📋 컨벤션

### # 커밋 메시지

**⚙️ 메시지 구조**

```
Type: 커밋 내용
```

**⚙️ Type**

```
| 타입 종류 | 설명                                 |
| --------- | ------------------------------------ |
| feat      | 새로운 기능에 대한 커밋              |
| fix       | 수정에 대한 커밋                     |
| bug       | 버그에 대한 커밋                     |
| build     | 빌드 관련 파일 수정에 대한 커밋      |
| ci/cd     | 배포 커밋                            |
| docs      | 문서 수정에 대한 커밋                |
| style     | 코드 스타일 혹은 포맷 등에 관한 커밋 |
| refactor  | 코드 리팩토링에 대한 커밋            |
| test      | 테스트 코드 수정에 대한 커밋         |
```

<br>

<br>

## 🗺️서비스 플로우

<p align="center">
  <img src="https://user-images.githubusercontent.com/84304802/187105970-f8147dd1-396a-47a3-8a48-aa8775041881.png" alt="아키텍처">
</p>


<br>

<br>

## ⚒️ 기술 스택

<br>

⚙️ **Language**

Kotlin, Java

⚙️ **Architecture**

MVP + Clean Architecture

⚙️ **Library**

RxKotlin, Coroutine, Retrofit, Room, Dagger Hilt, Glide, Lottie

<br>

<br>

## 📱 InApp 소개
<br>
<p align="center" display="inline">
  
  ![스플래시](https://user-images.githubusercontent.com/56534241/187340859-eda5cda3-cbb7-4881-bdf8-6d067244dc44.png)
  ![검색_완료](https://user-images.githubusercontent.com/56534241/187340865-85a2749a-0197-4473-8f5c-4fdcae954a18.png)
  ![메인](https://user-images.githubusercontent.com/56534241/187340867-a8e00da1-b250-4712-87a1-ff3b51e97514.png)
  ![음성검색](https://user-images.githubusercontent.com/56534241/187340872-236b4963-96e7-4b15-90a6-971ac5afd985.png)
  ![신고하기_신고유형](https://user-images.githubusercontent.com/56534241/187340873-b5291975-2c90-4be2-a2d7-d0d72b25eeb9.png)
  ![주차장상세_full](https://user-images.githubusercontent.com/56534241/187340875-371fb4aa-97a0-4b9c-a277-748e64da645b.png)
</p><br><br>

## 👥 파트 및 개발 계획

### **[ 팀원 & 파트 ]**

#### 🖥️ 안드로이드 

- 최승호 [Github](https://github.com/tmdgh1592)
- 윤준서 [Github](https://github.com/lowapple)

#### 🗄️ 백엔드 

- 최승준 [Github](https://github.com/PgmJun)

#### 🎨 디자인

- 윤수정 [Email] abcabcbabc@gmail.com

<br>

**[ 개발 기간 ]** 2022/08/23 ~ 2022/08/27

<br>

📑**Notion**: https://lucky-crater-a5b.notion.site/Unithon-8th-41966fe84a2948d6900e80d93ffb8c51

📑**참가후기**: https://itstory1592.tistory.com/77

<br>
