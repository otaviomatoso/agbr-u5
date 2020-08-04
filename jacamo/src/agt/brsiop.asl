// agbr/u5

/* Initial beliefs and rules */
url("http://node-red:1880/brsiop"). // (docker) dummy url
username("jomi"). // BR-SiOP credentials
password("hubner"). // BR-SiOP credentials
gln("P-40_2020.gln"). // optimization file

/* Initial goals */
!start.

/* Plans */
+!start : url(U)
   <- register(U); // registers the dummy url
      .print("ag_brsiop started");
      .

-!start <- .print("There was an error trying to start").

+!sign_in : username(User) & password(Password)
   <- .print("Authenticating to BR-SiOP...");
      .term2string(User, U);
      .term2string(Password, P);
      .concat("login(",U,",",P,")", Login);
      act(Login, T); // BR-SiOP authentication
      +token(T);
      .

-!sign_in <- .print("There was an error trying to sign in").

+token(_) <- .print("BR-SiOP authentication: Ok\n").

+!wait_marlim : gln(Gln)
   <- .print("Waiting for new Marlim files...\n");
      .wait({+new_file(W,F)[source(S)]});
      .print("New Marlim file detected");
      .print("Well name: ", W);
      .print("File name: ", F);
      -+well(W); // updates current well
      -+marlim_file(F); // updates current marlim file
      checkWell(W, Gln, R);
      .print(R);
      !generate_curve(F);
      // -new_file(W,F)[source(S)];
      .

-!wait_marlim <- .print("There was an error trying to manipulate the files").

+!generate_curve(File) : token(Token)
   <- .print("Generating the efficiency curves...");
      .print("Uploading the file: ", File);
      .term2string(File,F);
      .term2string(Token,T);
      .concat("upload(",T,",",F,")", Upload);
      act(Upload, Res1);
      .print("Upload response: ", Res1);

      .print("Running the MC_AS algorithm...");
      .concat("mc_as(",T,",",F,")", MC_AS);
      act(MC_AS, JobId);   // BR-SiOP MC_AS algorithm
      .print("JobID MC_AS: ", JobId);
      !wait_exec(JobId); // waits the algorithm to finish running
      .print("The efficiency curves have been generated");
      .

-!generate_curve(File) <- .print("There was an error trying to generate the efficiency curves from ", File).

+!update_table : token(Token) & well(Well) & marlim_file(File) & gln(Gln)
   <- .print("Updating table...");
      .term2string(Token,T);
      .term2string(Gln,G);
      .concat("upload(",T,",",G,")", Upload);
      .print("Uploading the file: ", Gln);
      act(Upload, Res1);
      .print("Upload response: ", Res1);

      .term2string(Well,W);
      .term2string(File,F);
      .concat("add_as(",T,",",W,",",G,",",F,")", AddAs);
      .print("Running the addAS2GLC algorithm...");
      act(AddAs, JobId);   // BR-SiOP addAS2GLC algorithm
      .print("JobID addAS2GLC: ", JobId);
      !wait_exec(JobId); // waits the algorithm to finish running

      .concat("download(",T,",",G,")", Download);
      .print("Downloading the output: ", Gln);
      act(Download, Res2);
      .print("Download response: ", Res2);
      .print("The table for well ", Well, " has been updated\n");
      !!restart;
      .

-!update_table : marlim_file(File) <- .print("There was an error trying to update the table from ", File).

+!restart <- resetGoal("wait_marlim").

+!wait_exec(JobId) : token(Token)
   <- .term2string(Token, T);
      .term2string(JobId, J);
      .concat("jobInfo(",T,",",J,")", JobInfo);
      act(JobInfo, S);
      .print("State: ", S);
      if (S \== "FINISHED") {
        .wait(3000);
        !wait_exec(JobId);
      } else { .print("The algorithm has finished running"); }
      .

-!wait_exec(JobId) <- .print("There was an error executing the process ", JobId).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
