*Problem:*

http://stackoverflow.com/questions/29677852/connectivitymanager-extra-no-connectivity-is-always-false-on-android-lollipop


*Solution:*

**NetworkAvailability** lets you register `BroadcastReceiver` which works for pre-lollipop and lollipop devices as well.    

You have to compile your project at least with AndroidSDK 21:
```gradle
android{
   compileSdkVersion 21
}
```

See [ExampleActivity](https://github.com/mklimek/NetworkAvailability/blob/master/ExampleActivity.java).

