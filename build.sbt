import org.scalajs.core.tools.linker.ModuleKind
import sbt.Keys.{libraryDependencies, resolvers}

name := "amf"

version in ThisBuild := "1.3.2"

scalaVersion in ThisBuild := "2.12.2"

publish := {}

jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv()

val settings = Common.settings ++ Common.publish ++ Seq(
  organization := "org.mule.amf",
  resolvers ++= List(Common.releases, Common.snapshots, Resolver.mavenLocal),
  credentials ++= Common.credentials(),
  aggregate in assembly := false,
  libraryDependencies ++= Seq(
    "org.scalatest"    %%% "scalatest" % "3.0.0" % Test,
    "com.github.scopt" %%% "scopt"     % "3.7.0"
  )
)

/** **********************************************
  * AMF-Core
  ********************************************** */
lazy val defaultProfilesGenerationTask = TaskKey[Unit](
  "defaultValidationProfilesGeneration",
  "Generates the validation dialect documents for the standard profiles")

lazy val core = crossProject
  .settings(
    Seq(
      name := "amf-core",
      libraryDependencies += "org.mule.syaml" %%% "syaml" % "0.1.5"
    ))
  .in(file("./amf-core"))
  .settings(settings: _*)
  .jvmSettings(
    libraryDependencies += "org.scala-js"           %% "scalajs-stubs"          % scalaJSVersion % "provided",
    libraryDependencies += "org.scala-lang.modules" % "scala-java8-compat_2.12" % "0.8.0",
    libraryDependencies += "org.json4s"             %% "json4s-jackson"         % "3.5.2",
    artifactPath in (Compile, packageDoc) := baseDirectory.value / "target" / "artifact" / "amf-core-javadoc.jar"
  )
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    scalaJSOutputMode := org.scalajs.core.tools.linker.backend.OutputMode.ECMAScript6,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    artifactPath in (Compile, fullOptJS) := baseDirectory.value / "target" / "artifact" / "amf-core-module.js"
  )

lazy val coreJVM = core.jvm.in(file("./amf-core/jvm"))
lazy val coreJS  = core.js.in(file("./amf-core/js"))

/** **********************************************
  * AMF-WebAPI
  ********************************************** */
lazy val webapi = crossProject
  .settings(name := "amf-webapi")
  .dependsOn(core)
  .in(file("./amf-webapi"))
  .settings(settings: _*)
  .jvmSettings(
    libraryDependencies += "org.scala-js"           %% "scalajs-stubs"          % scalaJSVersion % "provided",
    libraryDependencies += "org.scala-lang.modules" % "scala-java8-compat_2.12" % "0.8.0",
    libraryDependencies += "org.json4s"             %% "json4s-jackson"         % "3.5.2",
    artifactPath in (Compile, packageDoc) := baseDirectory.value / "target" / "artifact" / "amf-webapi-javadoc.jar"
  )
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    scalaJSOutputMode := org.scalajs.core.tools.linker.backend.OutputMode.ECMAScript6,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    artifactPath in (Compile, fullOptJS) := baseDirectory.value / "target" / "artifact" / "amf-webapi-module.js"
  )

lazy val webapiJVM = webapi.jvm.in(file("./amf-webapi/jvm"))
lazy val webapiJS  = webapi.js.in(file("./amf-webapi/js"))

/** **********************************************
  * AMF-Vocabularies
  ********************************************** */
lazy val vocabularies = crossProject
  .settings(name := "amf-vocabularies")
  .dependsOn(core)
  .in(file("./amf-vocabularies"))
  .settings(settings: _*)
  .jvmSettings(
    libraryDependencies += "org.scala-js"           %% "scalajs-stubs"          % scalaJSVersion % "provided",
    libraryDependencies += "org.scala-lang.modules" % "scala-java8-compat_2.12" % "0.8.0",
    libraryDependencies += "org.json4s"             %% "json4s-jackson"         % "3.5.2",
    artifactPath in (Compile, packageDoc) := baseDirectory.value / "target" / "artifact" / "amf-vocabularies-javadoc.jar"
  )
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    scalaJSOutputMode := org.scalajs.core.tools.linker.backend.OutputMode.ECMAScript6,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    artifactPath in (Compile, fullOptJS) := baseDirectory.value / "target" / "artifact" / "amf-vocabularies-module.js"
  )

