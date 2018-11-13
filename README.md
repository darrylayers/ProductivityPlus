# ProductivityPlus
Fall 2018 Capstone Project - Monitor how much time you spend in your applications and choose to view the data with in-app graphs or export to a .xlms file to be view in Excel.

**How to use**

You can find the latest version of this program at http://austinayers.com/ProductivityPlus.zip

Unzip the contents (ProductivityPlus.jar, resources, saved_data, and output folders) to any directory and run.

All preferences are saved using Java Preferences, meaning the can be found under your registry.

(Run->regedit->HKEY_CURRENT_USER->Software->JavaSoft->Prefs)

Library | What it is used for
------------ | -------------
Apache POI | Used to write Excel files
Apache XMLBeans | Used to generate a JAR library specifically for an XML schema (I need to use this to aid POI to write Excel files)
Commons Compress | Needed to support Apache POI
Commons Collections | Needed to support Apache POI
MigLayout15-swing | This is the Swing layout that most of ProductivityPlus is built on
LGoodDatePicker | Used to create calendar elements to the GUI and allow dates to be selected
jUnit | Used for unit tests
JNA | Used to access the WinAPI
JNA-Platform | More tools and interfaces for JNA lib
jGoodies | Form layout used for GUI building
JFreeChart | Used for graphing data
JCommon | Used to help JFreeChart graph data

All resources can be found here: http://austinayers.com/ProductivityPlus_lib.zip

![Image of the main window](http://austinayers.com/ProductivityPlusImages/main_window.png)
