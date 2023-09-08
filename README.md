# <b>ContactSyncApp</b>

A simple android contact synchronization app demonstrating Jetpack Compose, Coroutines, Kotlin flow and room database.

the simple demonstrate how we fetch contacts, save them in a local database
and observe any changes (Update, Delete and Save) with the abstract class [ContentObserver](https://developer.android.com/reference/android/database/ContentObserver).

The app offer the possibility to search contacts by phone number or display name.

![alt text](https://github.com/forzakmah/contactSyncApp/blob/main/previews/ss.png "Preview")

### Libraries

* Android Architecture Components (ViewModel, Compose navigation)
* [Room](https://developer.android.com/training/data-storage/room) is a persistence library provides an abstraction
  layer over SQLite.
* [coroutines](https://developer.android.com/kotlin/coroutines?hl=fr) is a concurrency design pattern for asynchronously
  tasks.
* [Jetpack Compose](https://developer.android.com/jetpack/compose) is a modern toolkit for building native UI.
* [material3](https://developer.android.com/jetpack/androidx/releases/compose-material3) Material Design 3 Components.
* [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization) Kotlin serialization consists of a compiler
  plugin, that generates visitor code for serializable classes.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) is a dependency injection library for
  Android
* [Kotlin-Flow](https://developer.android.com/kotlin/flow) flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value

#### Sonar analyse result

![alt text](https://github.com/forzakmah/contactSyncApp/blob/main/previews/sonar_contact_app.png "Preview")