lazy val vocabulariesJVM = vocabularies.jvm.in(file("./amf-vocabularies/jvm"))
lazy val vocabulariesJS  = vocabularies.js.in(file("./amf-vocabularies/js"))

/** **********************************************
  * AMF-Validation
  ********************************************** */
lazy val validation = crossProject
  .settings(name := "amf-validation")
  .dependsOn(core, vocabularies)
  .in(file("./amf-validation"))
  .settings(settings: _*)
  .jvmSettings(
    libraryDependencies += "org.scala-js"           %% "scalajs-stubs"          % scalaJSVersion % "provided",
    libraryDependencies += "org.scala-lang.modules" % "scala-java8-compat_2.12" % "0.8.0",
    libraryDependencies += "org.json4s"             %% "json4s-jackson"         % "3.5.2",
    libraryDependencies += "org.topbraid"           % "shacl"                   % "1.0.1",
    libraryDependencies += "org.slf4j"              % "slf4j-simple"            % "1.7.12",
    artifactPath in (Compile, packageDoc) := baseDirectory.value / "target" / "artifact" / "amf-validation-javadoc.jar"
  )
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    scalaJSOutputMode := org.scalajs.core.tools.linker.backend.OutputMode.ECMAScript6,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    artifactPath in (Compile, fullOptJS) := baseDirectory.value / "target" / "artifact" / "amf-validation-module.js"
  )

lazy val validationJVM = validation.jvm.in(file("./amf-validation/jvm"))
lazy val validationJS  = validation.js.in(file("./amf-validation/js"))

/** **********************************************
  * AMF Client
  ********************************************** */
lazy val client = crossProject
  .settings(Seq(
    name := "amf-client",
    fullRunTask(defaultProfilesGenerationTask, Compile, "amf.tasks.validations.ValidationProfileExporter")))
  .dependsOn(core, webapi, vocabularies, validation)
  .in(file("./amf-client"))
  .settings(settings: _*)
  .jvmSettings(
    libraryDependencies += "org.scala-js"           %% "scalajs-stubs"          % scalaJSVersion % "provided",
    libraryDependencies += "org.scala-lang.modules" % "scala-java8-compat_2.12" % "0.8.0",
    libraryDependencies += "org.json4s"             %% "json4s-jackson"         % "3.5.2",
    libraryDependencies += "org.topbraid"           % "shacl"                   % "1.0.1",
    mainClass in Compile := Some("amf.Main"),
    packageOptions in (Compile, packageBin) += Package.ManifestAttributes("Automatic-Module-Name" → "org.mule.amf"),
      aggregate in assembly := true,
    test in assembly := {},
    mainClass in assembly := Some("amf.Main"),
    assemblyOutputPath in assembly := file(s"./amf-${version.value}.jar"),
    assemblyMergeStrategy in assembly := {
      case x if x.toString.endsWith("JS_DEPENDENCIES")             => MergeStrategy.discard
      case PathList(ps @ _*) if ps.last endsWith "JS_DEPENDENCIES" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
  .jsSettings(
    jsDependencies += ProvidedJS / "shacl.js",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    scalaJSOutputMode := org.scalajs.core.tools.linker.backend.OutputMode.ECMAScript6,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    artifactPath in (Compile, fullOptJS) := baseDirectory.value / "target" / "artifact" / "amf-client-module.js"
  )

lazy val clientJVM = client.jvm.in(file("./amf-client/jvm"))
lazy val clientJS  = client.js.in(file("./amf-client/js"))

// Tasks

val publishJS = TaskKey[Unit](
  "publishJS",
  "Publish npm module")

publishJS := {
  val _ = (fullOptJS in Compile in clientJS).value
  "./amf-client/js/build-scripts/deploy-develop.sh".!
}

val buildJS = TaskKey[Unit](
  "buildJS",
  "Build npm module")
buildJS := {
  val _ = (fullOptJS in Compile in clientJS).value
  "./amf-client/js/build-scripts/buildjs.sh".!
}

publish := {
  val _ = (publish.value, publishJS.value)
  ()
}

addCommandAlias(
  "buildCommandLine",
  "; clean; clientJVM/assembly"
)
