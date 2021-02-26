import sbt._

object Dependencies {
  case object org {
    case object typelevel {
      val `cats-core` =
        "org.typelevel" %% "cats-core" % "2.4.2"

      val `cats-effect` =
        "org.typelevel" %% "cats-effect" % "2.3.3"

      val `kind-projector` =
        "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
    }

    case object augustjune {
      val `context-applied` =
        "org.augustjune" %% "context-applied" % "0.1.4"
    }
  }
}