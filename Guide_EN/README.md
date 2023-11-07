# AdMixer Android SDK V3 Guide

## Related to Inquiries

- If you have any questions, please contact us at ‘Support > Contact’ through the https://partner.admixer.co.kr/ site.

## History

- [History](HISTORY.md)

## App prerequisites

- Use the latest version of AdMixer SDK.
- Use the latest version of Android Studio.
- AdMixer SDK works on 5.0 (LOLLIPOP, API Level 21) and later devices..

## Project Settings

## Step 1. Gradle Setting

- Check the top level build.gradle or setting.gradle in the Android project

```java
# check the path below, for projects created with Android Studio Arctic Fox or earlier.
[build.gradle]
    ...
    allprojects {
        repositories {
            google()
            mavenCentral()
        }
    }
    ...

# check the path below, for projects created in Android Studio Arctic Fox later.
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

- Add 'dependencies' to app level build.gradle in Android project

```java
    dependencies {
        ...
        implementation 'io.github.nasmedia-tech:admixersdk:3.0.10'
        ...
    }
```

## Step 2. AdMixer SDK Initialize

- One initialization call must be made through the AdMixer object.

```java
[YourApplication]
    public class YourApplication extends android.app.Application {

        public static String MEDIA_KEY = "media key issued by the AdMixer platform";
        public static String ADUNIT_ID_BANNER = "banner adunit id issued by the AdMixer platform";
        public static String ADUNIT_ID_INTERSTITIAL_BANNER = "interstitial banner adunit id issued by the AdMixer platform";
        public static String ADUNIT_ID_NATIVE = "native adunit id issued by the AdMixer platform";

        public static ArrayList<String> adUnits = new ArrayList<>(
                Arrays.asList(ADUNIT_ID_BANNER, ADUNIT_ID_INTERSTITIAL_BANNER, ADUNIT_ID_NATIVE)
        );

        @Override
        public void onCreate() {
            super.onCreate();
            // setting log level
            AdMixerLog.setLogLevel(AdMixerLog.LogLevel.VERBOSE);

            // specifying a specific browser
            //AdMixer.setBrowser(new ArrayList<>(Arrays.asList("com.sec.android.app.sbrowser", "com.android.chrome")));

            // COPPA setting - optional
            // AdMixer.setTagForChildDirectedTreatment(AdMixer.AX_TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE);

            // To initialize AdMixer, you must call the app once before calling the ad.
            // The adunits parameter requires that all adunits_id to be used in the app be handed over in an array form.
            // XXXX_ADUNIT_ID is the Adunit ID issued by ‘Media > My Media > Add to media’ on the Admixer site.
            AdMixer.getInstance().initialize(this, MEDIA_KEY, adUnits);
        }
    }
```

## Step 3. Proguard setting

- If you set up the proguard for obfuscation, the ad may not appear when you open the app. In this case, you can add the following settings to the proguard file

```java
[xxxxxx-yyyyy.pro]

# AdMixer Setting
-keep class com.nasmedia.admixer.** { *; }

```

## Step 4. Adding ad

To show ad to an app, you need to add an ad to the app screen according to Ad format.
Please refer to the following documentation, according to the AdFormat set by [AdMixer Platform] (https://partner.admixer.co.kr/)..

* [Banner](BANNERAD.md)
* [Native](NATIVEAD.md)

## Step 5. Q&A
* From Version 3, AdMixer SDK no longer provides mediation services.

* Is there any log to check the problem?
    - AdMixerLog.setLogLevel(AdMixerLog.LogLevel.VERBOSE);
    - If you insert the code above, you can check the detailed log in LogCat. In the event of a problem, please forward the log to help us determine the cause more accurately.

* Can I apply multiple media keys to one app?
    - You must apply only one Media Key to one App.

* Can I create multiple advertising objects with the same AdUnit ID?
    - One AdUnit ID is only available on one ad object.

* I cannot show ads.
    - First, please check the log displayed in Logcat in Android Studio.
    - Please check the AdMixer site to see if there is any problem with setting the key value. : media key, adunit id,etc.
    - Please check if you made an initialization call through the AdMixer object. Only advertising objects with information that matches the Media Key and AdUnit ID arrays that you set at initialization work normally.
    - The available advertising objects are different depending on the fullscreen information entered when creating AdUnit. Banner objects can be used when fullscreen is off and interactive objects when fullscreen is on.
    - Native object must create layout.xml supplied as a template, otherwise it will not be expose.

* The ads in different sizes than the ones I set
    - The size value set for AdUnit is internally guaranteed for AdMixer, but it may be exposed differently depending on the type of ad.

* Can I use many AdUnits within one app?
    - There is no limit to the number of AdUnits available.
      However, since a lot of memory is allocated to set up and load ad objects, it is recommended that you avoid calling many ad objects for app performance.

* It’s hard to get a fullscreen ad.
    - The amount of fullscreen ads is less than that of banner ads, so the probability that the advertisement will not appear increases.

* It’s hard to get Native ad.
    - The amount of Native ads is less than that of banner ads, so the probability that the advertisement will not appear increases.

* Can I change the background color of the left and right margins of Banner ad?
    - Set the background of AdView or the background color of AdView's parent View.
