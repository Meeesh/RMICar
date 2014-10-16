all: classes jarfile

classes:
	javac *.java
	
jarfile:
	jar cvmf manifest.mf SpeedRacer.jar *.class ./images

clean:
	rm *.class
	rm *.jar
		