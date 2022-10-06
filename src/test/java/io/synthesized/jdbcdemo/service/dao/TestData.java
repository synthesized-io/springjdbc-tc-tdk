package io.synthesized.jdbcdemo.service.dao;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Speaker;
import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class TestData {
    public final static Speaker EGOROV = new Speaker(1001, "Сергей Егоров");
    public final static Speaker TOLKACHEV = new Speaker(1002, "Кирилл Толкачёв");
    public final static Speaker BORISOV = new Speaker(1003, "Евгений Борисов");
    public final static Speaker VALEEV = new Speaker(1004, "Тагир Валеев");

    public final static Conference JPOINT = new Conference(1001, "JPoint");
    public final static Conference JOKER = new Conference(1002, "Joker");

    public final static Talk JAVA914 = new Talk(1001,
            "Java 9-14: Маленькие оптимизации",
            JOKER,
            Status.IN_REVIEW,
            "",
            Collections.singleton(VALEEV));

    public final static Talk REACTIVEORNOT = new Talk(1002,
            "Reactive или не reactive, вот в чем вопрос",
            JPOINT,
            Status.IN_REVIEW,
            "",
            new HashSet<>(Arrays.asList(BORISOV, TOLKACHEV)));

    public final static Talk SIMPSON = new Talk(1003,
            "Не будь Гомером Симпсоном для своего Reactor-а!",
            JPOINT,
            Status.IN_REVIEW,
            "",
            Collections.singleton(EGOROV));

    public final static Talk TESTCONTAINERS = new Talk(1004,
            "Testcontainers: Год спустя",
            JOKER,
            Status.IN_REVIEW,
            "",
            Collections.singleton(EGOROV));
}
