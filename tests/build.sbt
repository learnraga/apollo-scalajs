enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.19" % Test
libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.6" % Test

npmDependencies in Compile += "@apollo/client" -> "3.11.10"

Test / webpack / version               := "5.96.1"
installJsdom /version := "25.0.1"


Test / npmDependencies += "react" -> "18.2.0"
Test / npmDependencies += "react-dom" -> "18.2.0"

Compile / npmDependencies += "unfetch" -> "2.1.1"

Test / requireJsDomEnv := true

val namespace = "com.apollographql.scalajs"

(Test / sourceGenerators) += Def.task {
  import scala.sys.process._

  val out = (Test / sourceManaged).value

  out.mkdirs()

  val graphQLScala = out / "queries.scala"

  Seq(
    "apollo",
    "client:codegen",
    "--config",
    "tests/src/test/graphql/queries/apollo.config.js",
    "--target",
    "scala",
    "--namespace",
    namespace,
    graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

(Test / sourceGenerators) += Def.task {
  import scala.sys.process._

  val out = (Test / sourceManaged).value

  out.mkdirs()

  val graphQLScala = out / "mutations.scala"

  Seq(
    "apollo",
    "client:codegen",
    "--config",
    "tests/src/test/graphql/mutations/apollo.config.js",
    "--target",
    "scala",
    "--namespace",
    namespace,
    graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

Test / watchSources ++= ((Test / sourceDirectory).value / "graphql" ** "*.graphql").get

scalaJSLinkerConfig ~= { _.withESFeatures(_.withUseECMAScript2015(false)) }
