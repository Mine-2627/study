# Java8新特性：Stream流详解

 

# 1. Stream初体验

我们先来看看Java里面是怎么定义Stream的：

> A sequence of elements supporting sequential and parallel aggregate operations.

我们来解读一下上面的那句话：

1. Stream是元素的集合，这点让Stream看起来用些类似Iterator；
2. 可以支持顺序和并行的对原Stream进行汇聚的操作；

大家可以把Stream当成一个高级版本的Iterator。原始版本的Iterator，用户只能一个一个的遍历元素并对其执行某些操作；高级版本的Stream，用户只要给出需要对其包含的元素执行什么操作，比如“过滤掉长度大于10的字符串”、“获取每个字符串的首字母”等，具体这些操作如何应用到每个元素上，就给Stream就好了！（这个秘籍，一般人我不告诉他：））大家看完这些可能对Stream还没有一个直观的认识，莫急，咱们来段代码。

//Lists是Guava中的一个工具类

List<Integer> nums = Lists.newArrayList(1,null,3,4,null,6);

nums.stream().filter(num -> num != null).count();

上面这段代码是获取一个List中，元素不为null的个数。这段代码虽然很简短，但是却是一个很好的入门级别的例子来体现如何使用Stream，正所谓“麻雀虽小五脏俱全”。我们现在开始深入解刨这个例子，完成以后你可能可以基本掌握Stream的用法！

剖析Stream通用语法

