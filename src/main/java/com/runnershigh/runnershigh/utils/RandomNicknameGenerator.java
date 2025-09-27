package com.runnershigh.runnershigh.utils;

import java.util.List;
import java.util.Random;

public class RandomNicknameGenerator {

    private static final List<String> adjectives = List.of(
            "달리는", "질주하는", "성난", "배고픈", "지치지않는", "쾌속질주",
            "바람을가르는", "땀흘리는", "가벼운", "강철의", "불타는", "도시의", "새벽의", "심야의", "주말의", "초보",
            "프로급", "유리무릎", "숨가쁜", "행복한", "슬픈", "비장한", "빠른", "날쌘", "경쾌한",
            "힘찬", "총알같은", "거침없는", "열정적인", "성실한", "진지한", "포기하지않는",
            "꾸준한", "즐거운", "건강한", "활기찬", "신나는", "상쾌한", "자유로운", "빛나는", "긍정적인", "멋진", "고요한"
    );

    private static final List<String> nouns = List.of(
            "치타", "거북이", "황소", "가젤", "야생마", "칼새", "러너",
            "마라토너", "페이서", "스프린터", "추노꾼", "심장", "다리",
            "발바닥", "엔진", "기록", "번개", "족발", "치킨", "국밥",
            "아아메", "핫식스", "챔피언", "발걸음", "햄스터", "강아지", "부스터",
            "표범", "독수리", "제비", "돌고래", "말", "바람", "태양", "구름", "운동화", "에너지", "날쌘돌이"
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
