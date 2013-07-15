package controllers

import play.api.mvc.{AnyContentAsFormUrlEncoded, BodyParser, AnyContent, BodyParsers}

trait MyBodyParsers {

  BodyParsers.parse.using { request =>
    request.contentType match {
      case Some("application/x-www-form-urlencoded") => { // TODO 条件追加
        play.api.Logger.trace("TODO")
          BodyParsers.parse.when(
            _.contentType.exists(_ == "application/x-www-form-urlencoded"),
            tolerantFormUrlEncodedByWindows31j(BodyParsers.parse.DEFAULT_MAX_TEXT_LENGTH),
            request => play.api.Play.maybeApplication.map(_.global.onBadRequest(request, "Expecting application/x-www-form-urlencoded body")).getOrElse(play.api.mvc.Results.BadRequest)
          )
      }
      case _ => {
        BodyParsers.parse.anyContent
      }
    }
  }

  def tolerantFormUrlEncodedByWindows31j(maxLength: Int): BodyParser[Map[String, Seq[String]]] = BodyParser("urlFormEncoded, maxLength=" + maxLength) { request =>

    import play.core.parsers._
    import scala.collection.JavaConverters._
    import play.api.libs.iteratee.{ Done, Iteratee, Traversable }
  import play.api.Play
  import play.api.mvc.Results
    import play.api.libs.iteratee.Input.Empty
    import org.apache.commons.codec.net.URLCodec

    Traversable.takeUpTo[Array[Byte]](maxLength).apply(Iteratee.consume[Array[Byte]]().map { c =>
      scala.util.control.Exception.allCatch[Map[String, Seq[String]]].either {
        val encoding = "windows-31j"
        play.api.Logger("play").trace("Parsing by \"Windows-31j\"")
        play.api.Logger("play").trace("raw request body[%s]".format(new String(c)))

        // 参考: play.core.utils.FormUrlEncodedParser
        import scala.collection.mutable.{ HashMap }
        var params = HashMap.empty[String, Seq[String]]
        new String(c).split('&').foreach { param =>
          if (param.contains('=')) {
            val parts = param.split('=')
            val key = new URLCodec().decode(parts.head, encoding)
            // TODO 先頭しか取ってないように見えるけど大丈夫か？
            val value = new URLCodec().decode(parts.tail.headOption.getOrElse(""), encoding)
            params += key -> (params.get(key).getOrElse(Seq.empty) :+ value)
          }
        }
        params.toMap



//        FormUrlEncodedParser.parse(new String(c, request.charset.getOrElse("utf-8")), request.charset.getOrElse("utf-8"))
      }.left.map { e =>
        Play.maybeApplication.map(_.global.onBadRequest(request, "Error parsing application/x-www-form-urlencoded")).getOrElse(Results.BadRequest)
      }
    }).flatMap(Iteratee.eofOrElse(Results.EntityTooLarge))
      .flatMap {
      case Left(b) => Done(Left(b), Empty)
      case Right(it) => it.flatMap {
        case Left(r) => Done(Left(r), Empty)
        case Right(urlEncoded) => Done(Right(urlEncoded), Empty)
      }
    }
  }

}
