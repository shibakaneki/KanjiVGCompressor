This projects requires these specific settings:
- Get the JDBC drivers
	- Get them from the web (http://www.toodlepip.co.uk/blog/2008/08/using-sqlite-eclipse-and-dbedit) and add them to your project as an external JAR dependency
- Increase the heap size of the Java VM in order to be able to parse kanjidic2
	- In Eclipse: Run/Run Configurations/Arguments/VM, write this: -Xms512M -Xmx1524M