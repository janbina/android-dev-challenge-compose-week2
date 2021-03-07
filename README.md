# Timer

![Workflow result](https://github.com/janbina/android-dev-challenge-compose-week2/workflows/Check/badge.svg)


## :scroll: Description
Simple timer app that allows you to specify the amount of time you want to track
and then shows you how much time is remaining, visualized with simple animation.


## :bulb: Motivation and Context
I wrote this app for Android Dev Challenge, to try some concepts of Jetpack Compose.
I like the state management, which is using just `rememberSaveable {}`. I wrapped the
`MutableState` in class that has methods to manipulate it (`addDigit()`, `removeAllDigits()`, ...)
and its super simple, no need for ViewModels.
I like how easy it is to create the animation and also how easy it is to create light and dark theme
and switch between them.


## :camera_flash: Screenshots
<!-- You can add more screenshots here if you like -->
<img src="/results/screenshot_1.png" width="260">&emsp;<img src="/results/screenshot_2.png" width="260">

<img src="/results/screenshot_3.png" width="260">&emsp;<img src="/results/screenshot_4.png" width="260">

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```