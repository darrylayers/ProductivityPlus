# ProductivityPlus
Fall 2018 Capstone Project - ProductivityPlus is a simplistic Windows GUI application designed to record and analyze how much time the user spends in the applications they use. Monitor how much time you spend in your applications and choose to view the data with in-app graphs or export to a .xlms file to be viewed in Excel. 

**Main window:**

![Image of the main window](http://austinayers.com/ProductivityPlusImages/main_window.png)

**Pie Chart demo:**

![Image of the pie chart window](http://austinayers.com/ProductivityPlusImages/graph_main.png)

**Explore Data window:**

![Image of the explore data](http://austinayers.com/ProductivityPlusImages/explore_data_combo.png)



**Instructions**
To run:
You can find the latest version of this program at 

http://austinayers.com/ProductivityPlus.exe or http://austinayers.com/ProductivityPlus.jar

Alternative mirrors: [.exe](https://github.com/darrylayers/ProductivityPlus/tree/master/downloads/ProductivityPlus.exe), [.jar](https://github.com/darrylayers/ProductivityPlus/tree/master/downloads/ProductivityPlus.exe)

Just double click to run.

All preferences are saved using Java Preferences, meaning they can be found under your registry.

(Run->regedit->HKEY_CURRENT_USER->Software->JavaSoft->Prefs)

To edit:
Simply clone or download the repo and import the project to your IDE.
All project resources can be found here: http://austinayers.com/ProductivityPlus_lib.zip
Want to contribute? Feel free to form and make adjustments! 

**Resources**

Library | What it is used for
------------ | -------------
[Apache POI](https://poi.apache.org/) | Used to write Excel files 
[Apache XMLBeans](https://poi.apache.org/) | Used to generate a JAR library specifically for an XML schema (I need to use this to aid POI to write Excel files)
[Commons Compress](https://commons.apache.org/proper/commons-compress/) | Needed to support Apache POI
[Commons Collections](https://commons.apache.org/proper/commons-collections/) | Needed to support Apache POI
[MigLayout15-swing](https://sourceforge.net/projects/photoviewer/files/bot/1.0/miglayout15-swing.jar/download) | This is the Swing layout that most of ProductivityPlus is built on
[LGoodDatePicker](https://github.com/LGoodDatePicker/LGoodDatePicker) | Used to create calendar elements to the GUI and allow dates to be selected
[jUnit](https://junit.org/junit5/) | Used for unit tests
[JNA](https://github.com/java-native-access/jna) | Used to access the WinAPI
[JNA-Platform](https://github.com/java-native-access/jna) | More tools and interfaces for JNA lib
[jGoodies](http://www.jgoodies.com/) | Form layout used for GUI building
[JFreeChart](http://www.jfree.org/jfreechart/) | Used for graphing data
[JCommon](http://www.jfree.org/jcommon/) | Used to help JFreeChart graph data

[Project Proposal](https://github.com/darrylayers/ProductivityPlus/tree/master/downloads/ProjectProposal.pdf)

[Technical Report](https://github.com/darrylayers/ProductivityPlus/tree/master/downloads/TechnicalReport.pdf)

[Timeline of this project using Gource](http://austinayers.com/ProductivityPlusImages/gource.gif)

ProductivityPlus icon is from https://openclipart.org/detail/152293/computer-chart, free for commercial and non-commercial use.

