package com.zyd.scala

import java.io.{File, FileWriter}

import scala.util.Random

class InforGenerator {
}
object InforGenerator{
  def main(args: Array[String]): Unit = {

    var writer = new FileWriter(new File("E:\\潭州课堂\\temp\\people_info.txt"))

    var random = new Random()
    for ( i <- 1 to 100){
      var height = random.nextInt(220)
      if(height < 50){
        height += 50
      }

      var gender = getRandomGender()
      if(height < 100 && gender == "M"){
        height += 50
      }
      if(height < 100 && gender == "F"){
        height +=100
      }

      writer.write(i + " " + gender + " " + height + "\n")
    }

    writer.flush()
    writer.close()
    println("数据生成完毕!")
  }

  def getRandomGender(): String = {
    val random = new Random();
    val randomNum = random.nextInt(2)
    if(randomNum % 2 == 0){
      "M"
    }else{
      "F"
    }
  }
}


