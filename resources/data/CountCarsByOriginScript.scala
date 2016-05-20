val raw = sc.textFile("/usr/root/cars-data.txt")
case class cars(make:String, model:String, mpg:Integer, cylinders:Integer, engine_disp:Integer, horsepower:Integer, weight:Integer, accelerate:Double, year:Integer, origin:String)
val data = raw.map(x=>x.split("\t"))
val carsRdd = data.map(x=>cars(x(0).toString, x(1).toString, x(2).toInt, x(3).toInt, x(4).toInt, x(5).toInt, x(6).toInt, x(7).toDouble, x(8).toInt, x(9).toString))
val countCarsByOrigin = carsRdd.map(x=>(x.origin,1)).reduceByKey((x,y)=>x+y)
countCarsByOrigin.collect

val mapMakeWeight = carsRdd.map(x=>(x.make, x.weight.toInt))
val combineMakeWeight = mapMakeWeight.combineByKey((x:Int) => (x,1),(acc:(Int, Int), x) => (acc._1 + x, acc._2 + 1),(acc1:(Int, Int), acc2:(Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
)
val mapMakeWeightAvg = combineMakeWeight.map(x=>(x._1, (x._2._1/x._2._2)))
mapMakeWeightAvg.collect