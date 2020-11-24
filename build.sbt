lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion    = "2.6.10"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "old.com.example",
      scalaVersion    := "2.13.3"
    )),
    name := "RandomPeopleGenerator",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",
      "org.postgresql" % "postgresql" % "42.2.18",
    //  "org.scalikejdbc" %% "scalikejdbc"       % "3.5.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      //"org.scalikejdbc" %% "scalikejdbc"       % "3.5.0",
      //"com.h2database"  %  "h2"                % "1.4.200",
     // "ch.qos.logback"  %  "logback-classic"   % "1.2.3",

     // "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "2.0.2",
     // "com.typesafe.slick" %% "slick" % "3.3.2",
     // "org.postgresql" % "postgresql" % "42.2.18",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )
