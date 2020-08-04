# AGBR/U5

__(under construction...)__

AGBR/U5 is an application designed to automate the process of generating tables for a given production scenario in a GasLift optimization context.

When a new production test is carried out in an oil well, a sequence of steps is performed to fulfill the optimization process:

> 1. Store the test results in the *PI* software
>
> 2. Update the file containing information about that well, called *marlim file*, with the resulting data
> 3. If the updated marlim file <ins>does not</ins> contain the efficiency curves (*MR2 file*), generate them. If it already contains the efficiency curves (*AS file*), no action is necessary for this step
> 4. Generate tables for that well, updating the optimization file (*GLN file*) of the platform
> 5. Execute the optimization using the updated optimization file and some constraints

The steps above are manually performed by a user. The *AGBR/U5 application* aims to insert a reactive behavior in this process, automating the generation of tables whenever a marlim file is updated after a test (steps 3-4).


> This application has been developed using [Node-RED](https://nodered.org/) and [JaCaMo](http://jacamo.sourceforge.net/).

## Prerequisites

- [Docker](https://docs.docker.com/engine/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)


## Running with Docker Compose
1. Make sure you are in the root directory of this project
2. Build docker image for JaCaMo:
  ```
  open a command-line shell
  $ cd jacamo
  $ ./gradlew clean compileJava copyToLib
  $ docker build -t agbr/u5:0.1 .
  ```
3. Launch Node-RED flows and JaCaMo application via Docker Compose:
  ```
  go back to the root directory and run the application
  $ cd ..
  $ docker-compose up
  ```

With JaCaMo and Node-RED running, just copy a marlim file (MR2 file) to the directory named ***marlim***. This action simulates the *step 2* of the optimization process previously described, triggering the AGBR/U5 application. At the end of the execution, the GLN file (located in the marlim directory) is updated with data from the marlim file. The available marlim files are in the *marlim-files* directory.
