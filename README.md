# Mission-Logistics

# 5주차 미션 - 운송 I (배송)

## Mission Senario

봉화로 온 박호건씨는 이 지역에서는 당일 배송이 안된다는 사실에 충격을 받는다. 이에 퀵배송을 해주는 회사를 구상하고 있다. 그 첫 단계로 배송하는데 걸리는 시간을 예측해보고자 한다. 대략적인 장소 및 거리 정보를 수집한 상태에서 특정 지역에서 다른 지역으로 이동하는데 걸리는 시간을 예측하는 프로그램을 개발하고자 한다.

## Mission Overview

등록된 목적지, 등록된 경로만으로 이동할 수 있으며 경유가 가능하다. 출발지와 도착지를 입력하면 예상 소요시간을 출력한다.

## Mission Goals

- 학습한 내용을 기반으로 유지 보수 및 확장 가능한 코드를 작성한다.
- 알고리즘을 적용하는 방법에 대해 고민한다.

## Study Documents

- https://yozm.wishket.com/magazine/detail/1748/
- https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4Interface%EC%9D%98-%EC%A0%95%EC%84%9D-%ED%83%84%ED%83%84%ED%95%98%EA%B2%8C-%EA%B0%9C%EB%85%90-%EC%A0%95%EB%A6%AC
- https://0soo.tistory.com/62
- https://velog.io/@red-sprout/Java-enum%EC%9D%80-%EC%99%9C-%EC%93%B0%EB%8A%94%EA%B1%B8%EA%B9%8C-feat.-%EC%9A%B0%EC%95%84%ED%95%9C%ED%98%95%EC%A0%9C%EB%93%A4-%EA%B8%B0%EC%88%A0%EB%B8%94%EB%A1%9C%EA%B7%B8
- https://devuna.tistory.com/32
- https://calculatorlib.com/ko/latlong-distance-calculator
- https://fruitdev.tistory.com/189

## Mission Guide

1. [미션 저장소](https://github.com/allrounder-backend/mission-logistics)를 Fork 한다.
2. Folk 한 레포의 main 브랜치에서 미션을 구현한다.
3. 구현이 끝난 후 미션 저장소에 PR을 보낸다.

## Feature Requirements Details

장소, 위치, 경로 정보는 csv 파일 형태로 제공된다.

- 장소 정보 : `place.csv` - id (식별자), name (장소 이름), address (세부 주소)
- 위치 정보 : `position.csv`  - place_id (place의 id), lat (Latitude, 위도), lng (Longitude, 경도)
    - 경로 정보 : `route.csv`  - place_id_1 (place의 id), place_id_2 (place의 id), time (이동 시간)
        - 방향은 고려하지 않는다. 이동 시간은 `h:nn` 형식이다.

### 이동 시간 예측

출발 및 도착 장소를 입력하면 그에 따라 이동 시간을 예측한다.

기본에서는 위도와 경도 정보를 기반으로 이동 시간을 예측한다. 60km/h의 속력에 직선적으로 이동한다고 가정하여 시간을 예상한다.

```bash
출발지를 입력해주세요. 
> 봉화군청

도착지를 입력해주세요. 
> 숭실대학교 정보과학관

이동 시간은 2시간 51분으로 예측됩니다. 
```

※ `h:nn` 의 형식으로 시간을 예측하며 소수점의 처리 방식은 자율적으로 결정한다. 시간은 근사가 가능하다.

(응용) `place.csv`, `position.csv`, `route.csv` 에 추가적인 데이터를 입력할 수 있다. 이 때 다음과 같은 상황을 고려하여 예외를 처리하고 단위 테스트를 진행한다.

- (기본 로직을 삭제하지 않았다면) 장소 정보와 위치 정보가 1:1로 매핑되는지
- 갈 수 없는 (고립된) route가 있는지, 그러한 이동시간 예측 요청을 받았는지
- 중복되는 route가 있는지

각 예외를 단순히 처리하는 것을 넘어 문제를 진단할 수 있도록 예외를 발생시킨다.

(심화) route에 있는 경로 정보를 바탕으로 이동 시간을 계산한다. route.csv는 쌍방향 정보이며 각 경로를 경유해서 이동할 수 있다.

```bash
출발지를 입력해주세요. 
> 봉화군청

도착지를 입력해주세요. 
> 숭실대학교 정보과학관

이동 시간은 3시간 10분으로 예측됩니다. 
```

### Exception Handling

다음과 같은 상황에서는 예외를 발생시켜 프로그램을 종료한다.

- 존재하지 않는 장소를 입력한 경우
- …

```bash
출발지를 입력해주세요. 
> 숭실대학교 정보캠퍼스

IllegalArgumentException : "숭실대학교 정보캠퍼스"을(를) 이름으로 갖는 장소는 존재하지 않습니다.
```

기능 명세 및 테스트케이스에 정의되지 않은 예외적 상황은 자의적으로 판단하되 예외를 발생시킬 경우 그 사유를 명확하게 적시하며 프로그램을 종료시킨다.

## Programming Requirements Details

- 개발환경은 JDK 21을 사용한다.
- 프로그램 실행의 시작점은 `Application`의 `main()`이다.
- `build.gradle` 파일을 변경할 수 없고, 외부 라이브러리를 사용하지 않는다.
- 프로그램 종료 시 `System.exit()`를 호출하지 않는다.
- 구글 스타일 가이드를 준수하며 코드를 작성한다.

  https://google.github.io/styleguide/javaguide.html, 한국어 번역 https://newwisdom.tistory.com/m/96

    - 4.2 블럭 들여쓰기: +4 스페이스
        - 새 블록 또는 블록과 유사한 구조(block-like construct)가 열릴 때마다 들여쓰기가 네 칸씩 증가합니다. 블록이 끝나면 들여쓰기는 이전 들여쓰기 단계로 돌아갑니다. 들여쓰기 단계는 블록 전체의 코드와
          주석 모두에 적용됩니다.
    - 4.4 열 제한: 120
        - Java 코드의 열 제한은 120자입니다. "문자"는 유니코드 코드 포인트를 의미합니다.
    - 4.5.2 들여쓰기 지속은 최소 +8 스페이스
        - 줄 바꿈 시 그 다음 줄은 원래 줄에서 +8 이상 들여씁니다.
    - 4.6.1 수직 빈 줄
        - ...
          빈 줄은 가독성을 향상시키기 위해서라면 어디든(예를 들면 논리적으로 코드를 구분하기 위해 문장 사이) 사용 될 수 있습니다. 클래스의 첫 번째 멤버나 초기화(initializer) 또는 마지막 멤버 또는 초기화(
          initializer) 뒤의 빈 줄은 권장되지도 비권장하지도 않습니다.

          > 클래스의 첫 번째 멤버나 초기화(initializer) 앞에 있는 빈줄을 강제하지 않습니다.
          >

          ...

          변수명, 함수명도 컨벤션이다.

- 입력은 `api` 의 `Console.readline()` 을 활용한다.
- 테스트는 `api` 의 `TestEnvironment` 를 활용한다.
    - `TestEnvironment` 의 `runMain` 메서드에 실행할 내용을 구현한다.
    - `@Test` 의 `run()` 의 파라미터로 mocking할 입력들을 넣는다.
    - `@Test` 안에 있는 `output()` 은 출력 결과를 낚아챈다.
