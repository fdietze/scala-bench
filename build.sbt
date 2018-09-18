// shadow sbt-scalajs' crossProject and CrossType from Scala.js 0.6.x
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val sharedSettings = Seq(
  crossScalaVersions := Seq("2.11.12", "2.12.6"),
  scalaVersion := crossScalaVersions.value.last,
)

lazy val bench =
  // select supported platforms
  crossProject(JSPlatform, JVMPlatform)//, NativePlatform)
    .crossType(CrossType.Pure) // [Pure, Full, Dummy], default: CrossType.Full
    .dependsOn(agent)
    .settings(
      sharedSettings,
      libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.6.6",
      libraryDependencies += "com.lihaoyi" %%% "pprint" % "0.5.3",
      libraryDependencies += "io.github.cquiroz" %%% "scala-java-locales" % "0.5.2-cldr31",
      /* libraryDependencies += "com.lihaoyi" % "ammonite_2.11.8" % "0.7.7", */
    )
    .jsSettings(
      scalaJSUseMainModuleInitializer := true,
      scalaJSStage in Global := FullOptStage,
    )
    .jvmSettings(
      fork in run := true,
      javaOptions in run += ("-javaagent:" + (packageBin in (agent.jvm, Compile)).value)
    )
    /* .nativeSettings( */
    /* ) */

val agent = 
  crossProject(JVMPlatform, JSPlatform)
    .crossType(CrossType.Full)
    .settings(
      sharedSettings,
      packageOptions in (Compile, packageBin) += 
        Package.ManifestAttributes( "Premain-Class" -> "agent.Agent" ),
    )
