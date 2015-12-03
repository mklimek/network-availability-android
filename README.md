**Problem:**

http://stackoverflow.com/questions/29677852/connectivitymanager-extra-no-connectivity-is-always-false-on-android-lollipop


**Solution:**

NetworkAvailability lets you register `BroadcastReceiver` which works for pre-lollipop and lollipop devices as well.    

You have to compile your project with AndroidSDK 21 at least:
```gradle
   android{
        compileSdkVersion 21
   }
```

See ExampleUsage.java 

