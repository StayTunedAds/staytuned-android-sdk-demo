# SDK StayTuned
​
Podcasts player for android.
​
This module adds an in-app podcasts player to your application.
​
## Installation
​
Add the following dependency to your project gradle file to have all the features available:
​
```gradle
dependencies {
	implementation 'sdk.staytuned:staytuned:1.0.0'
}
```
​
## Prerequisites
​
-   Your app must be using `androidx` instead of the old `android.support` libraries.
​
-   The Staytuned SDK uses Java 8. You need to add the following code to your project `build.gradle`
​
```gradle
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ...
}
```
​
## Initialisation
​
First, init the SDK in your `Application` class
​
```kotlin
override fun onCreate() {
    super.onCreate()
    Staytuned.init(applicationContext)
}
```
​
Include the floating player button in any view you want it to be visible in. It is used to open the widget player and interact with the current or the other podcasts.
​
```xml
<sdk.staytuned.views.FloatingPlayerButton
  android:id="@+id/floatingButton"
  android:layout_width="49dp"
  android:layout_height="49dp"
  android:elevation="4dp" />
```
​
## Single Location App
​
Include the main widget in your main layout. This view will be used to initialize the widget.
​
```xml
<sdk.staytuned.views.Widget
  android:id="@+id/widget"
  android:layout_width="1dp"
  android:layout_height="1dp"
  app:locationKey="@string/your-location-key"
   />
```
​
You also can pass your `locationKey` programmatically
​
```kotlin
findViewById<Widget>(R.id.widget).loadLocation("your-location-key")
```
​
Then declare the used widget
​
```kotlin
WidgetService.setCurrentWidget(widget)
```
​
## Multiple Location App
​
As for the single location app, you'll need to include the widget layout into your views
​
```xml
<sdk.staytuned.views.Widget
  android:id="@+id/widget"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  app:locationKey="@string/your-location-key"
   />
```
​
## Customization
​
If you wish to change some UI properties of the SDK such as the text default color, you can do so by overriding them in your `styles.xml` file :
​
```xml
 <item name="textDefaultColor">@color/your-default-text-color</item>
```
​
Here is a list of all the properties you can customize in the widget, followed with examples. You can either modify colors:
​
| Property | Example |
| :------ |:-------|
| textDefaultColor  | ```<item name="textDefaultColor">@color/your-default-text-color</item>``` |
| textLightColor  | ```<item name="textLightColor">@color/your-light-text-color</item>``` |
| textLinkColor  | ```<item name="textLinkColor">@color/your-link-text-color</item>``` |
| textGrayLightColor  | ```<item name="textGrayLightColor">@color/your-light-gray-text-color</item>``` |
​
Or font families of your application:
​
| Property | Example |
| :------ |:-------|
| fontFamilyBold  | ```<item name="fontFamilyBold">@font/your-bold-font</item>``` |
| fontFamilySemiBold  | ```<item name="fontFamilySemiBold">@font/your-semi-bold-font</item>``` |
| fontFamilyMedium  | ```<item name="fontFamilyMedium">@font/your-medium-font</item>``` |
| fontFamilyRegular  | ```<item name="fontFamilyRegular">@font/your-regular-font</item>``` |
​
## Change default actions in player
​
By default, the player will open its own podcast and episode detail screen. If you don't need them, you can set a custom action on item click event
​
For episode cells
​
```kotlin
Staytuned.onEpisodeSeeMoreClick = { track, content, activity, isFromPlayerDialog ->
    // your action
    // activity.finish() close the player
}
```
​
For podcast cells
​
```kotlin
Staytuned.onPodcastSeeMoreClick = { content, activity, isFromPlayerDialog ->
    // your action
    // activity.finish() close the player
}
```
