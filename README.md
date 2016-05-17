
# vagrant-spark-cluster #
Es un proyecto Vagrant que permite configurar un clúster Spark integrado a un sistema de ficheros YARN. Inicialmente se trabaja con el modelo standalone de Spark integrado con un sistema de ficheros YARN de tres nodos como mínimo.

## Prerequisitos del Clúster ##
* Cada maquina virtual por defecto tendrán 1GB de RAM. Por defecto, la instalación crea 4 maquinas virtuales por tanto la instalación necesita que el huésped tenga como mínimo 8GB de RAM.
* El proyecto esta probado con Ubuntu 32-bit 14.04 LTS y Windows 10 como sistema operativo en el huésped.
* El vagrant box será descargado en Linux en la ruta __~/.vagrant.d/boxes__. En Windows, será en __C:/Users/{your-username}/.vagrant.d/boxes__.
* Es necesario mantener el carácter de fin de línea de UNIX. En caso que se este utilizando Windows, es necesario comprobar en los scripts de instalación que no ha cambio ninguno.

## Instalando Clúster ##

### Preparando el Huésped ###
Es necesario instalar las siguientes aplicaciones en el ordenador huésped:

* Instalar Notepad++.
* Instalar Vagrant 1.7 o superior.
* Instalar Virtual Box 5.16 o superior.
* Instalar Git para Windows con soporte a Git Bash.

### Primeros Pasos ###
Para continuar con la instalación de este entorno es necesario seguir los siguientes pasos desde la consola "Git Bash":

* Creamos el directorio “vm-spark”.
```
mkdir vm-spark
```
* Descargamos el box del SO con el siguiente comando:
```
vagrant box add centos65 http://files.brianbirkinbine.com/vagrant-centos-65-i386-minimal.box
```
* Descargamos el proyecto “vagrant-spark-cluster” con el siguiente comando:
```
git clone https://github.com/ceal1818/vagrant-spark-cluster.git vm-spark
```
* Nos ubicamos en el directorio “resources” del directorio “vm-spark”.
* Descargamos Hadoop 2.6.0
```
curl -O https://archive.apache.org/dist/hadoop/core/hadoop-2.6.0/hadoop-2.6.0.tar.gz
```
* Descargamos Spark 1.1.1 integrado con Hadoop 2.4
```
curl -O http://d3kbcqa49mib13.cloudfront.net/spark-1.1.1-bin-hadoop2.4.tgz
```

### Desde el Navegador ###

Para descargar Java es necesario hacerlo desde el navegador, siguiendo los siguientes pasos:
* Descargamos Java 8 desde esta [página](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
* Aceptamos la condiciones y descargamos el fichero __jdk-8u77-linux-i586.tar.gz__
* Movemos el fichero __jdk-8u77-linux-i586.tar.gz__ al directorio "resources".

### Instalando el Clúster ###
Para instalar e iniciar el clúster se debe ejecutar el comando `vagrant up` (la primera vez que se ejecute este comando, se ejecutará la creación de la maquinas virtuales y la instalación de todos los aplicativos).

Es importante en este punto conocer los siguientes comandos vagrant:

* `vagrant up`: Inicia e instala la(s) maquina(s) indicadas por el proyecto vagrant.
* `vagrant halt`: Detiene la(s) maquina(s) virtuales que pertenecen al proyecto vagrant. 
* `vagrant destroy`: Destruye las maquinas virtuales creadas por el proyecto vagrant.
* `vagrant ssh [nombre de vm]`: Iniciar el acceso ssh a la maquina virtual indicada en los corchetes.

## Iniciamos los Servicios ##

Para empezar a trabajar con Spark (si el clúster estaba apagado), es necesario que arranquemos los servicios YARN y Spark. Para ello es necesario seguir los siguientes pasos:
* Entramos en el nodo 1, con el siguiente comando: `vagrant ssh node-1`.
* Formateamos el sistema de ficheros HDFS con el siguiente comando:
```
$HADOOP_PREFIX/bin/hdfs namenode -format myhadoop
```
* Para iniciar todos los servicios y trabajar en cualquiera de los nodos debemos estar logados como root, para ello ejecutamos el comando `sudo su` en cuanto estemos dentro de cualquiera de los nodos del clúster.
* Desde el nodo 1, iniciamos los nodos HDFS con los siguientes comandos:
```
$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode
$HADOOP_PREFIX/sbin/hadoop-daemons.sh --config $HADOOP_CONF_DIR --script hdfs start datanode
```
* Desde el nodo 2, iniciamos los servicios YARN con los siguientes comandos:
```
$HADOOP_YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start resourcemanager
$HADOOP_YARN_HOME/sbin/yarn-daemons.sh --config $HADOOP_CONF_DIR start nodemanager
$HADOOP_YARN_HOME/sbin/yarn-daemon.sh start proxyserver --config $HADOOP_CONF_DIR
$HADOOP_PREFIX/sbin/mr-jobhistory-daemon.sh start historyserver --config $HADOOP_CONF_DIR
```
* Desde el nodo 1, arrancamos los servicios Spark con el siguiente comando: `$SPARK_HOME/sbin/start-all.sh`

## Probamos Instalación ##
Para probar que todo se ha instalado correctamente podemos hacer la siguiente prueba de Spark integrado con YARN:

```
$SPARK_HOME/bin/spark-submit --class org.apache.spark.examples.SparkPi \
    --master yarn-cluster \
    --num-executors 10 \
    --executor-cores 2 \
    /usr/local/spark/lib/spark-examples*.jar \
    100
```

También podamos comprobar el estado de la instalación accediendo al spark-shell a través del siguiente comando:

`$SPARK_HOME/bin/spark-shell --master spark://node1:7077`

Otro modo de probar la instalación y el estado de los distintos servicios es a través de las siguientes herramientas:

* [NameNode](http://10.211.55.101:50070/dfshealth.html)
* [ResourceManager](http://10.211.55.102:8088/cluster)
* [JobHistory](http://10.211.55.102:19888/jobhistory)
* [Spark](http://10.211.55.101:8080/)

