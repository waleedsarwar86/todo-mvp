# ToDo Lite Android MVP example


Todo Lite app, shows the **MVP** pattern usage on Android.

## Screenshots:
<p float="left">
<img src="screenshots/1-login.png" width="220"/>
<img src="screenshots/2-home_empty.png" width="220"/>
<img src="screenshots/3-home_tasks.png" width="220"/>
<img src="screenshots/4-add_edit_task.png" width="220"/>

</p>

## App

- Login/Register 
- Add Task
- Update Task
- Delete/Complete Task by swiping the card left/right

## Project structure

**data**: Implementation of the Repository pattern, contains all the data accessing.<br/>
**di**: Contains the classes that provide dependencies using Dagger2.<br />
**ui**: MVP structure, all Activities, Fragments and UI elements in this package.<br />
**util**: Utility classes.<br />

## Tools/Libraries

* [Constraint Layout](https://developer.android.com/training/constraint-layout/index.html)
* [Firebase Auth](https://firebase.google.com/docs/auth/)
* [Firebase Database](https://firebase.google.com/docs/database/)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxBindings](https://github.com/JakeWharton/RxBinding)
* [Retrofit 2](https://github.com/square/retrofit)
* [Stetho](https://github.com/facebook/stetho)
* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Timer](https://github.com/JakeWharton/timber)
* [Dagger 2](https://github.com/google/dagger)

## Future work

### Todo

- Make presenter to survive configuration changes using ViewModel.
- Write Unit and instrumentation tests
- Setup continues integration using Circle.ci
- Convert the project to kotlin.

