package com.runnershigh.runnershigh.utils;

import java.util.List;
import java.util.Random;

public class RandomNicknameGenerator {

    private static final List<String> adjectives = List.of(
            "빠른", "날쌘", "가벼운", "경쾌한", "힘찬", "총알같은",
            "지치지않는", "거침없는", "열정적인", "성실한", "진지한",
            "포기하지않는", "꾸준한", "즐거운", "행복한", "새벽의",
            "건강한", "활기찬", "행복한", "신나는", "상쾌한",
            "자유로운", "빛나는", "긍정적인", "멋진", "고요한"
    );

    private static final List<String> nouns = List.of(
            "러너", "페이서", "마라토너", "스프린터", "챔피언",
            "발걸음", "심장", "치타", "가젤", "표범", "독수리",
            "제비", "돌고래", "말", "번개", "바람", "태양", "구름",
            "엔진", "운동화", "에너지", "날쌘돌이", "햄스터", "강아지",
            "부스터"
    );

    private static final Random random = new Random();

    // 다른 클래스에서 호출 가능한 메서드
    public static String generate() {
        String adj = adjectives.get(random.nextInt(adjectives.size()));
        String noun = nouns.get(random.nextInt(nouns.size()));
        int num = random.nextInt(1000); // 0~999
        return adj + " " + noun + num;
    }
}
