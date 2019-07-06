package com.ourslook.guower.test;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Java8LambdaTest {

    /**
     * 请求字符串转成Map对象
     * index.jsp?itemId=1&userId=1001&password=2388&token=14324324132432414324123&key=index
     * 参数转换成为Map
     */
    @Test
    public void test1() {
        String queryStr = "itemId=1&userId=1001&password=2388&token=14324324132432414324123&key=index";
        //map是转换的中间操作，对之前的steam进行加工
        Map<String, String> params = Stream.of(queryStr.split("&")).map(str -> str.split("=")).collect(Collectors.toMap(arr -> arr[0], arr -> (arr.length > 1 ? arr[1] : "")));
        System.out.println(params);
    }

    @Test
    public void test2() {
        //List<Book> --> List<Long>
        List<Long> ids1 = books().stream().map(book -> book.getId()).collect(Collectors.toList());
        List<Long> ids2 = books().stream().map(Book::getId).collect(Collectors.toList());
        System.out.println("对象集合转成id集合：" + ids1);

        String ids3Str = books().stream().map(book -> String.valueOf(book.getId())).collect(Collectors.joining(",", "(", ")"));
        System.out.println("对象集合转成id逗号分割字符串：" + ids3Str);

        List<LocalDate> ids4 = books().stream().map(Book::getPublishTime).distinct().collect(Collectors.toList());
        Set<LocalDate> ids5 = books().stream().map(Book::getPublishTime).collect(Collectors.toSet());
        System.out.println("对象集合转成日期数组并去重：" + ids4);
        System.out.println("对象集合转成日期数组并去重：" + ids5);

        System.out.println("直接排序:");
        books().stream().sorted(Comparator.comparing(Book::getPrice).reversed().thenComparing(Book::getPublishTime)).
                //distinct().
                        forEach(System.out::println);

        System.out.println("集合转换成为Map");
        Map<Long, Book> bookMap = books().stream().collect(Collectors.toMap(Book::getId, book -> book));
        System.out.println(bookMap);

        System.out.println("寻找平均价");
        Double avePrice = books().stream().collect(Collectors.averagingDouble(Book::getPrice));
        System.out.println(avePrice);

        System.out.println("查询最贵的书籍");
        Optional<Book> maxPrice = books().stream().collect(Collectors.maxBy(Comparator.comparing(Book::getPrice)));
        System.out.println(maxPrice);
    }

    @Test
    public void test3() {
        System.out.println("测试分组Group by");
        Map<Double, List<Book>> map = books().stream().collect(Collectors.groupingByConcurrent(Book::getPrice));
        System.out.println("分组：" + map);
        System.out.println();

        Map<Double, Long> countMap = books().stream().collect(Collectors.groupingByConcurrent(Book::getPrice, Collectors.counting()));
        System.out.println("分组并计算每组数量：" + countMap);

        Map<Double, Optional<Book>> couontMapMax = books().stream().collect(Collectors.groupingByConcurrent(Book::getPrice, Collectors.maxBy(Comparator.comparing(Book::getPublishTime))));
        System.out.println("分组并计算得到每组初版最晚的书：" + couontMapMax);
        couontMapMax.keySet().forEach(key -> {
            System.out.println(key);
            try {
                couontMapMax.get(key).orElseThrow((() -> null));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        System.out.println();
        System.out.println("过滤找出80块以上的书，并且按照时间排序");
        List<Book> books = books().stream().filter(book -> book.getPrice() > 80).sorted(Comparator.comparing(Book::getPublishTime)).collect(Collectors.toList());
        System.out.println(books);
    }

    @Test
    public void test4() {
        execute(new Book());
        executeByOptional(books().get(0));
    }

    @Test
    public void test5() {

        System.out.println("Map Reduce");

        Optional<Double> allPrice = books().stream().map((book) -> book.getPrice() * book.getId()).reduce((sum, aDouble2) -> sum + aDouble2);
        System.out.println("统计所有商品总和：" + allPrice.orElse(0D));

        System.out.println();
        System.out.println("统计：（和、最大、最小、平均）");
        DoubleSummaryStatistics statistics = books().stream().mapToDouble((book) -> book.getPrice() * book.getId()).summaryStatistics();
        System.out.println("average:" + statistics.getAverage());
        System.out.println("max:" + statistics.getMax());
        System.out.println("min:" + statistics.getMin());
        System.out.println("count:" + statistics.getCount());
    }

    @Test
    public void test6() {
        List<Book> books = books();
    }

    @Test
    public void test7() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yes = LocalDateTime.of(2018, 6, 1, 10 ,11, 59);
        Duration duration = Duration.between(now, yes);
        System.out.println(duration.toDays());
        System.out.println(duration.toHours());
        System.out.println(duration.toMinutes());
        System.out.println(duration.toNanos());
    }

    private void execute(Book book) {
        System.out.println("吧书本的name变成大写");
        if (book != null) {
            String name = book.getName();
            if (name != null) {
                System.out.println(name.toUpperCase());
            }
        }
    }

    private void executeByOptional(Book book) {
        System.out.println("把书本的name变成大写 -- optional");
        String str = Optional.ofNullable(book).map(Book::getName).map(String::toUpperCase).orElse(null);
        System.out.println(str);
    }

    private List<Book> books() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "java", 99D, LocalDate.parse("2018-12-11")));
        books.add(new Book(2L, "php", 55D, LocalDate.parse("1992-09-16")));
        books.add(new Book(3L, "python", 55D, LocalDate.parse("2018-12-11")));
        books.add(new Book(4L, "vue", 55D, LocalDate.parse("2018-12-11")));
        books.add(new Book(5L, "jquery", 55D, LocalDate.parse("2018-12-11")));
        books.add(new Book(6L, "linux", 55D, LocalDate.parse("2018-12-11")));
        books.add(new Book(77L, "tomcat", 76D, LocalDate.parse("2018-12-11")));
        books.add(new Book(8L, "nginx", 99D, LocalDate.parse("2011-01-14")));
        return books;
    }

    private class SubBook {
        private String bookname;
        private int size;

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    private class Book {
        private Long id;
        private String name;
        private Double price;
        private LocalDate publishTime;
        private SubBook subBook;

        public Book() {
        }

        public Book(Long id, String name, Double price, LocalDate publishTime) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.publishTime = publishTime;
        }

        public SubBook getSubBook() {
            return subBook;
        }

        public void setSubBook(SubBook subBook) {
            this.subBook = subBook;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public LocalDate getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(LocalDate publishTime) {
            this.publishTime = publishTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return Objects.equals(price, book.price) &&
                    Objects.equals(publishTime, book.publishTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(price, publishTime);
        }

        @Override
        public String toString() {
            return "Book{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", publishTime=" + publishTime +
                    '}';
        }
    }

}