![img](https://img-blog.csdnimg.cn/20190413091145183.jpeg)

图片就是对于Stream例子的一个解析，可以很清楚的看见：原本一条语句被三种颜色的框分割成了三个部分。红色框中的语句是一个Stream的生命开始的地方，负责创建一个Stream实例；绿色框中的语句是赋予Stream灵魂的地方，把一个Stream转换成另外一个Stream，红框的语句生成的是一个包含所有nums变量的Stream，进过绿框的filter方法以后，重新生成了一个过滤掉原nums列表所有null以后的Stream；蓝色框中的语句是丰收的地方，把Stream的里面包含的内容按照某种算法来汇聚成一个值，例子中是获取Stream中包含的元素个数。如果这样解析以后，还不理解，那就只能动用“核武器”–图形化，一图抵千言！

![img](https://img-blog.csdnimg.cn/20190413091145225.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

在此我们总结一下使用Stream的基本步骤：

1. 创建Stream；
2. 转换Stream，每次转换原有Stream对象不改变，返回一个新的Stream对象（**可以有多次转换**）；
3. 对Stream进行聚合（Reduce）操作，获取想要的结果；

# 2. 创建Stream

最常用的创建Stream有两种途径：

1. 通过Stream接口的静态工厂方法（注意：Java8里接口可以带静态方法）；
2. 通过Collection接口的默认方法（默认方法：Default method，也是Java8中的一个新特性，就是接口中的一个带有实现的方法，后续文章会有介绍）–stream()，把一个Collection对象转换成Stream

2.1 使用Stream静态方法来创建Stream

1. of方法：有两个overload方法，一个接受变长参数，一个接口单一值

```
Stream<Integer> integerStream = Stream.of(1, 2, 3, 5);

Stream<String> stringStream = Stream.of("taobao");
```



2. generator方法：生成一个无限长度的Stream，其元素的生成是通过给定的Supplier（这个接口可以看成一个对象的工厂，每次调用返回一个给定类型的对象）

```
Stream.generate(new Supplier<Double>() {

@Override

public Double get() {

return Math.random();

}

});

Stream.generate(() -> Math.random());

Stream.generate(Math::random);
```

三条语句的作用都是一样的，只是使用了lambda表达式和方法引用的语法来简化代码。每条语句其实都是生成一个无限长度的Stream，其中值是随机的。这个无限长度Stream是懒加载，一般这种无限长度的Stream都会配合Stream的limit()方法来用。

3. iterate方法：也是生成无限长度的Stream，和generator不同的是，其元素的生成是重复对给定的种子值(seed)调用用户指定函数来生成的。其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环

```
Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);
```

这段代码就是先获取一个无限长度的正整数集合的Stream，然后取出前10个打印。千万记住使用limit方法，不然会无限打印下去。

2.2 通过Collection子类获取Stream

这个在本文的第一个例子中就展示了从List对象获取其对应的Stream对象，如果查看Java doc就可以发现Collection接口有一个stream方法，所以其所有子类都都可以获取对应的Stream对象。

```
public interface Collection<E> extends Iterable<E> {

 //其他方法省略

 default Stream<E> stream() {

 return StreamSupport.stream(spliterator(), false);

 }

}
```



# 3. 转换Stream

转换Stream其实就是把一个Stream通过某些行为转换成一个新的Stream。Stream接口中定义了几个常用的转换方法，下面我们挑选几个常用的转换方法来解释。

1. distinct: 对于Stream中包含的元素进行去重操作（去重逻辑依赖元素的equals方法），新生成的Stream中没有重复的元素；

distinct方法示意图(**以下所有的示意图都要感谢[RxJava](https://github.com/Netflix/RxJava)项目的doc中的图片给予的灵感, 如果示意图表达的有错误和不准确的地方，请直接联系我。**)：

![img](https://img-blog.csdnimg.cn/20190413091145238.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

2. filter: 对于Stream中包含的元素使用给定的过滤函数进行过滤操作，新生成的Stream只包含符合条件的元素；

filter方法示意图：

![img](https://img-blog.csdnimg.cn/20190413091145229.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

3. map: 对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。这个方法有三个对于原始类型的变种方法，分别是：mapToInt，mapToLong和mapToDouble。这三个方法也比较好理解，比如mapToInt就是把原始Stream转换成一个新的Stream，这个新生成的Stream中的元素都是int类型。之所以会有这样三个变种方法，可以免除自动装箱/拆箱的额外消耗；

map方法示意图：

![img](https://img-blog.csdnimg.cn/20190413091145283.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

4. flatMap：和map类似，不同的是其每个元素转换得到的是Stream对象，会把子Stream中的元素压缩到父集合中；

flatMap方法示意图：

![img](https://img-blog.csdnimg.cn/20190413091145279.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

5. peek: 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数；

peek方法示意图：

![img](https://img-blog.csdnimg.cn/20190413091145274.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

6. limit: 对一个Stream进行截断操作，获取其前N个元素，如果原Stream中包含的元素个数小于N，那就获取其所有的元素；

limit方法示意图：

![img](https://img-blog.csdnimg.cn/20190413091145279.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

7. skip: 返回一个丢弃原Stream的前N个元素后剩下元素组成的新Stream，如果原Stream中包含的元素个数小于N，那么返回空Stream；

skip方法示意图：

![img](https://img-blog.csdnimg.cn/20190413091145280.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

8. 在一起,在一起！

```
List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);

System.out.println(“sum is:”+nums.stream().filter(num -> num != null).

distinct().mapToInt(num -> num * 2).

peek(System.out::println).skip(2).limit(4).sum());
```

这段代码演示了上面介绍的所有转换方法（除了flatMap），简单解释一下这段代码的含义：给定一个Integer类型的List，获取其对应的Stream对象，然后进行过滤掉null，再去重，再每个元素乘以2，再每个元素被消费的时候打印自身，在跳过前两个元素，最后去前四个元素进行加和运算(解释一大堆，很像废话，因为基本看了方法名就知道要做什么了。这个就是声明式编程的一大好处！)。大家可以参考上面对于每个方法的解释，看看最终的输出是什么。

9. 性能问题

有些细心的同学可能会有这样的疑问：在对于一个Stream进行多次转换操作，每次都对Stream的每个元素进行转换，而且是执行多次，这样时间复杂度就是一个for循环里把所有操作都做掉的N（转换的次数）倍啊。其实不是这样的，转换操作都是lazy的，多个转换操作只会在汇聚操作（见下节）的时候融合起来，一次循环完成。我们可以这样简单的理解，Stream里有个操作函数的集合，每次转换操作就是把转换函数放入这个集合中，在汇聚操作的时候循环Stream对应的集合，然后对每个元素执行所有的函数。

# 4. 汇聚（Reduce）Stream

> 汇聚这个词，是我自己翻译的，如果大家有更好的翻译，可以在下面留言。在官方文档中是reduce，也叫fold。

在介绍汇聚操作之前，我们先看一下Java doc中对于其定义：

> A reduction operation (also called a fold) takes a sequence of input elements and combines them into a single summary result by repeated application of a combining operation, such as finding the sum or maximum of a set of numbers, or accumulating elements into a list. The streams classes have multiple forms of general reduction operations, called reduce() and collect(), as well as multiple specialized reduction forms such as sum(), max(), or count().

简单翻译一下：汇聚操作（也称为折叠）接受一个元素序列为输入，反复使用某个合并操作，把序列中的元素合并成一个汇总的结果。比如查找一个数字列表的总和或者最大值，或者把这些数字累积成一个List对象。Stream接口有一些通用的汇聚操作，比如reduce()和collect()；也有一些特定用途的汇聚操作，比如sum(),max()和count()。注意：sum方法不是所有的Stream对象都有的，只有IntStream、LongStream和DoubleStream是实例才有。

下面会分两部分来介绍汇聚操作：

1. 可变汇聚：把输入的元素们累积到一个可变的容器中，比如Collection或者StringBuilder；
2. 其他汇聚：除去可变汇聚剩下的，一般都不是通过反复修改某个可变对象，而是通过把前一次的汇聚结果当成下一次的入参，反复如此。比如reduce，count，allMatch；

4.1 可变汇聚

可变汇聚对应的只有一个方法：collect，正如其名字显示的，它可以把Stream中的要有元素收集到一个结果容器中（比如Collection）。先看一下最通用的collect方法的定义（还有其他override方法）：

```
<R> R collect(Supplier<R> supplier,

 BiConsumer<R, ? super T> accumulator,

 BiConsumer<R, R> combiner);
```

先来看看这三个参数的含义：Supplier supplier是一个工厂函数，用来生成一个新的容器；BiConsumer accumulator也是一个函数，用来把Stream中的元素添加到结果容器中；BiConsumer combiner还是一个函数，用来把中间状态的多个结果容器合并成为一个（并发的时候会用到）。看晕了？来段代码！

```
List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);

 List<Integer> numsWithoutNull = nums.stream().filter(num -> num != null).

 collect(() -> new ArrayList<Integer>(),

(list, item) -> list.add(item),

 (list1, list2) -> list1.addAll(list2));
```

上面这段代码就是对一个元素是Integer类型的List，先过滤掉全部的null，然后把剩下的元素收集到一个新的List中。进一步看一下collect方法的三个参数，都是lambda形式的函数（*上面的代码可以使用方法引用来简化，留给读者自己去思考*）。

- 第一个函数生成一个新的ArrayList实例；
- 第二个函数接受两个参数，第一个是前面生成的ArrayList对象，二个是stream中包含的元素，函数体就是把stream中的元素加入ArrayList对象中。第二个函数被反复调用直到原stream的元素被消费完毕；
- 第三个函数也是接受两个参数，这两个都是ArrayList类型的，函数体就是把第二个ArrayList全部加入到第一个中；

但是上面的collect方法调用也有点太复杂了，没关系！我们来看一下collect方法另外一个override的版本，其依赖[Collector](http://docs.oracle.com/javase/8/docs/api/java/util/stream/Collector.html)。

```
<R, A> R collect(Collector<? super T, A, R> collector);
```

这样清爽多了！少年，还有好消息，Java8还给我们提供了Collector的工具类–[Collectors](http://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html)，其中已经定义了一些静态工厂方法，比如：Collectors.toCollection()收集到Collection中, Collectors.toList()收集到List中和Collectors.toSet()收集到Set中。这样的静态方法还有很多，这里就不一一介绍了，大家可以直接去看JavaDoc。下面看看使用Collectors对于代码的简化：

```
List<Integer> numsWithoutNull = nums.stream().filter(num -> num != null).

 collect(Collectors.toList());
```

4.2 其他汇聚

– reduce方法：reduce方法非常的通用，后面介绍的count，sum等都可以使用其实现。reduce方法有三个override的方法，本文介绍两个最常用的，最后一个留给读者自己学习。先来看reduce方法的第一种形式，其方法定义如下：

```
Optional<T> reduce(BinaryOperator<T> accumulator);
```

接受一个BinaryOperator类型的参数，在使用的时候我们可以用lambda表达式来。

```
List<Integer> ints = Lists.newArrayList(1,2,3,4,5,6,7,8,9,10);

System.out.println("ints sum is:" + ints.stream().reduce((sum, item) -> sum + item).get());
```

可以看到reduce方法接受一个函数，这个函数有两个参数，第一个参数是上次函数执行的返回值（也称为中间结果），第二个参数是stream中的元素，这个函数把这两个值相加，得到的和会被赋值给下次执行这个函数的第一个参数。要注意的是：**第一次执行的时候第一个参数的值是Stream的第一个元素，第二个参数是Stream的第二个元素**。这个方法返回值类型是Optional，这是Java8防止出现NPE的一种可行方法，后面的文章会详细介绍，这里就简单的认为是一个容器，其中可能会包含0个或者1个对象。

这个过程可视化的结果如图：

![img](https://img-blog.csdnimg.cn/20190413091145280.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zODI5NDk5OQ==,size_16,color_FFFFFF,t_70)

reduce方法还有一个很常用的变种：

```
T reduce(T identity, BinaryOperator<T> accumulator);
```

这个定义上上面已经介绍过的基本一致，不同的是：它允许用户提供一个循环计算的初始值，如果Stream为空，就直接返回该值。而且这个方法不会返回Optional，因为其不会出现null值。下面直接给出例子，就不再做说明了。

```
List<Integer> ints = Lists.newArrayList(1,2,3,4,5,6,7,8,9,10);

System.out.println("ints sum is:" + ints.stream().reduce(0, (sum, item) -> sum + item));
```

– count方法：获取Stream中元素的个数。比较简单，这里就直接给出例子，不做解释了。

```
List<Integer> ints = Lists.newArrayList(1,2,3,4,5,6,7,8,9,10);

System.out.println("ints sum is:" + ints.stream().count());
```

– 搜索相关

```
allMatch：是不是Stream中的所有元素都满足给定的匹配条件

anyMatch：Stream中是否存在任何一个元素满足匹配条件

findFirst: 返回Stream中的第一个元素，如果Stream为空，返回空Optional

noneMatch：是不是Stream中的所有元素都不满足给定的匹配条件

max和min：使用给定的比较器（Operator），返回Stream中的最大|最小值
```

下面给出allMatch和max的例子，剩下的方法读者当成练习。

```
List<Integer> ints = Lists.newArrayList(1,2,3,4,5,6,7,8,9,10);

System.out.println(ints.stream().allMatch(item -> item < 100));

ints.stream().max((o1, o2) -> [o1.compareTo(o2)).ifPresent(System.out::println);
```



# 5.Stream常用

Stream 就如同一个迭代器（Iterator），单向，不可往复，数据只能遍历一次，遍历过一次后即用尽了，就好比流水从面前流过，一去不复返。

有多种方式生成 Stream Source：

从 Collection 和数组

```
Collection.stream()

Collection.parallelStream()

Arrays.stream(T array) or Stream.of()
```

从 BufferedReader

```
java.io.BufferedReader.lines()
```

静态工厂

```
java.util.stream.IntStream.range()

java.nio.file.Files.walk()
```

自己构建

```
java.util.Spliterator
```

其它

```
Random.ints()

BitSet.stream()

Pattern.splitAsStream(java.lang.CharSequence)

JarFile.stream()
```

### 1.流(stream)的操作类型分为两种：

1. Intermediate：一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历。

2. Terminal：一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果，或者一个 side effect。


在对于一个 Stream 进行多次转换操作 (Intermediate 操作)，每次都对 Stream 的每个元素进行转换，而且是执行多次，这样时间复杂度就是 N（转换次数）个 for 循环里把所有操作都做掉的总和吗？其实不是这样的，转换操作都是 lazy 的，多个转换操作只会在 Terminal 操作的时候融合起来，一次循环完成。我们可以这样简单的理解，Stream 里有个操作函数的集合，每次转换操作就是把转换函数放入这个集合中，在 Terminal 操作的时候循环 Stream 对应的集合，然后对每个元素执行所有的函数。

还有一种操作被称为 short-circuiting。用以指：

1. 对于一个 intermediate 操作，如果它接受的是一个无限大（infinite/unbounded）的 Stream，但返回一个有限的新 Stream。

2. 对于一个 terminal 操作，如果它接受的是一个无限大的 Stream，但能在有限的时间计算出结果。


### 2.流的使用详解

简单说，对 Stream 的使用就是实现一个 filter-map-reduce 过程，产生一个最终结果，或者导致一个副作用（side effect）

### 3.流的构造与转换

下面提供最常见的几种构造 Stream 的样例。

```
// 1. Individual values
Stream stream = Stream.of("a", "b", "c");
// 2. Arrays
String [] strArray = new String[] {"a", "b", "c"};
stream = Stream.of(strArray);
stream = Arrays.stream(strArray);
// 3. Collections
List<String> list = Arrays.asList(strArray);
stream = list.stream();
```

需要注意的是，对于基本数值型，目前有三种对应的包装类型 Stream：

IntStream、LongStream、DoubleStream。当然我们也可以用 Stream<Integer>、Stream<Long> >、Stream<Double>，但是 boxing 和 unboxing 会很耗时，所以特别为这三种基本数值型提供了对应的 Stream。

数值流的构造

```
IntStream.of(new int[]{1, 2, 3}).forEach(System.out::println);
IntStream.range(1, 3).forEach(System.out::println);
IntStream.rangeClosed(1, 3).forEach(System.out::println);
```

流转换为其它数据结构

```java
// 1. Array
String[] strArray1 = stream.toArray(String[]::new);
// 2. Collection
List<String> list1 = stream.collect(Collectors.toList());
List<String> list2 = stream.collect(Collectors.toCollection(ArrayList::new));
Set set1 = stream.collect(Collectors.toSet());
Stack stack1 = stream.collect(Collectors.toCollection(Stack::new));
// 3. String
String str = stream.collect(Collectors.joining()).toString();
```

一个 Stream 只可以使用一次，上面的代码只是示例，为了简洁而重复使用了数次(正常开发只能使用一次)。

### 4.流的操作

接下来，当把一个数据结构包装成 Stream 后，就要开始对里面的元素进行各类操作了。常见的操作可以归类如下。

```java
Intermediate：

map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered

Terminal：

forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator

Short-circuiting：

anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
```

典型用法示例

#### map/flatMap

//转换大写，wordList为单词集合List<String>类型

```
List<String> output = wordList.stream().map(String::toUpperCase).collect(Collectors.toList());
```

求平方数

//这段代码生成一个整数 list 的平方数 {1, 4, 9, 16}。

```
List<Integer> nums = Arrays.asList(1, 2, 3, 4);
List<Integer> squareNums = nums.stream().map(n -> n * n).collect(Collectors.toList());
```

从上面例子可以看出，map 生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap。

//将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。

```
Stream<List<Integer>> inputStream = Stream.of(
 Arrays.asList(1),
 Arrays.asList(2, 3),
 Arrays.asList(4, 5, 6)
 );
Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
List<Integer> list =outputStream.collect(Collectors.toList());
System.out.println(list.toString());
```

#### filter

filter 对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream。

```
//留下偶数，经过条件“被 2 整除”的 filter，剩下的数字为 {2, 4, 6}。
Integer[] sixNums = {1, 2, 3, 4, 5, 6};
Integer[] evens =Stream.of(sixNums).filter(n -> n%2 == 0).toArray(Integer[]::new);
//把每行的单词用 flatMap 整理到新的 Stream，然后保留长度不为 0 的，就是整篇文章中的全部单词了。
//REGEXP为正则表达式，具体逻辑具体分析
List<String> output = reader.lines().
    flatMap(line -> Stream.of(line.split(REGEXP))).
    filter(word -> word.length() > 0).collect(Collectors.toList());
forEach

forEach 方法接收一个 Lambda 表达式，然后在 Stream 的每一个元素上执行该表达式。

//打印所有男性姓名，roster为person集合类型为List<Pserson>
// Java 8
roster.stream().filter(p -> p.getGender() == Person.Sex.MALE).forEach(p -> System.out.println(p.getName()));
// Pre-Java 8
for (Person p : roster) {
    if (p.getGender() == Person.Sex.MALE) {
        System.out.println(p.getName());
    }
}
```

当需要为多核系统优化时，可以 parallelStream().forEach()，只是此时原有元素的次序没法保证，并行的情况下将改变串行时操作的行为，此时 forEach 本身的实现不需要调整，而 Java8 以前的 for 循环 code 可能需要加入额外的多线程逻辑。

另外一点需要注意，forEach 是 terminal 操作，因此它执行后，Stream 的元素就被“消费”掉了，你无法对一个 Stream 进行两次 terminal 运算。下面代码是错误的

```
//错误代码示例，一个stream不可以使用两次
stream.forEach(element -> doOneThing(element));
stream.forEach(element -> doAnotherThing(element));
```

相反，具有相似功能的 intermediate 操作 peek 可以达到上述目的。如下是出现在该 api javadoc 上的一个示例。

```
// peek 对每个元素执行操作并返回一个新的 Stream
Stream.of("one", "two", "three", "four")
     .filter(e -> e.length() > 3)
     .peek(e -> System.out.println("Filtered value: " + e))
     .map(String::toUpperCase)
     .peek(e -> System.out.println("Mapped value: " + e))
     .collect(Collectors.toList());
```

#### findFirst

这是一个 termimal 兼 short-circuiting 操作，它总是返回 Stream 的第一个元素，或者空。

这里比较重点的是它的返回值类型：Optional。这也是一个模仿 Scala 语言中的概念，作为一个容器，它可能含有某值，或者不包含。使用它的目的是尽可能避免 NullPointerException。

```
String strA = " abcd ", strB = null;
print(strA);
print("");
print(strB);
getLength(strA);
getLength("");
getLength(strB);
//输出text不为null的值
public static void print(String text) {
    // Java 8
     Optional.ofNullable(text).ifPresent(System.out::println);
    // Pre-Java 8
     if (text != null) {
        System.out.println(text);
     }
 }
//输出text的长度，避免空指针
public static int getLength(String text) {
    // Java 8
    return Optional.ofNullable(text).map(String::length).orElse(-1);
    // Pre-Java 8
    //return if (text != null) ? text.length() : -1;
}
```

在更复杂的 if (xx != null) 的情况中，使用 Optional 代码的可读性更好，而且它提供的是编译时检查，能极大的降低 NPE 这种 Runtime Exception 对程序的影响，或者迫使程序员更早的在编码阶段处理空值问题，而不是留到运行时再发现和调试。

Stream 中的 findAny、max/min、reduce 等方法等返回 Optional 值。还有例如 IntStream.average() 返回 OptionalDouble 等等

#### reduce

这个方法的主要作用是把 Stream 元素组合起来。它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。也有没有起始值的情况，这时会把 Stream 的前面两个元素组合起来，返回的是 Optional。

```
// reduce用例
// 字符串连接，concat = "ABCD"
String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat); 
// 求最小值，minValue = -3.0
double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min); 
// 求和，sumValue = 10, 有起始值
int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
// 求和，sumValue = 10, 无起始值，返回Optional,所以有get()方法
sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
// 过滤，字符串连接，concat = "ace"
concat = Stream.of("a", "B", "c", "D", "e", "F")
    .filter(x -> x.compareTo("Z") > 0)
    .reduce("", String::concat);
```

#### limit/skip

limit 返回 Stream 的前面 n 个元素；skip 则是扔掉前 n 个元素（它是由一个叫 subStream 的方法改名而来）。

```
// limit 和 skip 对运行次数的影响
public void testLimitAndSkip() {
     List<Person> persons = new ArrayList();
     for (int i = 1; i <= 10000; i++) {
         Person person = new Person(i, "name" + i);
         persons.add(person);
    }
    List<String> personList2 = persons.stream()
        .map(Person::getName).limit(10).skip(3)
        .collect(Collectors.toList());
    

System.out.println(personList2);

}
private class Person {
    public int no;
    private String name;
    public Person (int no, String name) {
        this.no = no;
        this.name = name;
    }
     public String getName() {
        System.out.println(name);
        return name;
     }
}
```

//结果

```
name1
name2
name3
name4
name5
name6
name7
name8
name9
name10
[name4, name5, name6, name7, name8, name9, name10]
```

//这是一个有 10，000 个元素的 Stream，但在 short-circuiting 操作 limit 和 skip 的作用下，管道中 map 操作指定的 getName() 方法的执行次数为 limit 所限定的 10 次，而最终返回结果在跳过前 3 个元素后只有后面 7 个返回。
有一种情况是 limit/skip 无法达到 short-circuiting 目的的，就是把它们放在 Stream 的排序操作后，原因跟 sorted 这个 intermediate 操作有关：此时系统并不知道 Stream 排序后的次序如何，所以 sorted 中的操作看上去就像完全没有被 limit 或者 skip 一样。

```
 List<Person> persons = new ArrayList();
 for (int i = 1; i <= 5; i++) {
     Person person = new Person(i, "name" + i);
     persons.add(person);
 }
List<Person> personList2 = persons.stream().sorted((p1, p2) -> 
p1.getName().compareTo(p2.getName())).limit(2).collect(Collectors.toList());
System.out.println(personList2);
```

//结果

```
name2
name1
name3
name2
name4
name3
name5
name4
[stream.StreamDW$Person@816f27d, stream.StreamDW$Person@87aac27]
```

//虽然最后的返回元素数量是 2，但整个管道中的 sorted 表达式执行次数没有像前面例子相应减少。
sorted

对 Stream 的排序通过 sorted 进行，它比数组的排序更强之处在于你可以首先对 Stream 进行各类 map、filter、limit、skip 甚至 distinct 来减少元素数量后，再排序，这能帮助程序明显缩短执行时间。

```
List<Person> persons = new ArrayList();
 for (int i = 1; i <= 5; i++) {
     Person person = new Person(i, "name" + i);
     persons.add(person);
 }
List<Person> personList2 = persons.stream().limit(2).sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
System.out.println(personList2);
```

//结果

```
name2
name1
[stream.StreamDW$Person@6ce253f1, stream.StreamDW$Person@53d8d10a]
```

#### min/max/distinct

min 和 max 的功能也可以通过对 Stream 元素先排序，再 findFirst 来实现，但前者的性能会更好，为 O(n)，而 sorted 的成本是 O(n log n)。同时它们作为特殊的 reduce 方法被独立出来也是因为求最大最小值是很常见的操作。

//找出最长一行的长度

```
BufferedReader br = new BufferedReader(new FileReader("c:\\Service.log"));
int longest = br.lines().mapToInt(String::length).max().getAsInt();
br.close();
System.out.println(longest);
//找出全文的单词，转小写，并排序,使用 distinct 来找出不重复的单词。单词间只有空格
List<String> words = br.lines()
    .flatMap(line -> Stream.of(line.split(" ")))
    .filter(word -> word.length() > 0)
    .map(String::toLowerCase)
    .distinct().sorted()
    .collect(Collectors.toList());
br.close();
System.out.println(words);
```

#### Match

Stream 有三个 match 方法，从语义上说：

allMatch：Stream 中全部元素符合传入的 predicate，返回 true

anyMatch：Stream 中只要有一个元素符合传入的 predicate，返回 true

noneMatch：Stream 中没有一个元素符合传入的 predicate，返回 true

它们都不是要遍历全部元素才能返回结果。例如 allMatch 只要一个元素不满足条件，就 skip 剩下的所有元素，返回 false。

//Match使用示例

```
List<Person> persons = new ArrayList();
persons.add(new Person(1, "name" + 1, 10));
persons.add(new Person(2, "name" + 2, 21));
persons.add(new Person(3, "name" + 3, 34));
persons.add(new Person(4, "name" + 4, 6));
persons.add(new Person(5, "name" + 5, 55));
boolean isAllAdult = persons.stream().allMatch(p -> p.getAge() > 18);
System.out.println("All are adult? " + isAllAdult);
boolean isThereAnyChild = persons.stream().anyMatch(p -> p.getAge() < 12);
System.out.println("Any child? " + isThereAnyChild);
```

//结果

```
All are adult? false
Any child? true
```

#### 自己生成流

通过实现 Supplier 接口，你可以自己来控制流的生成。这种情形通常用于随机数、常量的 Stream，或者需要前后元素间维持着某种状态信息的 Stream。把 Supplier 实例传递给 Stream.generate() 生成的 Stream，默认是串行（相对 parallel 而言）但无序的（相对 ordered 而言）。由于它是无限的，在管道中，必须利用 limit 之类的操作限制 Stream 大小。

```
//生成10个随机数
Random seed = new Random();
Supplier<Integer> random = seed::nextInt;
Stream.generate(random).limit(10).forEach(System.out::println);
//Another way
IntStream.generate(() -> (int) (System.nanoTime() % 100)).
limit(10).forEach(System.out::println);
Stream.generate() 还接受自己实现的 Supplier。例如在构造海量测试数据的时候，用某种自动的规则给每一个变量赋值；或者依据公式计算 Stream 的每个元素值。这些都是维持状态信息的情形。

Stream.generate(new PersonSupplier()).
    limit(10).forEach(p -> System.out.println(p.getName() + ", " + p.getAge()));
private class PersonSupplier implements Supplier<Person> {
     private int index = 0;
     private Random random = new Random();
     @Override
     public Person get() {
        return new Person(index++, "StormTestUser" + index, random.nextInt(100));
     }
}
```

//结果

```
StormTestUser1, 9
StormTestUser2, 12
StormTestUser3, 88
StormTestUser4, 51
StormTestUser5, 22
StormTestUser6, 28
StormTestUser7, 81
StormTestUser8, 51
StormTestUser9, 4
StormTestUser10, 76
Stream.iterate
```

iterate 跟 reduce 操作很像，接受一个种子值，和一个 UnaryOperator（例如 f）。然后种子值成为 Stream 的第一个元素，f(seed) 为第二个，f(f(seed)) 第三个，以此类推

//生成等差数列

```
Stream.iterate(0, n -> n + 3).limit(10). forEach(x -> System.out.print(x + " "));
```

//结果
0 3 6 9 12 15 18 21 24 27
Stream.generate 相仿，在 iterate 时候管道必须有 limit 这样的操作来限制 Stream 大小。

用 Collectors 来进行 reduction 操作
java.util.stream.Collectors 类的主要作用就是辅助进行各类有用的 reduction 操作，例如转变输出为 Collection，把 Stream 元素进行归组。

#### groupingBy/partitioningBy

```
//按照年龄归组
Map<Integer, List<Person>> personGroups = Stream.generate(new PersonSupplier())
    .limit(100).collect(Collectors.groupingBy(Person::getAge));
Iterator it = personGroups.entrySet().iterator();
while (it.hasNext()) {
     Map.Entry<Integer, List<Person>> persons = (Map.Entry) it.next();
     System.out.println("Age " + persons.getKey() + " = " + persons.getValue().size());
}
```

//上面的 code，首先生成 100 人的信息，然后按照年龄归组，相同年龄的人放到同一个 list 中，如下的输出：

```
Age 0 = 2
Age 1 = 2
Age 5 = 2
Age 8 = 1
Age 9 = 1
Age 11 = 2
……
```

```java
Map<Boolean, List<Person>> children = Stream.generate(new PersonSupplier())
    .limit(100).collect(Collectors.partitioningBy(p -> p.getAge() < 18));
System.out.println("Children number: " + children.get(true).size());
System.out.println("Adult number: " + children.get(false).size());
```

//结果

```
Children number: 23 
Adult number: 77
```

### 5.Stream 的特性

可以归纳为：

#### 不是数据结构

它没有内部存储，它只是用操作管道从 source（数据结构、数组、generator function、IO channel）抓取数据。

它也绝不修改自己所封装的底层数据结构的数据。例如 Stream 的 filter 操作会产生一个不包含被过滤元素的新 Stream，而不是从 source 删除那些元素。

所有 Stream 的操作必须以 lambda 表达式为参数

#### 不支持索引访问

你可以请求第一个元素，但无法请求第二个，第三个，或最后一个。不过请参阅下一项。

很容易生成数组或者 List

#### 惰性化

很多 Stream 操作是向后延迟的，一直到它弄清楚了最后需要多少数据才会开始。

Intermediate 操作永远是惰性化的。

#### 并行能力

当一个 Stream 是并行化的，就不需要再写多线程代码，所有对它的操作会自动并行进行的。

#### 可以是无限的

集合有固定大小，Stream 则不必。limit(n) 和 findFirst() 这类的 short-circuiting 操作可以对无限的 Stream 进行运算并很快完成。
