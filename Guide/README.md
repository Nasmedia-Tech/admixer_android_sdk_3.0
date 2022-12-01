# AdMixer Android SDK V3 Guide

## 문의사항 관련

- 문의사항은 https://partner.admixer.co.kr/ 사이트를 통해 1:1 문의로 연락 주시기 바랍니다.

## 변경 이력

- [변경 이력](HISTORY.md)

## 개발 환경 [공통]

- 최신 버전의 AdMixerSDK 사용을 권장.
- 최신 버전의 Android Studio 권장.
- AdMixer SDK 는 8.0 (Oreo, API Level 26)이상 기기에서 동작.

## 프로젝트 설정 [공통]

## Step 1. Gradle Setting

- 안드로이드 프로젝트 내 최상위 level build.gradle 또는 setting.gradle 확인

```java
# AndroidStudio Arctic Fox 이전 버전에서 생성한 프로젝트의 경우에는 아래 경로 확인.
[build.gradle]
    ...
    allprojects {
        repositories {
            google()
            mavenCentral()
        }
    }
    ...

# AndroidStudio <Arctic Fox> 이후 버전에서 생성한 프로젝트의 경우에는 아래 경로 확인.
[settings.gradle]
    ...
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
        }
    }
    ...
```

- 안드로이드 프로젝트 내 app level build.gradle 에 'dependencies' 추가

```java
    dependencies {
        ...
        implementation 'io.github.nasmedia-tech:admixersdk:3.0.3'
        ...
    }
```

## Step 2. AdMixer SDK Initialize

- AdMixer 객체를 통해 반드시 1회 초기화 호출이 필요합니다.

```java
[YourApplication]
    public class YourApplication extends android.app.Application {

        public static String MEDIA_KEY = "AdMixer 플랫폼에서 발급받은 미디어 키";
        public static String ADUNIT_ID_BANNER = "AdMixer 플랫폼에서 발급받은 배너 ADUNIT_ID";
        public static String ADUNIT_ID_INTERSTITIAL_BANNER = "AdMixer 플랫폼에서 발급받은 전면 배너 ADUNIT_ID";
        public static String ADUNIT_ID_NATIVE = "AdMixer 플랫폼에서 발급받은 네이티브 ADUNIT_ID";

        public static ArrayList<String> adUnits = new ArrayList<>(
                Arrays.asList(ADUNIT_ID_BANNER, ADUNIT_ID_INTERSTITIAL_BANNER, ADUNIT_ID_NATIVE)
        );

        @Override
        public void onCreate() {
            super.onCreate();
            // 로그 레벨 설정
            AdMixerLog.setLogLevel(AdMixerLog.LogLevel.VERBOSE);

            // 특정 브라우저 지정
            //AdMixer.setBrowser(new ArrayList<>(Arrays.asList("com.sec.android.app.sbrowser", "com.android.chrome")));

            // COPPA(아동보호법) 관련 항목 설정값 - 선택사항
            // AdMixer.setTagForChildDirectedTreatment(AdMixer.AX_TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE);

            // AdMixer 초기화를 위해 반드시 광고 호출 전에 앱에서 1회 호출해주셔야 합니다.
            // adunits 파라미터는 앱 내에서 사용할 모든 adunit_id 를 배열 형태로 넘겨 주셔야 합니다.
            // XXXX_ADUNIT_ID 는 Admixer 사이트 미디어 > 미디어관리 > 미디어 등록에서 발급받은 Adunit ID 입니다.
            AdMixer.getInstance().initialize(this, MEDIA_KEY, adUnits);
        }
    }
```

## Step 3. Proguard 설정

- 난독화를 위해 proguard 를 설정하신 경우에는 앱 실행 시 광고가 나오지 않을 수 있습니다. 이를 위해서는 다음의 설정 내용을 proguard 파일에 추가해 주시면
  됩니다.

```java
[xxxxxx-yyyyy.pro]

# AdMixer Setting
-keep class com.nasmedia.admixer.** { *; }

```

## Step 4. 광고 추가하기

앱에 광고를 노출하기 위해서는 AdFormat 에 따라 앱 화면에 광고를 추가하는 과정이 필요합니다.<br/>
[AdMixer 플랫폼](https://partner.admixer.co.kr/)에서 설정한 AdFormat 에 따라 아래 문서를 참고하시기 바랍니다.

* [배너 광고 시작하기](BANNERAD.md)
* [네이티브 광고 시작하기](NATIVEAD.md)

## Step 5. 자주하는 질문 (Q&A)
* V3 부터는 AdMixer SDK 에서 더 이상 미디에이션 서비스를 제공하지 않습니다.

* 문제 확인을 위한 로그는 없나요?
    - AdMixerLog.setLogLevel(AdMixerLog.LogLevel.VERBOSE);
    - 위의 코드를 넣으시면 LogCat 에 상세한 로그를 확인하실 수 있습니다. 문제 발생 시 해당 로그를 전달해 주시면 좀 더 정확한 원인 파악에 도움이 됩니다.

* 하나의 App 에 복수 개의 Media Key 를 적용해도 되나요?
    - 한 개의 App 에는 한 개의 Media Key 만 적용하셔야 합니다.

* 동일한 AdUnit ID로 여러 개의 광고객체를 생성해도 되나요?
    - 한 개의 AdUnit ID는 한 개의 광고객체에서만 사용가능합니다.

* 광고가 나오지 않습니다.
    - 먼저 Android Studio 의 Logcat 에 표시되는 로그를 확인해 주시기 바랍니다.
    - AdMixer 사이트에서 광고 키 설정에 문제가 없는지 확인 바랍니다.
    - AdMixer 객체를 통해 초기화 호출을 해주셨는지 확인 부탁드립니다. 초기화 시에 설정한 Media Key 및 AdUnit ID 배열과 일치하는 정보를 가진 광고객체만
      정상적으로 동작합니다.
    - AdUnit 생성 시에 입력한 fullscreen 정보에 따라서 사용가능한 광고객체가 다릅니다. banner 객체는 fullscreen 이 off 일 때,
      interstitial 객체는 fullscreen 이 on 일 때 각각 사용할 수 있습니다.
    - Native 객체는 탬플릿으로 제공되는 layout.xml 을 작성해야 하며, 이를 전달하지 않을 시, 광고 설정이 되지 않습니다.

* AdUnit 에 설정한 사이즈와 다른 사이즈의 광고가 노출됩니다
    - AdUnit 에 설정한 사이즈값은 AdMixer 는 내부적으로 사이즈가 보장되지만, 광고의 유형에 따라 다르게 노출 될 수 있습니다.

* 한 App 내에서 많은 AdUnit 을 사용해도 되나요?
    - 사용 가능한 AdUnit 수에 제한은 없지만, 광고 객체를 설정하고 로딩하는데에 많은 메모리가 할당되기 때문에 앱 성능을 위해서 많은 광고객체 호출은 지양하시는 것이
      좋습니다.

* 전면 광고가 자주 나오지 않습니다.
    - 전면 광고의 경우 배너 광고보다 광고 물량이 적어 광고가 나오지 않을 확률도 그 만큼 커집니다.

* 네이티브 광고가 자주 나오지 않습니다.
    - 네이티브 광고의 경우 배너 광고보다 광고 물량이 적어 광고가 나오지 않을 확률도 그 만큼 커집니다.

* Banner 광고 좌우 여백 부분 배경색을 바꿀 수 있나요?
    - AdView 의 배경 혹은 AdView 의 부모 View 의 배경색을 설정하시면 됩니다.
