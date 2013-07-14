package services

import com.hp.cache4guice.Cached

// trait -> impl classでもいいけど、classだけでもいい
class HogeService {

  @Cached(timeToLiveSeconds = 30) // cache時間を指定
  def hoge(i: Int): String = {
    println(s"aaaaaaaaaa${i} - ${new java.util.Date}")
    s"__fuga${i}__"
  }

}