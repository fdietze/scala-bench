
val sharedSettings = Seq(
  scalaVersion := "2.12.6"
)
val agent = project
  .settings(
    sharedSettings,
    packageOptions in (Compile, packageBin) += 
     Package.ManifestAttributes( "Premain-Class" -> "agent.Agent" )
  )

val bench = project
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(agent)
  .settings(
    sharedSettings,
    fork in run := true,

    scalaJSStage in Global := FullOptStage,

    /* libraryDependencies += "com.lihaoyi" % "ammonite_2.11.8" % "0.7.7", */
    libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.6.6",
    libraryDependencies += "com.lihaoyi" %%% "pprint" % "0.5.3",
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-locales" % "0.5.2-cldr31",
    javaOptions in run += ("-javaagent:" + (packageBin in (agent, Compile)).value)
)
