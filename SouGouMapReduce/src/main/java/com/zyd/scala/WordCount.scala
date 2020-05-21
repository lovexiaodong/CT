package com.zyd.scala

import org.apache.spark.{SparkConf, SparkContext}

class WordCount {

}

object WordCount{

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("wordCount").setMaster("local")
       var  sc = new SparkContext(conf);
//    sc.textFile("E:\\潭州课堂\\电商大数据实战项目与试验环境\\1.txt")
//      .flatMap(_.split(" "))
//      .map((_,1))
//      .reduceByKey(_+_)
//      .saveAsTextFile("E:\\潭州课堂\\电商大数据实战项目与试验环境\\2S")
    val list = sc.textFile("E:\\潭州课堂\\电商大数据实战项目与试验环境\\数据\\SogouQ.txt")
      .map((x:String) => x.split("\t"))
      .filter(_.length == 6)
      .filter(_(3).toInt == 1)
      .filter(_(4).toInt == 2)
    .map(_.mkString("\t"))
      .saveAsTextFile("E:\\潭州课堂\\电商大数据实战项目与试验环境\\3S")

//    list.collect().foreach(x=>print(x.toString))

  }
}
