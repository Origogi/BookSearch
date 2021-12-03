# BookSearch

## Tech stack & Open-source libraries

- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- JetPack
  - LiveData
  - ViewModel
  - Data binding
  - Compose
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- Open-source
  - [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
  - [Glide](https://github.com/bumptech/glide) - loading images.
  - [Coil](https://github.com/coil-kt/coil) - loading images for Compose

## Open API

ITBook Store API

### Search Book

<img src="https://www.programmableweb.com/sites/default/files/styles/article_profile_150x150/public/icon_itbookstore_200.png?itok=JrMOB-xu" align="right" width="10%"/>

> API(GET): https://api.itbook.store/1.0/search

### Get Book detail information

> API(GET): https://api.itbook.store/1.0/search/{query}/{page}


## MAD Score

![summary](https://user-images.githubusercontent.com/35194820/144568830-2b97c0fd-8f5e-42f7-81f0-8f2d514e38ed.png)
![kotlin](https://user-images.githubusercontent.com/35194820/144568823-03974486-5e58-4ed4-8ba5-e1321266783e.png)
![jetpack](https://user-images.githubusercontent.com/35194820/144568832-7466762b-ab15-452e-b443-0d7ba0c0ee21.png)

## Design Reference

### [Main View (List)](https://dribbble.com/shots/4454451-Book-App-UI)

![ref1](https://user-images.githubusercontent.com/35194820/144572996-f2a81569-5ab2-46a7-86cf-9ff62cfa5315.PNG)

### [Detail View](https://dribbble.com/shots/16492554-Bacala-Online-Book-Mobile-App)

![ref2](https://user-images.githubusercontent.com/35194820/144573004-99837b3d-2286-485c-a2dd-3ebad79713f4.PNG)