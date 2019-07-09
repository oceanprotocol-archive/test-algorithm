package com.oceanprotocol.wordcount

import java.io.{File, PrintWriter}

import scala.annotation.tailrec
import scala.collection.immutable.ListMap


object WordCount {

  val usage = """
    Usage: WordCount --input0 path/to/input_file0  --input1 path/to/input_file1 --output path/to/output_file --logs path/to/log_file
  """

  val outputFileName = "output.txt"

  type OptionMap = Map[Symbol, Any]

  def nextParameter(map : OptionMap, list: List[String]) : OptionMap = {


    list match {
      case Nil => map
      case head :: value :: tail if head.startsWith("--input")  =>
          nextParameter(map ++ Map('input -> List(value).++( map.getOrElse('input, List()).asInstanceOf[List[String]])), tail)
      case "--output" :: value :: tail => nextParameter(map ++ Map('output -> value), tail)
      case "--logs" :: value :: tail => nextParameter(map ++ Map('logs -> value), tail)
      case option :: tail => println("Unknown option "+option)
                            println(usage)
                            System.exit(1)
                            null
    }
  }

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  def getLines(inputs:List[String]):Iterator[String] = {

    @tailrec
    def concatFile(files:List[File], currentIterator:Iterator[String]):Iterator[String] = {

      files match {
        case Nil => currentIterator
        case file :: tail =>  concatFile(tail, scala.io.Source.fromFile(file).getLines ++ currentIterator)
      }

    }

    concatFile(inputs.flatMap(getListOfFiles(_)), Iterator())

  }

  def writeResult(result: Map[String, Int], outputPath:String) =
    new PrintWriter(outputPath) {
      result.foreach {
        case (k, v) =>
          write(k + "," + v)
          write("\n")
      }
      close()
    }


  def main(args: Array[String]): Unit = {

    if (args.length == 0) println(usage)

    val arglist = args.toList
    val options = nextParameter(Map(), arglist)
    println(options)

   val counter = getLines(options('input).asInstanceOf[List[String]])
      .flatMap(_.split("\\W+"))
      .foldLeft(Map.empty[String, Int]){
        (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
      }

    val orderedCounter = ListMap(counter.toSeq.sortWith(_._2 > _._2):_*)

    println( orderedCounter)

    writeResult(orderedCounter, options('output).asInstanceOf[String].concat(File.separator).concat(outputFileName))

  }

}
