# Battleship Game for Android

#### Written By Meital Kozhidov
##### The Open University, Israel
##### April 2021

## Features
- 1 vs 1 remote classic 5X5 Battleship game 
- login/sign in to play
- leaderboard (10 top players)
- last scores (10 last games)

## Technologies
- Client side - written in `Java` (and XML), using Android Studio with Android 9.0 SDK (API level 28).
- Server side - written in `PHP` using `XAMPP` `PhpMyAdmin` and `MySql` Database.
- Client and server communication is via `REST API` (using Retrofit client).

## PDF files (in hebrew)
- Description to user including pictures in `Description_to_user.pdf`
- System's structure and flow between the different components in `system_structure.pdf`

## Steps to run the Battleship app:
* open new project in Android Studio named "Battleship"
* copy java files from `battleship_app/java to app/src/main/java/com.example.battleship`
* copy xml layout files from `battleship_app/res/layout to app/src/res/layout`
* create new folder in res named `anim`. copy files from `battleship_app/res/anim` to `app/src/res/anim`.
* copy the pictures from `battleship_app/res/drawble` folder to `app/src/res/drawble`.

add to build.gradle the dependencies:

```sh
implementation 'io.reactivex.rxjava2:rxjava:2.2.2'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
implementation 'com.squareup.retrofit2:retrofit:2.3.0'
implementation 'com.squareup.retrofit2:retrofit:2.3.0'
implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
```

add to Manifest.xml:
* in `<application>` tag: this allows network access to the app.
```sh
android:networkSecurityConfig="@xml/network_security_config"
```

* before `</application>` tag:
```sh
<activity android:name=".GameActivity"> </activity>
<activity android:name=".TopTenActivity"> </activity>
<activity android:name=".LastTenActivity"> </activity>
<activity android:name=".UserMenuActivity"> </activity>
<activity android:name=".RegisterActivity"> </activity>
<activity android:name=".LoginActivity"> </activity>
```

* after `</application>` tag: this allows internet premission.
```sh
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
```

create new folder `app/src/res/xml` and new file `network_security_config.xml`.
copy this to `network_security_config.xml`:
```sh
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

##### optional:
in `app/src/res/values` change `purple_500` code to `#5D1049`
and `purple_700` to `#7D1662`.

