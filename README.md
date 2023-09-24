<h1 align="center">Upbit Clone</h1>

<p align="center">
  <a href="https://kotlinlang.org"><img alt="Kotlin Version" src="https://img.shields.io/badge/Kotlin-1.8.0-blueviolet.svg?style=flat"/></a>
  <a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
</p>


The Upbit Clone project is an application created using the Open API provided by Upbit to display Upbit coin information.

<div align="center">
  <img src="https://github.com/meenjoon/Upbit/assets/88024665/cef5edad-7def-4463-9dc6-bf255a1fa1c0" alt="UPBIT LOGO">
</div>

<br>

## Tech stack & Open-source libraries

### Android

- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- JetPack
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Store UI related data that isn't destroyed on app rotations.
  - [Compose](https://developer.android.com/jetpack/compose?hl=ko) - Declarative UI toolkit for building native Android user interfaces
- [Hilt](https://dagger.dev/hilt/) - Dependency injection.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.

### API Data

  - Upbit[ http communication ] - [Upbit Http](https://docs.upbit.com/reference/%EB%A7%88%EC%BC%93-%EC%BD%94%EB%93%9C-%EC%A1%B0%ED%9A%8C)
  - Upbit[ WebSocket communication ] - [Upbit WepSocket](https://docs.upbit.com/reference/websocket-ticker#response)

## Features


> ### Real-time coin information checking & applying effects (e.g., border) on real-time data changes

<div align="center">

| Dark | Lite |
| :---------------: | :---------------: |
| <img src="https://github.com/meenjoon/Upbit/blob/main/docs/upbit_dark_home.webp" align="center" width="300px"/> | <img src="https://github.com/meenjoon/Upbit/blob/main/docs/upbit_lite_home.webp" align="center" width="300px"/> |

</div>

> ### Sorting functionality for real-time coin information (current price, daily change, trading volume)

<div align="center">

| Dark | Lite |
| :---------------: | :---------------: |
| <img src="https://github.com/meenjoon/Upbit/blob/main/docs/upbit_dark_home_sort.webp" align="center" width="300px"/> | <img src="https://github.com/meenjoon/Upbit/blob/main/docs/upbit_lite_home_sort.webp" align="center" width="300px"/> |

</div>

## Architecture

Upbit_Clone is based on the MVVM architecture and the Repository pattern.

<p align = 'center'>
<img width = '600' height = '600' src = 'https://github.com/meenjoon/Upbit/assets/88024665/49226fde-d66f-40d9-aa63-1ed9cd6aa24c'>
</p>

Currently, data is obtained through direct API calls from web services such as Upbit, similar to this method.

<p align = 'center'>
<img width = '900' src = 'https://github.com/meenjoon/Upbit/assets/88024665/4d463a3f-558a-4a2c-954f-082a3f725b2f'>
</p>
