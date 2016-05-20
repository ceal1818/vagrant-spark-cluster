val rddFile = sc.textFile("/usr/root/input.txt")
val mapReduceResult = rddFile.flatMap((x)=>x.split(" ")).map((x)=>(x,1)).reduceByKey((_+_))
mapReduceResult.saveAsTextFile("/usr/root/mapReduceResult")