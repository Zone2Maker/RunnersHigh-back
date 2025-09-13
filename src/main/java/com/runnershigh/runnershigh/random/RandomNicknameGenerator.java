package com.runnershigh.runnershigh.random;

import java.util.List;
import java.util.Random;

public class RandomNicknameGenerator {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<String> adjectives = List.of(
                "빠른", "날쌘", "가벼운", "경쾌한", "힘찬", "총알같은",
                "지치지않는", "거침없는", "열정적인", "성실한", "진지한",
                "포기하지않는", "꾸준한", "즐거운", "행복한", "새벽의",
                "건강한", "활기찬", "행복한", "신나는", "상쾌한",
                "자유로운", "빛나는", "긍정적인", "멋진", "고요한"
        );

        List<String> nouns = List.of(
                "러너", "페이서", "마라토너", "스프린터", "챔피언",
                "발걸음", "심장", "치타", "가젤", "표범", "독수리",
                "제비", "돌고래", "말", "번개", "바람", "태양", "구름",
                "엔진", "운동화", "에너지", "날쌘돌이", "햄스터", "강아지",
                "부스터"
        );

        Random random = new Random();

        String adj = adjectives.get(random.nextInt(adjectives.size()));
        String noun = nouns.get(random.nextInt(nouns.size()));
        int num = random.nextInt(1000);
        System.out.println(adj + ' ' + noun + num);



    }

}
