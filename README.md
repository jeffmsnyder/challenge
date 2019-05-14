# Challenge

Requests a dataset id and answers it with the dealer information and the dealers associated vehicles.

Using the provided API, create a program that retrieves a dataset id, retrieves all vehicles
and dealers for that dataset, and successfully posts to the answer endpoint. Each vehicle
and dealer should be requested only once. The response structure returned from the answer
endpoint describes the status and total ellapsed time; this response is to be outputted.

## Getting Started

### Prerequisites

Requires Java 8 SDK
1. If not installed, download from here: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Accept the license agreement
3. Select the appropriate product to download
4. Install as appropriate for your machine OS

Requires Maven
1. If not installed, follows the instruction here: https://maven.apache.org/install.html

May require Git depending on how the project is retrieved
1. If not installed, follows the instruction here: https://git-scm.com/downloads

### Running locally using IntelliJ IDEA

Prerequisite for running the app using IntelliJ IDEA:

1. [Fork it](https://github.com/jeffmsnyder/challenge/fork)
2. Clone the project `git clone https://github.com/YOUR_GITHUB_USERNAME/challenge.git`
3. Open IntelliJ IDEA `File -> New -> Project from Existing Sources... `
4. Navigate to the project and select `pom.xml`. Click `OK`
5. Click `Next` on Import Project from Maven window
6. Verify that project is selected, click 'Next'
7. Verify that the Java 8 SDK is selected (version 1.8), click 'Next'
8. Click 'Finished'

The first two steps can be changed to just download the project, by doing the following:

1. Go to https://github.com/jeffmsnyder/challenge
2. Select Clone or Download / Download ZIP
3. Unzip project and place where project where it is to be located
4. Continue with step 3 above.

To run under IntelliJ:

1. Edit the configuration to run Challenge
2. Update the command line arguments as desired
3. Depending on the arguments, running it will generate results or wait until additional user input before results are output

### Installing

Run the following command from the top level project directory (where the pom.xml file is):

<pre>
mvn clean package
</pre>

## Running the tests

To run the tests, use the following command:

<pre>
mvn clean test
</pre>

1. DatasetTest: tests for dataset
2. VehiclesTest: tests processing vehicles
3. DealersTest: tests for associating vehicles with dealers

### End to end tests

1. ChallengeTest: tests the complete application

## Running from the command line after installation

### Windows

From the project directory, use challenge.bat.  An example of its use is as follows:

<pre>
challenge
</pre>

Output from the application is:

<pre>
Congratulations.
5234
</pre>

### *nix

This command must be run before the shell script can be used:

<pre>
chmod +x challenge.sh
</pre>

Then, the following is an example of its usage:

<pre>
./challenge
</pre>

Output from the application is:

<pre>
Congratulations.
5234
</pre>

## Usage

<pre>
usage: challenge
Requests a dataset id and answers it with the dealer information and the dealers associated vehicles.
</pre>

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Jeff Snyder** - *Initial work* - [jeffmsnyder](https://github.com/jeffmsnyder)

