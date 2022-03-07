package com.example.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BlogApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * String(字符串)
     */
    @Test
    public void testStrings() {
        String redisKey = "test:string";

        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    /**
     * Hash(哈希）
     */
    @Test
    public void testHash() {
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "张三");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    /**
     * List（集合）
     */
    @Test
    public void testLists() {
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 0));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    /**
     * set
     */
    @Test
    public void testSets() {
//        String redisKey = "test:teachers";
//
//        redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞", "赵云", "诸葛亮");
        String redisKey = "test:";
//        Set set =redisTemplate.keys(redisKey + "*");
//        System.out.println(redisTemplate.opsForSet().size());

//        List<String> redisKeys  = new ArrayList<>();
//        redisKeys.add("test:teachers");
//        redisKeys.add(redisKey);
//        for(String Key : redisKeys){
//            System.out.println(redisTemplate.opsForSet().size(Key));
//        }
//        redisTemplate.opsForSet().add(redisKey, "孙悟空", "关羽", "张飞", "赵云", "诸葛亮");
// System.out.println(redisTemplate.opsForSet().size(Key));
//        System.out.println(redisTemplate.opsForSet().size(redisKey));
//        System.out.println(redisTemplate.opsForSet().pop(redisKey));
//        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    /**
     * zset
     */
    @Test
    public void testZSets() {
        String redisKey = "test:students";

        redisTemplate.opsForZSet().add(redisKey, "小明", 80);
        redisTemplate.opsForZSet().add(redisKey, "小红", 90);
        redisTemplate.opsForZSet().add(redisKey, "小王", 50);
        redisTemplate.opsForZSet().add(redisKey, "小李", 70);
        redisTemplate.opsForZSet().add(redisKey, "小刚", 60);

        System.out.println("学生人数有："+redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println("小王分数是："+redisTemplate.opsForZSet().score(redisKey, "小王"));
        //reverseRank从大到小的排名
        System.out.println("小王分数排名 "+redisTemplate.opsForZSet().reverseRank(redisKey, "小王")+ "+1");
        System.out.println("排行榜前三："+redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));
        System.out.println("排行榜："+redisTemplate.opsForZSet().reverseRange(redisKey, 0,-1));
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:students");

        System.out.println(redisTemplate.hasKey("test:user"));

        //设置10秒删除
        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);

    }

    // 多次访问同一个key解决方案:批量发送命令,节约网络开销.
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

}
