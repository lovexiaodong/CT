package com.zyd.scala

import org.apache.spark.{SparkConf, SparkContext}

class PeopleScala {
}

object PeopleScala{
  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("PeopleScala").setMaster("local")
val conf = new SparkConf().setAppName("PeopleScala")
    val sc = new SparkContext(conf)

//    val dataFile = sc.textFile("E:\\潭州课堂\\temp\\people_info.text")

    val dataFile = sc.textFile(args(0))

    val melData = dataFile.filter(line => line.contains("M")).map(line => line.split(" ")(1) + " " + line.split(" ")(2))
    val femelData = dataFile.filter(line => line.contains("F")).map(line => line.split(" ")(1) + " " + line.split(" ")(2))

    val melHighdata = melData.map(line => line.split(" ")(1).toInt)
    val femelHighdata = femelData.map(line => line.split(" ")(1).toInt)

    val lowMelHigh = melHighdata.sortBy(x => x, true).first()
    val lowFeMelHigh = femelHighdata.sortBy(x => x, true).first()

    val maxMelHigh = melHighdata.sortBy(x => x, false).first()
    val maxFeMelHigh = femelHighdata.sortBy(x => x, false).first()
    println("M max high = " + maxMelHigh)
    println("F max high = " + maxFeMelHigh)
    println("M min high = " + lowMelHigh)
    println("F min high = " + lowFeMelHigh)



  }
}
