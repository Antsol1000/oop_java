# The easiest place to simulate trading

### Object-oriented programming - java project
### Antoni Solarski 148270

We can run the project in two ways:
- use run task from Gradle, simply hit:

        gradlew run
- or (build fat-jar and) run executable jar 
without any parameters

        gradlew jar
        java -jar java-project-1.0.jar

The application GUI is pretty straightforward. 
You can create currencies, markets, funds and 
investors by clicking create button.
Then you can create asset in the market window.

Investors and funds will appear automatically 
when assets are created.
